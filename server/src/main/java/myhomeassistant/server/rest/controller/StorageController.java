package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.rest.RequestParser;
import myhomeassistant.server.rest.response.SuccessResponse;
import myhomeassistant.server.rest.service.StorageService;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StorageController {

    public static String getAllFiles(Request request, Response response) {
        return new Gson().toJson(StorageService.getAllFiles(false));
    }

    public static HttpServletResponse download(Request request, Response response) throws IOException {
        return StorageService.download(response, request.params("file"));
    }

    public static String upload(Request request, Response response) throws IOException {
        StorageService.uploadFile(new RequestParser(request));
        return new SuccessResponse().toString();
    }

    public static String generateZIP(Request request, Response response) throws IOException {
        StorageService.generateZIP();
        return new SuccessResponse().toString();
    }

    public static String deleteFile(Request request, Response response) {
        StorageService.deleteFile(request.params("file"));
        return new SuccessResponse().toString();
    }
}
