import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Constants} from "../utils/constants";
import {UIUtil} from "../utils/ui.util";
import {setZeroLengthIfEmpty} from "../utils/functions.util";

@Injectable()
export class ApiProvider {

  constructor(public http: HttpClient, public uiUtil: UIUtil) {
  }

  public async getServerVersion() {
    return await this.performGetRequest("version");
  }

  /*========================= USERS =========================*/
  public async getAllUsers() {
    return setZeroLengthIfEmpty(await this.performGetRequest("users"));
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
    return setZeroLengthIfEmpty(await this.performGetRequest("modules/music/songs"));
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

    return isThereActiveSong == null ? 0 : isThereActiveSong['data'];
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

  /*========================= FILE STORAGE =========================*/
  public async getAllFiles() {
    return setZeroLengthIfEmpty(await this.performGetRequest("modules/storage/getFiles"));
  }

  public async uploadFile(body: Object) {
    return await this.performPostRequest("modules/storage/upload", body);
  }

  /*========================= REQUESTS =========================*/
  private async performGetRequest(route: String) {
    return await this.http.get(Constants.API_URL + route).toPromise()
      .catch((error: any) => {
        this.uiUtil.errorToast("Check the connection with your local server.");
        return null;
      });
  }

  private async performPostRequest(route: String, body: Object) {
    return await this.http.post(Constants.API_URL + route, JSON.stringify(body),
      {headers: {"Content-Type": "application/json"}}).toPromise()
      .catch((error: any) => {
        this.uiUtil.errorToast("If you can't solve the problem, contact us.");
        return null;
      });
  }
}
