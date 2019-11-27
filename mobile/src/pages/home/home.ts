import {Component} from '@angular/core';
import {NavController, Platform} from 'ionic-angular';
import {SpeechRecognition} from '@ionic-native/speech-recognition';
import {Device} from "@ionic-native/device";
import {Constants} from "../../utils/constants";
import {UIHelper} from "../../utils/uihelper.util";
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

  constructor(private speechRecognition: SpeechRecognition, private plt: Platform, private device: Device,
              private uiHelper: UIHelper, public navCtrl: NavController, public apiProvider: ApiProvider) {
  }

  async ngOnInit() {
    this.serverIp = Constants.IP;
    this.serverVersion = await this.apiProvider.getServerVersion();
    this.isConnected = !isEmpty(this.serverIp) && this.serverVersion != null;

    if (this.isConnected) {
      this.socket = new WebSocket(Constants.WEBSOCKET_URL);
    }

    // Ask for permission
    if (this.isCordova()) {
      this.speechRecognition.hasPermission().then((hasPermission: boolean) => {
        if (!hasPermission) {
          this.speechRecognition.requestPermission();
        }
      });
    }
  }

  isCordova() {
    return this.plt.is('cordova');
  }

  isIos() {
    return this.plt.is('ios');
  }

  stopListening() {
    this.speechRecognition.stopListening().then(() => {
      this.isRecording = false;
    });
  }

  async startListening() {
    let userId = await this.apiProvider.getUserByUUID(this.device.uuid)['id'];
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
        this.uiHelper.showToast({message: "The application does not have voice recording permission!"})
      }
    });
  }

  goToAboutPage() {
    this.navCtrl.setRoot(AboutPage);
  }
}
