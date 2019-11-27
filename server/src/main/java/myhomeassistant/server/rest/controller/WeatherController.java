package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.rest.RequestParser;
import myhomeassistant.server.rest.response.SuccessResponse;
import myhomeassistant.server.rest.service.WeatherService;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class WeatherController {

    public static String getSettings(Request request, Response response) throws SQLException {
        return new Gson().toJson(WeatherService.getSettings());
    }

    public static String updateSettings(Request request, Response response) throws SQLException {
        RequestParser parser = new RequestParser(request);

        WeatherService.updateSettings(parser.get("city"), parser.get("units"));
        return new SuccessResponse().toString();
    }

}
