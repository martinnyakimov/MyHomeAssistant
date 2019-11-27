import {Component} from '@angular/core';
import {ApiProvider} from "../../../providers/api";
import {formatDate} from "../../../utils/functions.util";

@Component({
  selector: 'page-security',
  templateUrl: 'security.html'
})
export class SecurityPage {
  log = [];

  constructor(private apiProvider: ApiProvider) {
  }

  async ngOnInit() {
    await this.getLogList();
  }

  async delete(id: number) {
    await this.apiProvider.deleteLogItem(id);
    await this.getLogList();
  }

  async getLogList() {
    this.log = []; // Clear log
    let log = await this.apiProvider.getSecurityLog();
    for (let logItem in log) {
      let date = new Date(log[logItem]['timestamp'] * 1000);
      log[logItem]['timestamp'] = formatDate(date);

      this.log.push(log[logItem]);
    }
  }
}
