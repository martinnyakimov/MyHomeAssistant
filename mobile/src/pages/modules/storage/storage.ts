import {Component} from '@angular/core';
import {Constants} from "../../../utils/constants";
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'page-storage',
  templateUrl: 'storage.html'
})
export class StoragePage {

  ip: string;

  constructor(public sanitizer: DomSanitizer) {
    this.ip = Constants.IP;
  }
}
