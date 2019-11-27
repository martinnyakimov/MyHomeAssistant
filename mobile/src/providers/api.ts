import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Constants} from "../utils/constants";
import {UIHelper} from "../utils/uihelper.util";

@Injectable()
export class ApiProvider {

  constructor(public http: HttpClient, private uiHelper: UIHelper) {
  }

  public async getServerVersion() {
    return await this.performGetRequest("version");
  }

  /*========================= USERS =========================*/
  public async getAllUsers() {
    return await this.performGetRequest("users");
  }

  public async getUserById(id: number) {
    return await this.performGetRequest("users/" + id);
  }

  public async getUserByUUID(uuid: string) {
    return await this.performGetRequest("users/uuid/" + uuid);
  }

  public async createUser(user: Object) {
    return await this.performPostRequest("users/create", user);
  }

  public async updateUser(id: number, user: Object) {
    return await this.performPostRequest("users/" + id, user);
  }

  public async deleteUser(id: number) {
    return await this.performPostRequest("users/" + id + "/delete", {});
  }

  /*========================= ABOUT =========================*/
  public async getInternalStorageData() {
    return await this.performGetRequest("about/internalStorageData");
  }

  /*========================= MUSIC =========================*/
  public async getAllSongs() {
    return await this.performGetRequest("modules/music/songs");
  }

  public async getSongById(id: number) {
    return await this.performGetRequest("modules/music/songs/" + id);
  }

  public async addSong(body: Object) {
    return await this.performPostRequest("modules/music/songs/add", body);
  }

  public async playSong(id: number) {
    return await this.performGetRequest("modules/music/play/" + id);
  }

  public async deleteSong(id: number) {
    return await this.performPostRequest("modules/music/songs/" + id + "/delete", {});
  }

  public async checkForActiveSong() {
    let isThereActiveSong = await this.performGetRequest("modules/music/activeSong");
    if (isThereActiveSong == null) {
      return 0;
    }
    return isThereActiveSong['data'];
  }

  public async stopMusic() {
    return await this.performGetRequest("modules/music/stop");
  }

  /*========================= WEATHER =========================*/
  public async getWeatherSettings() {
    return await this.performGetRequest("modules/weather/settings");
  }

  public async saveWeatherSettings(body: Object) {
    return await this.performPostRequest("modules/weather/updateSettings", body);
  }

  /*========================= SECURITY =========================*/
  public async getSecurityLog() {
    return await this.performGetRequest("modules/security/log");
  }

  public async deleteLogItem(id: number) {
    return await this.performPostRequest("modules/security/delete/" + id, {});
  }

  /*========================= REQUESTS =========================*/
  private async performGetRequest(route: String) {
    return await this.http.get(Constants.API_URL + route).toPromise()
      .catch((error: any) => {
        this.uiHelper.errorToast("Error! Please check your internet connection or server's IP address.");
        return null;
      });
  }

  private async performPostRequest(route: String, body: Object) {
    return await this.http.post(Constants.API_URL + route, JSON.stringify(body),
      {headers: {"Content-Type": "application/json"}}).toPromise()
      .catch((error: any) => {
        this.uiHelper.errorToast("Error! If you can't solve the problem, contact us.");
        return null;
      });
  }
}
