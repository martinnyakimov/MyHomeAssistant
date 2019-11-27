package myhomeassistant.server.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;
import myhomeassistant.server.util.UserInputObject;
import opennlp.tools.doccat.DoccatModel;
import org.apache.commons.io.FileUtils;

public class ActionToClassMapper {

    private static Map<String, String> actionMap = new HashMap<>();

    static {
        actionMap.put("greeting", "GreetingAction");
        actionMap.put("thanks", "ThanksAction");
        actionMap.put("not_found", "CommandNotFoundAction");
        actionMap.put("play_music", "PlayMusicAction");
        actionMap.put("stop_music", "StopMusicAction");
        actionMap.put("weather", "WeatherAction");
        actionMap.put("room_temperature", "RoomTemperatureAction");
    }

    private static void pluginMapper() throws IOException {
        // Load plugins from the config file
        File file = new File(System.getProperty("user.dir") + "/plugins/config.json");
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(content);

        Map<String, String> actionToPluginMap = new HashMap<>();
        for (String jarFile : jsonObject.keySet()) {
            for (String action : jsonObject.get(jarFile).getAsJsonObject().keySet())
                actionToPluginMap.put(action, jarFile);

        }
        // Merge plugins with the built-in actions
        actionMap.putAll(actionToPluginMap);
    }

    public static void detectActionAndRedirectToClass(UserInputObject userInput) throws Exception {
        String category = getDetectedCategory(userInput.getInput());

        // Get plugins
        pluginMapper();

        if (category != null) {
            String actionClassOrPlugin = actionMap.get(category);

            if (actionClassOrPlugin == null) {
                MainUtil.textToSpeech("Plugin not found!");
            } else if (actionClassOrPlugin.contains(".jar")) {
                //TODO: If Undefined plugin -> send message to the mobile app
                Runtime.getRuntime().exec(String.format("java -jar %s %s",
                        System.getProperty("user.dir") + "/plugins/" + actionClassOrPlugin, category));
            } else {
                // Call the class to implement the action
                Class<?> className = Class.forName("myhomeassistant.server.action." + actionClassOrPlugin);

                Object instanceOfClass = className.getDeclaredConstructor().newInstance();
                className.getDeclaredMethod("implementation", Integer.class)
                        .invoke(instanceOfClass, userInput.getUserId());
            }
        } else {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }

    private static String getDetectedCategory(String userInput) throws IOException {
        DoccatModel model = NLPService.deserializeModel();

        String[] sentences = NLPService.breakSentences(userInput);
        String detectedCategory = null;

        for (String sentence : sentences) { //TODO: if sentences are more than 1
            String[] tokens = NLPService.tokenizeSentence(sentence);
            detectedCategory = NLPService.detectCategory(model, tokens);

            //NB: removed because it is very slow on Raspberry Pi
            //String[] posTags = NLPService.detectPOSTags(tokens);
            //String[] lemmas = NLPService.lemmatizeTokens(tokens, posTags);
        }
        return detectedCategory;
    }
}
