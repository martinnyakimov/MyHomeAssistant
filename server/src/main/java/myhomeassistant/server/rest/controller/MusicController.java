package myhomeassistant.server.rest.controller;

import com.google.gson.Gson;
import myhomeassistant.server.db.model.Song;
import myhomeassistant.server.rest.response.BaseResponse;
import myhomeassistant.server.rest.RequestParser;
import myhomeassistant.server.rest.response.StatusResponse;
import myhomeassistant.server.rest.response.SuccessResponse;
import myhomeassistant.server.rest.service.MusicService;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MusicController {

    public static String getAllSongs(Request request, Response response) throws SQLException {
        List<Song> songs = MusicService.getAllSongs();
        return new Gson().toJson(songs);
    }

    public static String getSongById(Request request, Response response) throws SQLException {
        Song song = MusicService.getSongById(Integer.valueOf(request.params("id")));
        return new Gson().toJson(song);
    }

    public static String addSong(Request request, Response response) throws SQLException, IOException {
        RequestParser parser = new RequestParser(request);

        MusicService.addSong(parser.get("title"), parser.get("url"));
        return new SuccessResponse().toString();
    }

    public static String playSingle(Request request, Response response) throws SQLException {
        MusicService.playSingle(Integer.valueOf(request.params("id")));
        return new SuccessResponse().toString();
    }

    public static String stop(Request request, Response response) {
        MusicService.stop();
        return new SuccessResponse().toString();
    }

    public static String activeSong(Request request, Response response) {
        return new Gson().toJson(new BaseResponse(StatusResponse.SUCCESS, MusicService.activeSong() ? 1 : 0));
    }

    public static String deleteSong(Request request, Response response) throws SQLException {
        MusicService.deleteSong(Integer.valueOf(request.params("id")));
        return new SuccessResponse().toString();
    }
}
