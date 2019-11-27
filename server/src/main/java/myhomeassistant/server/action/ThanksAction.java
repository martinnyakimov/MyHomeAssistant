package myhomeassistant.server.action;

import myhomeassistant.server.util.MainUtil;

public class ThanksAction extends Action {

    @Override
    public void implementation(Integer userId) {
        MainUtil.textToSpeech(MainUtil.selectRandomElementFromArray(new String[]
                {"You’re welcome", "Glad to help", "My pleasure"}
        ));
    }
}
