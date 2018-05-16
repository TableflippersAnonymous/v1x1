import './polyfills';
// RxJS
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/share';
import 'rxjs/add/operator/publishReplay';
import 'rxjs/add/operator/mergeAll';
import 'rxjs/add/operator/first';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/find';
import 'rxjs/add/operator/retryWhen';
import 'rxjs/add/operator/delay';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/observable/of';
import 'rxjs/add/observable/zip';
import 'hammerjs';
import './scss/vendor.scss';
// Font Awesome
import fontawesome from '@fortawesome/fontawesome';
import {faDiscord, faSlack, faTwitch, faYoutube} from "@fortawesome/fontawesome-free-brands";
import {faSortDown, faUndo} from "@fortawesome/fontawesome-pro-regular";

fontawesome.library.add(faTwitch, faDiscord, faSlack, faYoutube);
fontawesome.library.add(faUndo, faSortDown);
fontawesome.dom.i2svg(undefined);
