import {Component} from '@angular/core';
import {NavController} from "ionic-angular";
import {ApiProvider} from "../../../providers/api";
import {Constants} from "../../../utils/constants";

@Component({
  selector: 'page-storage',
  templateUrl: 'storage.html'
})
export class StoragePage {

  file: Blob = null;
  fileName: String = null;
  isFileSelected: boolean = this.fileName != null && this.fileName != '';
  isUploading: boolean = false;

  constructor(public navCtrl: NavController, public apiProvider: ApiProvider) {
  }

  getFileName(event) {
    this.file = event.target.files[0];
    this.fileName = this.file['name'];
    this.isFileSelected = this.fileName != null && this.fileName != '';
  }

  async upload() {
    this.isUploading = true;
    let reader = new FileReader();
    let _this = this;

    reader.readAsDataURL(this.file);
    reader.onload = async function () {
      await _this.apiProvider.uploadFile({name: _this.fileName, content: reader.result})
        .then(response => {
          if (response['status'] == Constants.SUCCESS) {
            _this.isUploading = false;
            _this.selectNewFile();
          }
        });
    };
  }

  selectNewFile() {
    this.navCtrl.setRoot(StoragePage);
  }
}
