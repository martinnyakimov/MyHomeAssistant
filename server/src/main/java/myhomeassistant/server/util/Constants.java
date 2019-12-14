package myhomeassistant.server.util;

public class Constants {
    public static final String ERROR = "Error! Please try again.";
    public static final String API_URL = "http://127.0.0.1:8000/api/";

    public static final String COMMAND_GET_IP = "ifconfig | grep -Eo 'inet (addr:)?([0-9]*\\.){3}[0-9]*' | grep -Eo '([0-9]*\\.){3}[0-9]*' | grep -v '127.0.0.1'";
}
