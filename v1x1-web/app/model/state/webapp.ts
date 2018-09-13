import {V1x1WebConfig} from "../api/v1x1_web_config";
import {Module} from "./module";
import {GlobalUser} from "./global_user";
import {Tenant} from "./tenant";
import {ReplaySubject, Subject} from "rxjs";

export class WebApp {
  configuration: Subject<V1x1WebConfig> = new ReplaySubject(1);
  modules: Subject<Map<string, Module>> = new ReplaySubject(1);
  currentUser: Subject<GlobalUser> = new ReplaySubject(1);
  tenants: Subject<Map<string, Tenant>> = new ReplaySubject(1);
  currentTenant: Subject<Tenant> = new ReplaySubject(1);
}
