package myhomeassistant.server;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.github.myhomeassistant.util.MHAUtils;
import myhomeassistant.server.db.DatabaseConnection;
import myhomeassistant.server.rest.Routes;
import myhomeassistant.server.service.ActionToClassMapper;
import myhomeassistant.server.service.NLPService;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.UserInputObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static myhomeassistant.server.util.MainUtil.*;
import static spark.Spark.*;

public class Main {

    public static final String VERSION = "0.1";

    public static void main(String[] args) throws SQLException, IOException {
        // Disable info messages, show only warnings and errors
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ERROR);

        if (checkRequirements()) {
            // Init web server
            webSocket("/socket", WebSocketHandler.class);
            staticFiles.location("/public");
            staticFiles.expireTime(600);
            port(8080);
            Routes.init();

            // Init database
            DatabaseConnection databaseConnection = new DatabaseConnection();
            databaseConnection.initDatabase();

            // Get IP
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", Constants.COMMAND_GET_IP);
            final String IP = IOUtils.toString(pb.start().getInputStream(), StandardCharsets.UTF_8);

            System.out.println("Welcome to " + printBoldString("MyHomeAssistant " + VERSION) + "!");
            System.out.println("Device's IP: " + printBoldString(IP));

            // Init console and security log
            final ExecutorService pool = Executors.newFixedThreadPool(2);
            pool.execute(Main::console);
            pool.execute(Main::securityLog);

        } else {
            printErrorMessage("Error!");
            printErrorMessage("You have not installed the necessary software:");

            if (MHAUtils.TTSSoftwarePath().equals(""))
                printErrorMessage("Pico Text-to-Speech => sudo apt-get install libttspico-utils");
        }

    }

    private static boolean checkRequirements() {
        return !MHAUtils.TTSSoftwarePath().equals("");
    }

    private static void console() {
        Scanner sc = new Scanner(System.in);
        System.out.println(" ========================= " + printBoldString("Console (voice command simulation)") + " ========================= ");
        while (true) {
            try {
                System.out.print("> ");
                String input = sc.nextLine();
                if (input.equals("retrain")) {
                    NLPService.trainCategorizerModel();
                } else {
                    ActionToClassMapper.detectActionAndRedirectToClass(new UserInputObject(null, input));
                }
            } catch (Exception e) {
                textToSpeech(Constants.ERROR);
            }
        }
    }

    private static void securityLog() {
        // TODO
    }
}
