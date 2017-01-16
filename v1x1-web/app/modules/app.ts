import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppComponent} from '../components/app';
import {ConfigurationPageComponent} from '../components/configuration_page';
import {ConfigurationModuleComponent} from "../components/configuration_module";
import {ConfigurationScopeComponent} from "../components/configuration_scope";
import {ConfigurationFieldComponent} from "../components/configuration_field";
import {ConfigurationFieldValueComponent} from "../components/configuration_field_value";
import {ConfigurationFieldValueBooleanComponent} from "../components/configuration_field_value_boolean";
import {ConfigurationFieldValueBotNameComponent} from "../components/configuration_field_value_bot_name";
import {ConfigurationFieldValueComplexComponent} from "../components/configuration_field_value_complex";
import {ConfigurationFieldValueComplexListComponent} from "../components/configuration_field_value_complex_list";
import {ConfigurationFieldValueComplexStringMapComponent} from "../components/configuration_field_value_complex_string_map";
import {ConfigurationFieldValueCredentialComponent} from "../components/configuration_field_value_credential";
import {ConfigurationFieldValueIntegerComponent} from "../components/configuration_field_value_integer";
import {ConfigurationFieldValueMasterEnableComponent} from "../components/configuration_field_value_master_enable";
import {ConfigurationFieldValueStringComponent} from "../components/configuration_field_value_string";
import {ConfigurationFieldValueStringListComponent} from "../components/configuration_field_value_string_list";
import {ConfigurationFieldValueStringMapComponent} from "../components/configuration_field_value_string_map";
import {ConfigurationFieldValueTwitchOauthComponent} from "../components/configuration_field_value_twitch_oauth";

@NgModule({
  imports:      [ NgbModule.forRoot(), BrowserModule ],
  declarations: [
    AppComponent, ConfigurationPageComponent, ConfigurationModuleComponent,
    ConfigurationScopeComponent, ConfigurationFieldComponent, ConfigurationFieldValueComponent,
    ConfigurationFieldValueBooleanComponent, ConfigurationFieldValueBotNameComponent, ConfigurationFieldValueComplexComponent,
    ConfigurationFieldValueComplexListComponent, ConfigurationFieldValueComplexStringMapComponent, ConfigurationFieldValueCredentialComponent,
    ConfigurationFieldValueIntegerComponent, ConfigurationFieldValueMasterEnableComponent, ConfigurationFieldValueStringComponent,
    ConfigurationFieldValueStringListComponent, ConfigurationFieldValueStringMapComponent, ConfigurationFieldValueTwitchOauthComponent
  ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
