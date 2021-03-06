import {Component} from '@angular/core';
import {NavController} from 'ionic-angular';
import {MusicPage} from "./music/music";
import {WeatherPage} from "./weather/weather";
import {SecurityPage} from "./security/security";
import {StoragePage} from "./storage/storage";

@Component({
  selector: 'page-modules',
  templateUrl: 'modules.html'
})
export class ModulesPage {
  modules: Array<{ title: string, component: any, icon: string }> = [];

  constructor(public navCtrl: NavController) {
    this.modules.push(
      {title: 'Music', component: MusicPage, icon: 'musical-note'},
      {title: 'Weather settings', component: WeatherPage, icon: 'partly-sunny'},
      {title: 'Security log', component: SecurityPage, icon: 'videocam'},
      {title: 'File storage', component: StoragePage, icon: 'folder'},
    );
  }

  openModule(module) {
    this.navCtrl.setRoot(module.component);
  }
}
