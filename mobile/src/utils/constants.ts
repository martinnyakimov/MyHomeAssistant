export class Constants {
  public static IP = localStorage.getItem("SERVER_IP");
  private static PORT = 8080;

  public static SUCCESS = "SUCCESS";
  public static ERROR = "ERROR";

  public static API_URL = `http://${Constants.IP}:${Constants.PORT}/api/`;
  public static WEBSOCKET_URL = `ws://${Constants.IP}:${Constants.PORT}/socket`;
}
