import {Component} from '@angular/core';
import {Events, ModalController, NavParams, ViewController} from 'ionic-angular';
import {ApiProvider} from "../../../providers/api";
import {UIHelper} from "../../../utils/uihelper.util";

@Component({
  selector: 'page-music',
  templateUrl: 'music.html'
})
export class MusicPage {
  static songs = [];
  static isThereActiveSong = 0;

  constructor(private apiProvider: ApiProvider, public modalCtrl: ModalController, public events: Events) {
    events.subscribe("musicRefresh", (songs, isThereActiveSong) => {
      MusicPage.songs = songs;
      MusicPage.isThereActiveSong = isThereActiveSong;
    });
  }

  async ngOnInit() {
    MusicPage.songs = await this.apiProvider.getAllSongs();
    MusicPage.isThereActiveSong = await this.apiProvider.checkForActiveSong();
  }

  get songs() {
    return MusicPage.songs;
  }

  get isThereActiveSong() {
    return MusicPage.isThereActiveSong;
  }

  add() {
    this.modalCtrl.create(SongDetailsPage, {mode: "add"}).present();
  }

  songDetails(id: number) {
    this.modalCtrl.create(SongDetailsPage, {id: id}).present();
  }

  async play(id: number) {
    await this.apiProvider.playSong(id);
    await this.refresh();
  }

  async delete(id: number) {
    await this.apiProvider.deleteSong(id);
    await this.refresh();
  }

  async stop() {
    await this.apiProvider.stopMusic();
    await this.refresh();
  }

  async refresh() {
    await this.events.publish("musicRefresh", await this.apiProvider.getAllSongs(),
      await this.apiProvider.checkForActiveSong());
  }
}

@Component({
  templateUrl: 'song.html'
})
export class SongDetailsPage {
  // View mode
  mode: string;

  // Data for selected song
  id: number = 0;
  title: string = null;
  url: string = null;

  isDisabled: boolean = false;

  constructor(public viewCtrl: ViewController, public navParams: NavParams, private uiHelper: UIHelper,
              private apiProvider: ApiProvider, private events: Events) {
    this.mode = navParams.get('mode') == null ? 'show' : navParams.get('mode');
  }

  async ngOnInit() {
    if (this.mode == 'show') {
      let song = await this.apiProvider.getSongById(this.navParams.get('id'));
      this.id = song['id'];
      this.title = song['title'];
      this.url = song['url'];
    }
  }

  async save() {
    let songObject = {title: this.title, url: this.url};

    if (this.uiHelper.checkForEmptyField(songObject, ['url'])) {
      this.isDisabled = true;

      await this.apiProvider.addSong(songObject)
        .then(response => {
          this.uiHelper.successToast(response, "Song")
        }).catch((error: any) => {
        });
      this.dismiss();
    }
  }

  async delete(id: number) {
    await this.apiProvider.deleteSong(id);
    await this.dismiss();
  }

  async dismiss() {
    await this.viewCtrl.dismiss();
    await this.events.publish("musicRefresh",
      await this.apiProvider.getAllSongs(),
      await this.apiProvider.checkForActiveSong());
  }
}
