import {Component} from '@angular/core';
import {ApiProvider} from "../../../providers/api";
import {UIHelper} from "../../../utils/uihelper.util";

@Component({
  selector: 'page-weather',
  templateUrl: 'weather.html'
})
export class WeatherPage {
  city: string;
  units: string;

  constructor(private apiProvider: ApiProvider, private uiHelper: UIHelper) {
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
        this.uiHelper.successToast(response, "Settings")
      }).catch((error: any) => {
      });
  }
}
