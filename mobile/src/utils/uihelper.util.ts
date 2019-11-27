import {ToastController} from "ionic-angular";
import {Injectable} from "@angular/core";
import {Constants} from "./constants";
import {capitalizeFirstLetter} from "./functions.util";

@Injectable()
export class UIHelper {
  constructor(private toastCtrl: ToastController) {
  }

  showToast({message, position = "bottom", duration = 3000}: { message: string, position?: string, duration?: number }) {
    return this.toastCtrl.create({
      message: message,
      duration: duration,
      position: position
    }).present();
  }

  successToast(response, updatedObject: string) {
    if (response["status"] == Constants.SUCCESS) {
      this.showToast({message: updatedObject + " was saved successfully."})
    }
  }

  errorToast(message: string) {
    this.showToast({
      message: message,
      duration: 5000
    });
  }

  checkForEmptyField(target: Object, checkedField) {
    for (let key in target) {
      let notNullField = checkedField.find(field => field == key);

      if (notNullField != null && target[key] == null) {
        this.errorToast("Field \"" + capitalizeFirstLetter(key) + "\" is required!");
        return false;
      }
    }
    return true;
  }
}
