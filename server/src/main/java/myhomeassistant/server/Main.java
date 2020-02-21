package myhomeassistant.server;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.github.myhomeassistant.util.MHAUtils;
import myhomeassistant.server.db.DatabaseConnection;
import myhomeassistant.server.rest.Routes;
import myhomeassistant.server.service.ActionToClassMapper;
import myhomeassistant.server.service.NLPService;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;
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

    public static void main(String[] args) throws SQLException {
        // Disable info messages, show only warnings and errors
        initExceptionHandler((e) -> {
            printErrorMessage("The server is already running!");
            MainUtil.exit();
        });
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ERROR);

        if (checkRequirements()) {
            System.out.println(printBoldString("Loading... Please wait."));
            // Init web server
            webSocket("/socket", WebSocketHandler.class);
            staticFiles.location("/public");
            port(8080);
            Routes.init();

            // Init database
            DatabaseConnection databaseConnection = new DatabaseConnection();
            databaseConnection.initDatabase();

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
        // Get IP
        ProcessBuilder getIP = new ProcessBuilder("bash", "-c", Constants.COMMAND_GET_IP);
        String IP = null;
        try {
            IP = IOUtils.toString(getIP.start().getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            printErrorMessage("Error! Failed to get IP address.");
        }

        String url = null;
        try {
            url = MainUtil.startLocalTunnel();
        } catch (Exception e) {
            printErrorMessage("Error! Failed to start local tunnel.");
        }

        System.out.println("Welcome to " + printBoldString("MyHomeAssistant " + VERSION) + "!");
        System.out.println("Device's IP: " + printBoldString(IP.replace("\n", "")));
        if (url != null) {
            System.out.println("Ngrok ID for remote connection: " + printBoldString(url) + " (it changes on every start)");
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("\n========================= " + printBoldString("Console (voice command simulation)") + " =========================");
        while (true) {
            System.out.print("> ");
            try {
                String input = sc.nextLine();

                if (input.equals("retrain")) {
                    NLPService.trainCategorizerModel();
                } else if (input.equals("exit")) {
                    MainUtil.exit();
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
