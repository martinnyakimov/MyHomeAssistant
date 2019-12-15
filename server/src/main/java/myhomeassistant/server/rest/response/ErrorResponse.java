package myhomeassistant.server.rest.response;

import com.google.gson.Gson;

public class ErrorResponse {

    private String response;

    public ErrorResponse(String message) {
        response = new Gson().toJson(new BaseResponse(StatusResponse.ERROR, message));
    }

    public String toString() {
        return response;
    }
}
