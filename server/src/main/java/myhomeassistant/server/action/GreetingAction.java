package myhomeassistant.server.action;

import myhomeassistant.server.util.MainUtil;

public class GreetingAction extends Action {

    @Override
    public void implementation(Integer userId) {
        MainUtil.textToSpeech(MainUtil.selectRandomElementFromArray(
                new String[]{"Hello", "Hi"}) + ", how can I help you?");
    }
}
