package myhomeassistant.server.action;

import myhomeassistant.server.rest.service.WeatherService;
import myhomeassistant.server.util.MainUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class RoomTemperatureAction extends Action {

    @Override
    public void implementation(Integer userId) {
        try {
            String units = WeatherService.getSettings().getUnits();
            ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.dir") + "/lib/room_temp");

            // 0 - Celsius; 1 - Fahrenheit
            String[] tempArr = IOUtils.toString(pb.start().getInputStream(), StandardCharsets.UTF_8).split("\\|");
            int temp = (int) Double.parseDouble(tempArr[units.equals("metric") ? 0 : 1]);
            MainUtil.textToSpeech("The temperature in the room is " + temp + " degrees");
        } catch (IOException | SQLException e) {
            MainUtil.textToSpeech("Cannot access sensor data!");
        }
    }
}
