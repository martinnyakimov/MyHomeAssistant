import {Component} from '@angular/core';
import {ApiProvider} from "../../../providers/api";

@Component({
  selector: 'page-storage',
  templateUrl: 'storage.html'
})
export class StoragePage {

  constructor(public apiProvider: ApiProvider) {
  }

  async fileUpload(event) {
    let file = event.target.files[0];
    let reader = new FileReader();
    let _this = this;

    reader.readAsDataURL(file);
    reader.onload = async function () {
      await _this.apiProvider.uploadFile({name: file['name'], content: reader.result});
    };
  }
}
