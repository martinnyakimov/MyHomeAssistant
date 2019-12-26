package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.rest.RequestParser;
import myhomeassistant.server.rest.response.SuccessResponse;
import myhomeassistant.server.rest.service.StorageService;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class StorageController {

    public static String getAllFiles(Request request, Response response) throws IOException {
        return new Gson().toJson(StorageService.getAllFiles());
    }

    public static String upload(Request request, Response response) throws IOException {
        StorageService.uploadFile(new RequestParser(request));
        return new SuccessResponse().toString();
    }
}
