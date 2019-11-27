package myhomeassistant.server.util;

import com.github.myhomeassistant.util.MHAUtils;

import java.io.IOException;

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
}
