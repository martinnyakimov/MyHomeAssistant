import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import {IonicApp, IonicErrorHandler, IonicModule} from 'ionic-angular';

import {MyApp} from './app.component';

import {HttpClientModule} from '@angular/common/http';
import {StatusBar} from '@ionic-native/status-bar';
import {SplashScreen} from '@ionic-native/splash-screen';
import {SpeechRecognition} from '@ionic-native/speech-recognition';
import {Device} from '@ionic-native/device';
import {ProgressBarModule} from "angular-progress-bar"
import {Network} from "@ionic-native/network";
import {UIUtil} from "../utils/ui.util";
import {DeviceUtil} from "../utils/device.util";

import {ApiProvider} from "../providers/api";
import {HomePage} from "../pages/home/home";
import {UsersPage, UserDetailsPage} from "../pages/users/users";
import {ModulesPage} from "../pages/modules/modules";
import {AboutPage} from "../pages/about/about";
import {MusicPage, SongDetailsPage} from "../pages/modules/music/music";
import {StoragePage} from "../pages/modules/storage/storage";
import {WeatherPage} from "../pages/modules/weather/weather";
import {SecurityPage} from "../pages/modules/security/security";

let components = [
  MyApp,
  HomePage,
  UsersPage,
  UserDetailsPage,
  ModulesPage,
  AboutPage,

  // Modules
  MusicPage,
  SongDetailsPage,
  StoragePage,
  WeatherPage,
  SecurityPage,
];

@NgModule({
  declarations: components,
  imports: [
    BrowserModule,
    HttpClientModule,
    IonicModule.forRoot(MyApp, {}, {
      links: [
        {component: HomePage, name: 'Home', segment: 'home'},
        {component: UsersPage, name: 'Users', segment: 'users'},
        {component: ModulesPage, name: 'Modules', segment: 'modules'},
        {component: AboutPage, name: 'About', segment: 'about'},
        {component: MusicPage, name: 'Music', segment: 'music'},
        {component: StoragePage, name: 'Storage', segment: 'storage'},
        {component: WeatherPage, name: 'Weather', segment: 'weather'},
        {component: SecurityPage, name: 'Security', segment: 'security'},
      ]
    }),
    ProgressBarModule,
  ],
  bootstrap: [IonicApp],
  entryComponents: components,
  providers: [
    StatusBar,
    SplashScreen,
    SpeechRecognition,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    Device,
    Network,
    UIUtil,
    DeviceUtil,
    ApiProvider,
  ]
})
export class AppModule {
}
