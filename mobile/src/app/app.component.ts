import {Component, ViewChild} from '@angular/core';
import {Nav, Platform} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';
import {Network} from "@ionic-native/network";
import {Device} from "@ionic-native/device";

import {HomePage} from '../pages/home/home';
import {UsersPage} from '../pages/users/users';
import {ModulesPage} from "../pages/modules/modules";
import {AboutPage} from "../pages/about/about";

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) nav: Nav;
  uuid: string = null;

  rootPage: any = HomePage;
  pages: Array<{ title: string, component: any, icon: string }>;

  constructor(public platform: Platform, public statusBar: StatusBar, public splashScreen: SplashScreen,
              private network: Network, private device: Device) {
    this.initializeApp();

    this.pages = [
      {title: 'Home', component: HomePage, icon: "home"},
      {title: 'Users', component: UsersPage, icon: "people"},
      {title: 'Modules', component: ModulesPage, icon: "apps"},
      {title: 'About', component: AboutPage, icon: "information-circle"},
    ];

  }

  ngOnInit() {
    this.platform.ready().then(() => {
      this.uuid = this.device.uuid;
    });
  }

  initializeApp() {
    this.platform.ready().then(() => {
      this.statusBar.styleDefault();
      this.splashScreen.hide();
    });
  }

  openPage(page) {
    this.nav.setRoot(page.component);
  }
}
