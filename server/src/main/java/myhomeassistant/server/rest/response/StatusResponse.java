package myhomeassistant.server.rest.response;

public enum StatusResponse {
    SUCCESS("SUCCESS"), ERROR("ERROR");

    private final String status;

    StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
