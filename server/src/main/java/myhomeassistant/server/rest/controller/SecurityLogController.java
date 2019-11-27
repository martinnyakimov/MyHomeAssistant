package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.rest.response.SuccessResponse;
import myhomeassistant.server.rest.service.SecurityLogService;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class SecurityLogController {

    public static String getLog(Request request, Response response) throws SQLException {
        return new Gson().toJson(SecurityLogService.getLog());
    }

    public static String add(Request request, Response response) throws SQLException {
        SecurityLogService.add();
        return new SuccessResponse().toString();
    }

    public static String deleteLogItem(Request request, Response response) throws SQLException {
        SecurityLogService.deleteLogItem(Long.valueOf(request.params("id")));

        return new SuccessResponse().toString();
    }
}
