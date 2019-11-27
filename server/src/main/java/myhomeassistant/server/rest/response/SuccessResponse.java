package myhomeassistant.server.rest.response;

import com.google.gson.Gson;

public class SuccessResponse {

    private String response;

    public SuccessResponse() {
        response = new Gson().toJson(new BaseResponse(StatusResponse.SUCCESS));
    }

    public String toString() {
        return response;
    }
}
