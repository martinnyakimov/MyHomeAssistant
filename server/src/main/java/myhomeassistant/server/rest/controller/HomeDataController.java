package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.db.model.WeatherSettings;
import myhomeassistant.server.rest.service.WeatherService;
import myhomeassistant.server.service.MainUtilService;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;

public class HomeDataController {

    public static String getRoomTemperature(Request request, Response response) {
        String roomTemp;
        try {
            roomTemp = MainUtilService.getRoomTemperature() + getTemperatureUnit();
        } catch (IOException e) {
            roomTemp = "N/A";
        }
        return new Gson().toJson(roomTemp);
    }

    public static String getCityTemperature(Request request, Response response) throws SQLException, IOException {
        WeatherSettings settings = WeatherService.getSettings();
        String temp = String.valueOf(MainUtilService.getTemperatureInCity(settings).intValue());

        JSONObject json = new JSONObject();
        json.put(settings.getCity(), temp + getTemperatureUnit());
        return json.toString();
    }

    private static String getTemperatureUnit() {
        return MainUtilService.getTemperatureUnit().equals("metric") ? "°C" : "°F";
    }
}
