package myhomeassistant.server.rest.service;

import myhomeassistant.server.rest.RequestParser;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Map;

public class StorageService {

    public static Map<String, Integer> getAllFiles() {
        Map<String, Integer> files = new Hashtable<>();
        try {
            Files.walk(Paths.get("storage"))
                    .filter(Files::isRegularFile)
                    .forEach(file ->
                            // Key: filename; Value: size in KB
                            files.put(file.toString().replace("storage/", ""),
                                    (int) file.toFile().length() / 1000)
                    );
        } catch (IOException e) {
        }
        return files;
    }

    public static HttpServletResponse download(Response response, String file) throws IOException {
        HttpServletResponse raw = response.raw();
        response.header("Content-Disposition", "attachment; filename=" + file);
        response.type("application/force-download");

        Path path = Paths.get("storage/" + file);
        byte[] data = Files.readAllBytes(path);

        raw.getOutputStream().write(data);
        raw.getOutputStream().flush();
        raw.getOutputStream().close();
        return raw;
    }

    public static void uploadFile(RequestParser request) throws IOException {
        final String SEPARATOR = ",";
        new File("storage").mkdir();

        if (request.get("content").contains(SEPARATOR)) {
            String encodedFile = request.get("content").split(SEPARATOR)[1];
            byte[] decodedFile = Base64.getDecoder().decode(encodedFile.getBytes(StandardCharsets.UTF_8));

            Path destinationFile = Paths.get("storage/", request.get("name"));
            Files.write(destinationFile, decodedFile);
        }
    }
}
