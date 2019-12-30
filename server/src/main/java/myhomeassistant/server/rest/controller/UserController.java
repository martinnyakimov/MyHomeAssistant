package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.db.model.User;
import myhomeassistant.server.rest.RequestParser;
import myhomeassistant.server.rest.response.ErrorResponse;
import myhomeassistant.server.rest.response.SuccessResponse;
import myhomeassistant.server.rest.service.UserService;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

public class UserController {

    public static String getAllUsers(Request request, Response response) {
        List<User> users = UserService.getAllUsers();
        return new Gson().toJson(users);
    }

    public static String getUserById(Request request, Response response) throws SQLException {
        User user = UserService.getUserById(Integer.valueOf(request.params("id")));
        return new Gson().toJson(user);
    }

    public static String getUserByUUID(Request request, Response response) throws SQLException {
        User user = UserService.getUserByUUID(request.params("uuid").trim());
        return new Gson().toJson(user);
    }

    public static String createUser(Request request, Response response) throws SQLException {
        RequestParser parser = new RequestParser(request);

        if (UserService.getUserByUUID(parser.get("uuid")) == null) {
            UserService.createUser(parser.get("name"), parser.get("email"), parser.get("uuid"));
            return new SuccessResponse().toString();
        }
        return new ErrorResponse("User with this UUID already exists.").toString();
    }

    public static String updateUser(Request request, Response response) throws SQLException {
        RequestParser parser = new RequestParser(request);
        int id = Integer.valueOf(request.params("id"));

        if (UserService.getUserByUUID(parser.get("uuid"), id) == null) {
            UserService.updateUser(id, parser.get("name"), parser.get("email"), parser.get("uuid"));
            return new SuccessResponse().toString();
        }
        return new ErrorResponse("User with this UUID already exists.").toString();
    }

    public static String deleteUser(Request request, Response response) throws SQLException {
        UserService.deleteUser(Integer.valueOf(request.params("id")));
        return new SuccessResponse().toString();
    }
}
