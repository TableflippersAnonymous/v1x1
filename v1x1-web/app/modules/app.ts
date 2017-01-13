import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppComponent} from '../components/app';
import {ConfigComponent} from '../components/config';

@NgModule({
  imports:      [ NgbModule.forRoot(), BrowserModule ],
  declarations: [ AppComponent, ConfigComponent ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
