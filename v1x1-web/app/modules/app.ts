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
import {DashboardPageComponent} from "../components/dashboard/page";
import {HelpPageComponent} from "../components/help/page";
import {LogsPageComponent} from "../components/logs/page";
import {PermissionsPageComponent} from "../components/permissions/page";
import {HttpModule} from "@angular/http";
import {V1x1ApiCache} from "../services/api_cache";
import {WelcomePageComponent} from "../components/welcome/page";
import {UserDropdownNavComponent} from "../components/nav/user_dropdown";
import {TenantDropdownNavComponent} from "../components/nav/tenant_dropdown";
import {PermissionsGroupsComponent} from "../components/permissions/groups";
import {PermissionsGroupComponent} from "../components/permissions/group";
import {TenantFormatterComponent} from "../components/util/tenant_formatter";
import {UserFormatterComponent} from "../components/util/user_formatter";
import {ConfigurationFieldValueUserListComponent} from "../components/configuration/field_values/user_list";
import {RouterModule, Routes} from "@angular/router";
import {V1x1GlobalState} from "../services/global_state";
import {NavRouterComponent} from "../components/nav/nav_router";
import {UrlId} from "../services/url_id";
import {V1x1PubSub} from "../services/pubsub";
import {V1x1WebInfo} from "../services/web_info";
import {ConfigurationChannelGroupComponent} from "../components/configuration/channel_group";
import {PermissionsMappingComponent} from "../components/permissions/mapping";
import {PermissionsGroupMappingComponent} from "../components/permissions/group_mapping";
import {PlatformFormatterComponent} from "../components/util/platform_formatter";
import {UserPageComponent} from "../components/user/page";

/**
 * <welcome-page [loggedIn]="loggedIn"></welcome-page>
 <dashboard-page></dashboard-page>
 <configuration-page [activeTenant]="activeTenant"></configuration-page>
 <permissions-page [activeTenant]="activeTenant"></permissions-page>
 <logs-page></logs-page>
 <help-page></help-page>
 * @type {[{path: string}]}
 */
/*
 * / -> /welcome
 * /welcome -> WelcomePageComponent
 */
const routes: Routes = [
  {
    path: ':tenant_id',
    component: NavRouterComponent,
    children: [
      { path: 'welcome', component: WelcomePageComponent },
      { path: 'dashboard', component: DashboardPageComponent },
      {
        path: 'config',
        component: ConfigurationPageComponent,
        children: [
          {
            path: ':module_name/:scope',
            component: ConfigurationPageComponent
          }
        ]
      },
      {
        path: 'permissions',
        component: PermissionsPageComponent,
        children: [
          {
            path: 'groups',
            component: PermissionsGroupsComponent
          },
          {
            path: 'mapping',
            component: PermissionsMappingComponent
          }
        ]
      },
      { path: 'logs', component: LogsPageComponent },
      { path: 'user', component: UserPageComponent },
      { path: 'help', component: HelpPageComponent }
    ]
  },
  {
    path: '',
    redirectTo: '/' + UrlId.fromApi('00000000-0000-0000-0000-000000000000').toUrl() + '/welcome',
    pathMatch: 'full'
  }
];

@NgModule({
  imports:      [
    NgbModule.forRoot(),
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(routes, { useHash: true })
  ],
  declarations: [
    AppComponent, ConfigurationPageComponent, ConfigurationModuleComponent, ConfigurationChannelGroupComponent,
    ConfigurationScopeComponent, ConfigurationFieldComponent, ConfigurationFieldValueComponent,
    ConfigurationFieldValueBooleanComponent, ConfigurationFieldValueBotNameComponent, ConfigurationFieldValueComplexComponent,
    ConfigurationFieldValueComplexListComponent, ConfigurationFieldValueComplexStringMapComponent, ConfigurationFieldValueCredentialComponent,
    ConfigurationFieldValueIntegerComponent, ConfigurationFieldValueMasterEnableComponent, ConfigurationFieldValueStringComponent,
    ConfigurationFieldValueStringListComponent, ConfigurationFieldValueStringMapComponent, ConfigurationFieldValueTwitchOauthComponent,
    ConfigurationFieldValueUserListComponent,
    TopNavComponent, TopNavEntryComponent, UserDropdownNavComponent, TenantDropdownNavComponent, NavRouterComponent,
    DashboardPageComponent, HelpPageComponent, LogsPageComponent, UserPageComponent,
    PermissionsPageComponent, PermissionsGroupsComponent, PermissionsGroupComponent, PermissionsMappingComponent, PermissionsGroupMappingComponent,
    WelcomePageComponent,
    TenantFormatterComponent, UserFormatterComponent, PlatformFormatterComponent
  ],
  providers:    [ V1x1Api, V1x1ApiCache, V1x1GlobalState, V1x1PubSub, V1x1WebInfo ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
