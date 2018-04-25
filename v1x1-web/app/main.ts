import '../vendor';
import {AppModule} from './modules/app';
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import '../scss/main.scss';
import {enableProdMode} from "@angular/core";

if (process.env.ENV === 'production') {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule);
