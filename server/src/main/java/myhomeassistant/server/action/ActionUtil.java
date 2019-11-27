package myhomeassistant.server.action;

import java.util.Map;

public class ActionUtil extends Action {

    @Override
    public void implementation(Integer userId) {
    }

    public Map<String, Long> getActionProcesses() {
        return this.getProcesses();
    }
}
