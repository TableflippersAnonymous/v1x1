import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppComponent} from '../components/app';
import {ConfigurationPageComponent} from '../components/configuration_page';
import {ConfigurationModuleComponent} from "../components/configuration_module";
import {ConfigurationScopeComponent} from "../components/configuration_scope";
import {ConfigurationFieldComponent} from "../components/configuration_field";

@NgModule({
  imports:      [ NgbModule.forRoot(), BrowserModule ],
  declarations: [ AppComponent, ConfigurationPageComponent, ConfigurationModuleComponent, ConfigurationScopeComponent, ConfigurationFieldComponent ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
