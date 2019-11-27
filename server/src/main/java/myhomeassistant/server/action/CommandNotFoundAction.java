package myhomeassistant.server.action;

import myhomeassistant.server.util.MainUtil;

public class CommandNotFoundAction extends Action {

    @Override
    public void implementation(Integer userId) {
        MainUtil.textToSpeech("Command not found. Please try again!");
    }
}
