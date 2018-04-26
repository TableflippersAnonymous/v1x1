import '../vendor';
import {AppModule} from './modules/app';
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import '../scss/main.scss';

const fontawesome = require('@fortawesome/fontawesome').default;
const brands = require('@fortawesome/fontawesome-free-brands').default;
const regular = require('@fortawesome/fontawesome-pro-regular').default;

platformBrowserDynamic().bootstrapModule(AppModule);

fontawesome.library.add(brands);
fontawesome.library.add(regular);
fontawesome.dom.i2svg();
