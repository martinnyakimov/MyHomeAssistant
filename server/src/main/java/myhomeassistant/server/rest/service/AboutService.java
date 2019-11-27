package myhomeassistant.server.rest.service;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

public class AboutService {

    public static Map<String, Double> getInternalStorageData() {
        Map<String, Double> storage = new Hashtable<>();

        File path = new File("/");
        Double totalSpace = convertBytesToGB((double) path.getTotalSpace());
        Double freeSpace = convertBytesToGB((double) path.getUsableSpace());

        storage.put("total", totalSpace);
        storage.put("available", freeSpace);
        return storage;
    }

    private static Double convertBytesToGB(Double bytes) {
        double gb = bytes / (1000 * 1000 * 1000);
        return Math.floor(gb * 100) / 100;
    }
}
