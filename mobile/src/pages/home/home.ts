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

  constructor(public speechRecognition: SpeechRecognition, public uiUtil: UIUtil, public deviceUtil: DeviceUtil,
              public navCtrl: NavController, public apiProvider: ApiProvider) {
  }

  async ngOnInit() {
    this.serverIp = Constants.IP_OR_URL;
    this.serverVersion = await this.apiProvider.getServerVersion();
    this.isConnected = !isEmpty(this.serverIp) && this.serverVersion != null;

    if (this.isConnected) {
      this.socket = new WebSocket(Constants.WEBSOCKET_URL);
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
