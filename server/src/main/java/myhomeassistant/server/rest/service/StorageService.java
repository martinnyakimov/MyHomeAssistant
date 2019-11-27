package myhomeassistant.server.rest.service;


import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageService {

    public static void uploadFile(Request request) throws IOException, ServletException {
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        new File("storage").mkdir();
        Part uploadedFile = request.raw().getPart("file");
        String fileName = uploadedFile.getSubmittedFileName();
        Path out = Paths.get("storage/" + fileName);

        try (final InputStream in = uploadedFile.getInputStream()) {
            Files.copy(in, out);
            uploadedFile.delete();
        }
    }
}
