import {Component} from '@angular/core';
import {NavController} from "ionic-angular";
import {ApiProvider} from "../../../providers/api";
import {Constants} from "../../../utils/constants";

@Component({
  selector: 'page-storage',
  templateUrl: 'storage.html'
})
export class StoragePage {

  mode: String = 'index';
  files = [];
  totalFilesSize: String = null;
  file: Blob = null;
  fileName: String = null;
  isFileSelected: boolean = this.fileName != null && this.fileName != '';
  isUploading: boolean = false;
  isConnectionRemote: boolean = Constants.IS_CONNECTION_REMOTE;

  constructor(public navCtrl: NavController, public apiProvider: ApiProvider) {
  }

  async ngOnInit() {
    this.files = [];
    let files = await this.apiProvider.getAllFiles();
    let totalSize = 0;
    for (let file in files) {
      let size = files[file];
      totalSize += size;
      this.files.push({'title': file, 'size': this.prepareFileSize(size)});
    }
    this.totalFilesSize = this.prepareFileSize(totalSize);
  }

  async refresh(refresher) {
    await this.ngOnInit();
    refresher.complete();
  }

  async generateZIP() {
    await this.apiProvider.generateZIP();
    this.showIndexMode(); // Refreshing page
  }

  prepareFileSize(kb: number) {
    if (kb >= 1000) {
      return Math.floor((kb / 1000) * 10) / 10 + ' MB';
    }
    return kb + ' KB'
  }

  async download(title: String) {
    window.open(Constants.API_URL + "modules/storage/download/" + title);
  }

  async delete(title: String) {
    await this.apiProvider.deleteFile(title);
    this.showIndexMode();
  }

  prepareUpload(event) {
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
            _this.showIndexMode();
          }
        });
    };
  }

  showIndexMode() {
    this.navCtrl.setRoot(StoragePage);
  }
}
