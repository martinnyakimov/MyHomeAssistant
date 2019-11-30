package myhomeassistant.server.util;

import com.github.myhomeassistant.util.MHAUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class MainUtil {
    private static String FORMAT_RESET = "\033[0m";


    public static void textToSpeech(String answer) {
        try {
            System.out.println("Answer: " + answer);
            MHAUtils.textToSpeech(answer);
        } catch (IOException e) {
            System.out.println("Error!"); // TODO: send the error to the mobile app
        }
    }

    public static void killProcess(Long pid) throws IOException {
        Runtime.getRuntime().exec("kill -9 " + pid);
    }

    public static String selectRandomElementFromArray(String[] array) {
        return array[(int) (Math.random() * array.length)];
    }

    public static void printErrorMessage(String message) {
        String COLOR_RED = "\u001b[31m";

        System.out.println(COLOR_RED + message + FORMAT_RESET);
    }

    public static String printBoldString(String string) {
        String BOLD = "\033[0;1m";

        return BOLD + string + FORMAT_RESET;
    }

    public static void joinFiles(File destination, File[] sources) throws IOException {
        OutputStream output = null;
        try {
            output = createAppendableStream(destination);
            for (File source : sources) {
                appendFile(output, source);
            }
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    private static BufferedOutputStream createAppendableStream(File destination) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(destination, true));
    }

    private static void appendFile(OutputStream output, File source) throws IOException {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(source));
            IOUtils.copy(input, output);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
