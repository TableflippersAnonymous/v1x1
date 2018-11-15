import {V1x1WebConfig} from "./v1x1_web_config";
import {V1x1GlobalUser} from "./v1x1_global_user";
import {ApiModule} from "./api_module";
import {ApiSyncTenant} from "./api_sync_tenant";

export class ApiWebApp {
  configuration: V1x1WebConfig = undefined;
  modules: {[moduleName: string]: ApiModule} = undefined;
  tenants: {[tenantId: string]: ApiSyncTenant} = undefined;
  current_user: V1x1GlobalUser = undefined;
}
