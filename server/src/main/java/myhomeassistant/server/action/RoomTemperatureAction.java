package myhomeassistant.server.action;

import myhomeassistant.server.service.MainUtilService;
import myhomeassistant.server.util.MainUtil;

import java.io.IOException;

public class RoomTemperatureAction extends Action {

    @Override
    public void implementation(Integer userId) {
        try {
            int temp = MainUtilService.getRoomTemperature();
            MainUtil.textToSpeech("The temperature in the room is " + temp + " degrees");
        } catch (IOException e) {
            MainUtil.textToSpeech("Cannot access sensor data!");
        }
    }
}
