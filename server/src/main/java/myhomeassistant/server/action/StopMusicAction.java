package myhomeassistant.server.action;

import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;

import java.io.IOException;
import java.util.Map;

public class StopMusicAction extends Action {

    @Override
    public void implementation(Integer userId) {
        Map<String, Long> processes = this.getProcesses();

        try {
            MainUtil.killProcess(processes.remove("music"));
        } catch (IOException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }
}
