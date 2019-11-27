package myhomeassistant.server.rest.response;

import com.google.gson.Gson;

public class ErrorResponse {

    private String response;

    public ErrorResponse() {
        response = new Gson().toJson(new BaseResponse(StatusResponse.ERROR));
    }

    public String toString() {
        return response;
    }
}
