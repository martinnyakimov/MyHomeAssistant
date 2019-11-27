package myhomeassistant.server.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserInputObject {
    private Integer userId;
    private String input;

    public UserInputObject(Integer userId, String input) {
        this.userId = userId;
        this.input = input;
    }

    public UserInputObject() {
    }

    public Integer getUserId() {
        return userId;
    }

    public String getInput() {
        return input;
    }

    public UserInputObject convertJsonToObject(String json) {
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(json);
        JsonObject rootObject = root.getAsJsonObject();
        
        userId = rootObject.get("user_id").getAsInt();
        input = rootObject.get("input").getAsString();

        return new UserInputObject(userId, input);
    }
}
