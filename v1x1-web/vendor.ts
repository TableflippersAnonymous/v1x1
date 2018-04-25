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
import 'hammerjs';
import './scss/vendor.scss';
// Font Awesome
import fontawesome from '@fortawesome/fontawesome';
import faTwitch from '@fortawesome/fontawesome-free-brands/faTwitch';
import faDiscord from '@fortawesome/fontawesome-free-brands/faDiscord';
import faSlack from '@fortawesome/fontawesome-free-brands/faSlack';
import faYoutube from '@fortawesome/fontawesome-free-brands/faYoutube';
import faUndo from '@fortawesome/fontawesome-pro-regular/faUndo';
import faSortDown from '@fortawesome/fontawesome-pro-regular/faSortDown';

fontawesome.library.add(faTwitch, faDiscord, faSlack, faYoutube);
fontawesome.library.add(faUndo, faSortDown);
fontawesome.dom.i2svg();
