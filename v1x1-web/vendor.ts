import './polyfills';
import 'hammerjs';
import './scss/vendor.scss';
// Font Awesome
import fontawesome from '@fortawesome/fontawesome';
import {faDiscord, faSlack, faTwitch, faYoutube} from "@fortawesome/fontawesome-free-brands";
import {faSortDown, faUndo} from "@fortawesome/fontawesome-pro-regular";

fontawesome.library.add(faTwitch, faDiscord, faSlack, faYoutube);
fontawesome.library.add(faUndo, faSortDown);
fontawesome.dom.i2svg(undefined);
