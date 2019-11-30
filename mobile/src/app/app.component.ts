import {Component, ViewChild} from '@angular/core';
import {Nav} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';

import {HomePage} from '../pages/home/home';
import {UsersPage} from '../pages/users/users';
import {ModulesPage} from "../pages/modules/modules";
import {AboutPage} from "../pages/about/about";
import {DeviceUtil} from "../utils/device.util";

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) nav: Nav;
  uuid: string = null;

  rootPage: any = HomePage;
  pages: Array<{ title: string, component: any, icon: string }>;

  constructor(public deviceUtil: DeviceUtil, public statusBar: StatusBar, public splashScreen: SplashScreen) {
    this.initializeApp();

    this.pages = [
      {title: 'Home', component: HomePage, icon: "home"},
      {title: 'Users', component: UsersPage, icon: "people"},
      {title: 'Modules', component: ModulesPage, icon: "apps"},
      {title: 'About', component: AboutPage, icon: "information-circle"},
    ];

  }

  ngOnInit() {
    this.deviceUtil.whenPlatformReady().then(() => {
      this.uuid = this.deviceUtil.getUUID();
    });
  }

  initializeApp() {
    this.deviceUtil.whenPlatformReady().then(() => {
      this.statusBar.styleDefault();
      this.splashScreen.hide();
    });
  }

  openPage(page) {
    this.nav.setRoot(page.component);
  }
}
