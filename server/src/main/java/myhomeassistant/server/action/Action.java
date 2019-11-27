package myhomeassistant.server.action;

import java.util.Hashtable;
import java.util.Map;

abstract class Action {

    private static Map<String, Long> processes = new Hashtable<>();

    Map<String, Long> getProcesses() {
        return processes;
    }

    void setProcesses(Map<String, Long> processes) {
        Action.processes = processes;
    }

    public abstract void implementation(Integer userId) throws Exception;
}
