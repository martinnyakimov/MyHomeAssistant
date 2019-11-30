
import {Injectable} from "@angular/core";
import {Device} from "@ionic-native/device";
import {Platform} from "ionic-angular";

@Injectable()
export class DeviceUtil {
  constructor(public device: Device, public plt: Platform) {
  }

  whenPlatformReady() {
    return this.plt.ready();
  }

  getUUID() {
    return this.device.uuid;
  }

  isCordova() {
    return this.plt.is('cordova');
  }

  isIos() {
    return this.plt.is('ios');
  }

}
