package myhomeassistant.server.rest.response;

public class BaseResponse {
    private StatusResponse status;
    private Object data;

    BaseResponse(StatusResponse status) {
        this.status = status;
    }

    public BaseResponse(StatusResponse status, Object data) {
        this.status = status;
        this.data = data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
