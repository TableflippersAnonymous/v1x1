import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppComponent} from '../components/app';
import {ConfigurationPageComponent} from '../components/configuration_page';
import {ConfigurationModuleComponent} from "../components/configuration_module";

@NgModule({
  imports:      [ NgbModule.forRoot(), BrowserModule ],
  declarations: [ AppComponent, ConfigurationPageComponent, ConfigurationModuleComponent ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
