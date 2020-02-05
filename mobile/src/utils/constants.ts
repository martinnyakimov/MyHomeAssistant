export class Constants {
  public static IP_OR_URL = localStorage.getItem("SERVER_IP");
  private static PORT = 8080;

  public static IS_CONNECTION_REMOTE = Constants.IP_OR_URL.includes('localtunnel.me');
  private static IP_PORT = Constants.IP_OR_URL + ":" + Constants.PORT;
  public static API_URL = (Constants.IS_CONNECTION_REMOTE ?
    Constants.prepareRemoteURL(Constants.IP_OR_URL) : `http://${Constants.IP_PORT}/`) + `api/`;
  public static WEBSOCKET_URL = `ws://${Constants.IP_PORT}/socket`;

  public static SUCCESS = "SUCCESS";
  public static ERROR = "ERROR";

  private static prepareRemoteURL(URL: String) {
    if (URL.slice(-1) != '/')
      return URL + '/';
    return URL;
  }
}
