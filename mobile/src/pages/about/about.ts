import {Component} from '@angular/core';
import {ApiProvider} from "../../providers/api";
import {UIUtil} from "../../utils/ui.util";
import {Constants} from "../../utils/constants";
import {isEmpty, trim} from "../../utils/functions.util";

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})
export class AboutPage {
  availableStorage: number;
  totalStorage: number;
  freeStoragePercentage: number;
  serverIp: string;
  isConnected: boolean;

  constructor(public apiProvider: ApiProvider, public uiUtil: UIUtil) {
    this.serverIp = localStorage.getItem("SERVER_IP");
    this.isConnected = !isEmpty(this.serverIp);
  }

  async ngOnInit() {
    let data = await this.apiProvider.getInternalStorageData();
    if (data != null) {
      this.availableStorage = data["available"];
      this.totalStorage = data["total"];
      this.freeStoragePercentage = Math.floor(100 - (this.availableStorage * 100) / this.totalStorage);
    }
  }

  save() {
    if (trim(this.serverIp) == '') {
      localStorage.removeItem("SERVER_IP");
      this.uiUtil.showToast({
        message: "You cleared the IP address field.",
        duration: 5000
      });
    } else {
      localStorage.setItem("SERVER_IP", this.serverIp);
      this.uiUtil.savedItemToast({status: Constants.SUCCESS}, "IP address");
    }
  }
}
