import {Component} from '@angular/core';
import {ApiProvider} from "../../../providers/api";
import {UIUtil} from "../../../utils/ui.util";

@Component({
  selector: 'page-weather',
  templateUrl: 'weather.html'
})
export class WeatherPage {
  city: string;
  units: string;

  constructor(public apiProvider: ApiProvider, public uiUtil: UIUtil) {
  }

  async ngOnInit() {
    let settings = await this.apiProvider.getWeatherSettings();
    if (settings) {
      this.city = settings["city"];
      this.units = settings["units"];
    }
  }

  async save() {
    await this.apiProvider.saveWeatherSettings({city: this.city, units: this.units})
      .then(response => {
        this.uiUtil.successToast(response, "Settings")
      }).catch((error: any) => {
      });
  }
}
