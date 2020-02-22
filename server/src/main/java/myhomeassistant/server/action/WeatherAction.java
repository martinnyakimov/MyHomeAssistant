package myhomeassistant.server.action;

import myhomeassistant.server.db.model.WeatherSettings;
import myhomeassistant.server.rest.service.WeatherService;
import myhomeassistant.server.service.MainUtilService;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;

import java.io.IOException;
import java.sql.SQLException;

public class WeatherAction extends Action {

    @Override
    public void implementation(Integer userId) {
        try {
            WeatherSettings settings = WeatherService.getSettings();

            int temp = MainUtilService.getTemperatureInCity(settings).intValue(); // TODO: get another data
            MainUtil.textToSpeech(String.format("The temperature in %s is %d %s", settings.getCity(), temp,
                    (temp == 1 || temp == -1) ? "degree" : "degrees"));
        } catch (IOException | SQLException e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
    }
}
