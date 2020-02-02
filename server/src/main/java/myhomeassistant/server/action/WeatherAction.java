package myhomeassistant.server.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import myhomeassistant.server.db.model.WeatherSettings;
import myhomeassistant.server.rest.service.WeatherService;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class WeatherAction extends Action {

    @Override
    public void implementation(Integer userId) {
        try {
            WeatherSettings settings = WeatherService.getSettings();

            int temp = getTemperatureInCity(settings).intValue(); // TODO: get another data
            MainUtil.textToSpeech(String.format("The temperature in %s is %d %s", settings.getCity(), temp,
                    (temp == 1 || temp == -1) ? "degree" : "degrees"));
        } catch (IOException | SQLException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }

    private Double getTemperatureInCity(WeatherSettings settings) throws IOException {
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
