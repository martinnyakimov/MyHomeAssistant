package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.rest.service.AboutService;
import spark.Request;
import spark.Response;

public class AboutController {

    public static String getInternalStorageData(Request request, Response response) {
        return new Gson().toJson(AboutService.getInternalStorageData());
    }
}
