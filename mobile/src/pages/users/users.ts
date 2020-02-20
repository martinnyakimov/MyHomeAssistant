import {Component, ViewChild} from '@angular/core';
import {NavController, NavParams, Navbar, ModalController, ViewController, Events} from 'ionic-angular';
import {ApiProvider} from "../../providers/api";
import {UIUtil} from "../../utils/ui.util";
import {DeviceUtil} from "../../utils/device.util";
import {Constants} from "../../utils/constants";

@Component({
  selector: 'page-users',
  templateUrl: 'users.html'
})
export class UsersPage {
  @ViewChild(Navbar) navBar: Navbar;
  static users = [];

  constructor(public modalCtrl: ModalController, public navParams: NavParams,
              public apiProvider: ApiProvider, public events: Events) {
    events.subscribe("usersRefresh", (users) => {
      UsersPage.users = users;
    });
  }

  async ngOnInit() {
    UsersPage.users = await this.apiProvider.getAllUsers();
  }

  async refresh(refresher) {
    await this.ngOnInit();
    refresher.complete();
  }

  get users() {
    return UsersPage.users;
  }

  create() {
    this.modalCtrl.create(UserDetailsPage, {mode: "create"}).present();
  }

  async selectUser(userId: number) {
    let user = await this.apiProvider.getUserById(userId);
    this.modalCtrl.create(UserDetailsPage, {mode: "edit", user: user}).present();
  }
}

@Component({
  templateUrl: 'user.html'
})
export class UserDetailsPage {
  // View mode
  mode: string;

  // Data for selected user
  id: number = 0;
  name: string = null;
  email: string = null;
  uuid: string = null;
  currentDevice: string = null;

  isDisabled: boolean = false;

  constructor(public navCtrl: NavController, public viewCtrl: ViewController, public navParams: NavParams,
              public apiProvider: ApiProvider, public uiUtil: UIUtil, public deviceUtil: DeviceUtil, public events: Events) {
    this.mode = navParams.get('mode') == null ? 'index' : navParams.get('mode');
    this.currentDevice = this.deviceUtil.getUUID();

    let selectedUser = navParams.get('user');
    if (selectedUser != null) {
      this.id = selectedUser['id'];
      this.name = selectedUser['name'];
      this.email = selectedUser['email'];
      this.uuid = selectedUser['uuid'];
    }
  }

  async save(mode: string) {
    let userObject = {name: this.name, email: this.email, uuid: this.uuid};

    if (this.uiUtil.checkForEmptyField(userObject, ['name'])) {
      this.isDisabled = true;

      if (mode == "create") {
        await this.apiProvider.createUser(userObject)
          .then(response => {
            this.createOrUpdate(response);
          }).catch((error: any) => {
          });
      }
      if (mode == "edit") {
        await this.apiProvider.updateUser(this.id, userObject)
          .then(response => {
            this.createOrUpdate(response);
          }).catch((error: any) => {
          });
      }
    }
  }

  createOrUpdate(response) {
    if (response["status"] == Constants.ERROR) {
      this.uiUtil.errorToast(response["data"]);
      this.isDisabled = false;
    } else {
      this.uiUtil.savedItemToast(response, "User");
      this.dismiss();
    }
  }

  async delete() {
    await this.apiProvider.deleteUser(this.id);
    this.dismiss();
  }

  setUUID() {
    this.uuid = this.currentDevice;
  }

  async dismiss() {
    await this.viewCtrl.dismiss();
    await this.events.publish("usersRefresh", await this.apiProvider.getAllUsers());
  }
}
