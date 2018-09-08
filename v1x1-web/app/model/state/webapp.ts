import {V1x1WebConfig} from "../api/v1x1_web_config";
import {Module} from "./module";
import {GlobalUser} from "./global_user";
import {Tenant} from "./tenant";
import {Observable} from "rxjs/internal/Observable";

export class WebApp {
  configuration: Observable<V1x1WebConfig>;
  modules: Observable<Map<string, Module>>;
  currentUser: Observable<GlobalUser>;
  tenants: Observable<Map<string, Tenant>>;
  currentTenant: Observable<Tenant>;
}
