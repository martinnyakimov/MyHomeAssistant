package myhomeassistant.server.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Request;

public class RequestParser {
    private JsonObject requestObj;

    public RequestParser(Request request) {
        requestObj = new JsonParser().parse(request.body()).getAsJsonObject();
    }

    public String get(String field) {
        try {
            return requestObj.get(field).getAsString();
        } catch (Exception e) {
            return null;
        }
    }
}
