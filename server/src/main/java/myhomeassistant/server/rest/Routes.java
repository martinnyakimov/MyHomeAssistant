package myhomeassistant.server.rest;

import myhomeassistant.server.Main;
import myhomeassistant.server.rest.controller.*;

import static spark.Spark.*;

public class Routes {
    public static void init() {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
        get("/", (request, response) -> "<html><body><h1 style='text-align: center;'>MyHomeAssistant</h2></body></html>");
        path("api/", () -> {
            get("version", (request, response) -> Main.VERSION);
            get("roomTemperature", HomeDataController::getRoomTemperature);
            get("cityTemperature", HomeDataController::getCityTemperature);

            // Users
            get("users", UserController::getAllUsers);
            path("users/", () -> {
                post("create", UserController::createUser);
                get(":id", UserController::getUserById);
                get("/uuid/:uuid", UserController::getUserByUUID);
                post(":id", UserController::updateUser);
                post(":id/delete", UserController::deleteUser);
            });

            // Modules
            path("modules/", () -> {
                path("music/", () -> {
                    get("songs", MusicController::getAllSongs);
                    path("songs/", () -> {
                        get(":id", MusicController::getSongById);
                        post("add", MusicController::addSong);
                        post(":id/delete", MusicController::deleteSong);
                    });
                    get("play/:id", MusicController::playSingle);
                    get("stop", MusicController::stop);
                    get("activeSong", MusicController::activeSong);
                });
                path("storage/", () -> {
                    get("getFiles", StorageController::getAllFiles);
                    get("download/:file", StorageController::download);
                    post("upload", StorageController::upload);
                    get("generateZIP", StorageController::generateZIP);
                    post(":file/delete", StorageController::deleteFile);
                });
                path("security/", () -> {
                    get("log", SecurityLogController::getLog);
                    post("add", SecurityLogController::add);
                    post("delete/:id", SecurityLogController::deleteLogItem);
                });
                path("weather/", () -> {
                    get("settings", WeatherController::getSettings);
                    post("updateSettings", WeatherController::updateSettings);
                });
            });

            // About
            path("about/", () -> {
                get("internalStorageData", AboutController::getInternalStorageData);
            });
        });
    }
}
