import {Component} from '@angular/core';
import {NavController} from 'ionic-angular';
import {SpeechRecognition} from '@ionic-native/speech-recognition';
import {Constants} from "../../utils/constants";
import {UIUtil} from "../../utils/ui.util";
import {DeviceUtil} from "../../utils/device.util";
import {ApiProvider} from "../../providers/api";
import {isEmpty} from "../../utils/functions.util";
import {AboutPage} from "../about/about";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  isRecording: boolean = false;
  socket = null;
  serverIp: string = null;
  serverVersion: number;
  isConnected: boolean;
  isConnectionRemote: boolean = Constants.IS_CONNECTION_REMOTE;

  roomTemperature: string = "...";
  city: string;
  cityTemperature: string;
  cityPicture: string = null;

  constructor(public speechRecognition: SpeechRecognition, public uiUtil: UIUtil, public deviceUtil: DeviceUtil,
              public navCtrl: NavController, public apiProvider: ApiProvider) {
  }

  async ngOnInit() {
    this.serverIp = Constants.IP_OR_URL;
    this.serverVersion = await this.apiProvider.getServerVersion();
    this.isConnected = !isEmpty(this.serverIp) && this.serverVersion != null;

    if (this.isConnected) {
      this.socket = new WebSocket(Constants.WEBSOCKET_URL);

      this.roomTemperature = await this.apiProvider.getRoomTemperature();

      let cityTemperatureObj = await this.apiProvider.getCityTemperature();
      if (cityTemperatureObj) {
        this.city = Object.keys(cityTemperatureObj)[0];
        this.cityTemperature = cityTemperatureObj[this.city];

        fetch('https://pixabay.com/api/?key=15355582-c7e80f568e7923da74c907f4c&q=' + this.city + '%20city&image_type=photo&pretty=true')
          .then(res => res.json())
          .then(
            (result) => {
              let picObj = result.hits[Object.keys(result.hits)[0]]; // Get first image
              if (picObj == undefined) {
                this.cityPicture = '/assets/imgs/not-found.png';
              } else {
                this.cityPicture = picObj['webformatURL'];
              }
            },
          )
      }
    }

    // Ask for permission
    if (this.deviceUtil.isCordova()) {
      this.speechRecognition.hasPermission().then((hasPermission: boolean) => {
        if (!hasPermission) {
          this.speechRecognition.requestPermission();
        }
      });
    }
  }

  async startListening() {
    let userId = await this.apiProvider.getUserByUUID(this.deviceUtil.getUUID())['id'];
    this.speechRecognition.hasPermission().then((hasPermission: boolean) => {
      if (hasPermission) {
        this.speechRecognition.startListening({language: 'en-US'}).subscribe(matches => {
          if (matches[0]) {
            let command = {
              user_id: userId == null ? 1 : userId,
              input: matches[0],
            };
            this.socket.send(JSON.stringify(command));
          }
        });

        this.isRecording = true;
      } else {
        this.uiUtil.showToast({message: "The application does not have voice recording permission!"})
      }
    });
  }

  stopListening() {
    this.speechRecognition.stopListening().then(() => {
      this.isRecording = false;
    });
  }

  goToAboutPage() {
    this.navCtrl.setRoot(AboutPage);
  }
}
