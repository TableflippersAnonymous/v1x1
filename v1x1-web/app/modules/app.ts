import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppComponent} from '../components/app';
import {ConfigurationPageComponent} from '../components/configuration/page';
import {ConfigurationModuleComponent} from "../components/configuration/module";
import {ConfigurationScopeComponent} from "../components/configuration/scope";
import {ConfigurationFieldComponent} from "../components/configuration/field";
import {ConfigurationFieldValueComponent} from "../components/configuration/field_value";
import {ConfigurationFieldValueBooleanComponent} from "../components/configuration/field_values/boolean";
import {ConfigurationFieldValueBotNameComponent} from "../components/configuration/field_values/bot_name";
import {ConfigurationFieldValueComplexComponent} from "../components/configuration/field_values/complex";
import {ConfigurationFieldValueComplexListComponent} from "../components/configuration/field_values/complex_list";
import {ConfigurationFieldValueComplexStringMapComponent} from "../components/configuration/field_values/complex_string_map";
import {ConfigurationFieldValueCredentialComponent} from "../components/configuration/field_values/credential";
import {ConfigurationFieldValueIntegerComponent} from "../components/configuration/field_values/integer";
import {ConfigurationFieldValueMasterEnableComponent} from "../components/configuration/field_values/master_enable";
import {ConfigurationFieldValueStringComponent} from "../components/configuration/field_values/string";
import {ConfigurationFieldValueStringListComponent} from "../components/configuration/field_values/string_list";
import {ConfigurationFieldValueStringMapComponent} from "../components/configuration/field_values/string_map";
import {ConfigurationFieldValueTwitchOauthComponent} from "../components/configuration/field_values/twitch_oauth";
import {FormsModule} from "@angular/forms";
import {V1x1Api} from "../services/api";
import {TopNavComponent} from "../components/nav/top_nav";
import {TopNavEntryComponent} from "../components/nav/top_nav_entry";
import {TopNavEntryContentComponent} from "../components/nav/top_nav_entry_content";
import {DashboardPageComponent} from "../components/dashboard/page";
import {HelpPageComponent} from "../components/help/page";
import {LogsPageComponent} from "../components/logs/page";
import {PermissionsPageComponent} from "../components/permissions/page";
import {HttpModule} from "@angular/http";
import {V1x1ApiCache} from "../services/api_cache";

@NgModule({
  imports:      [ NgbModule.forRoot(), BrowserModule, FormsModule, HttpModule ],
  declarations: [
    AppComponent, ConfigurationPageComponent, ConfigurationModuleComponent,
    ConfigurationScopeComponent, ConfigurationFieldComponent, ConfigurationFieldValueComponent,
    ConfigurationFieldValueBooleanComponent, ConfigurationFieldValueBotNameComponent, ConfigurationFieldValueComplexComponent,
    ConfigurationFieldValueComplexListComponent, ConfigurationFieldValueComplexStringMapComponent, ConfigurationFieldValueCredentialComponent,
    ConfigurationFieldValueIntegerComponent, ConfigurationFieldValueMasterEnableComponent, ConfigurationFieldValueStringComponent,
    ConfigurationFieldValueStringListComponent, ConfigurationFieldValueStringMapComponent, ConfigurationFieldValueTwitchOauthComponent,
    TopNavComponent, TopNavEntryComponent, TopNavEntryContentComponent, DashboardPageComponent, HelpPageComponent, LogsPageComponent,
    PermissionsPageComponent
  ],
  providers:    [ V1x1Api, V1x1ApiCache ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
