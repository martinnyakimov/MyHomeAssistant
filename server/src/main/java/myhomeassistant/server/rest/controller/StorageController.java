package myhomeassistant.server.rest.controller;

import myhomeassistant.server.rest.response.SuccessResponse;
import myhomeassistant.server.rest.service.StorageService;
import spark.Request;
import spark.Response;

import javax.servlet.ServletException;
import java.io.IOException;

public class StorageController {

    public static String upload(Request request, Response response) throws IOException, ServletException {
        StorageService.uploadFile(request);
        return new SuccessResponse().toString();
    }
}
