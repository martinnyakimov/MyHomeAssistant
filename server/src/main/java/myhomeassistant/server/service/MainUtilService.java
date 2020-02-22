package myhomeassistant.server.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import myhomeassistant.server.db.model.WeatherSettings;
import myhomeassistant.server.rest.service.WeatherService;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class MainUtilService {

    public static String getTemperatureUnit() {
        try {
            return WeatherService.getSettings().getUnits();
        } catch (SQLException e) {
            return "metric";
        }
    }

    public static int getRoomTemperature() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.dir") + "/lib/room_temp");

        // 0 - Celsius; 1 - Fahrenheit
        String[] tempArr = IOUtils.toString(pb.start().getInputStream(), StandardCharsets.UTF_8).split("\\|");
        return (int) Double.parseDouble(tempArr[getTemperatureUnit().equals("metric") ? 0 : 1]);
    }

    public static Double getTemperatureInCity(WeatherSettings settings) throws IOException {
        String city = settings.getCity();
        String units = settings.getUnits();
        final String API_KEY = "9b43e37d5e702a3d06f973a23f785112";

        String getData = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&appid=%s",
                city, units, API_KEY);

        URL url = new URL(getData);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        JsonObject main = root.get("main").getAsJsonObject();

        return main.get("temp").getAsDouble();
    }
}
