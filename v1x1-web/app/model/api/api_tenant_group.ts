import {V1x1GlobalUser} from "./v1x1_global_user";

export class ApiTenantGroup {
  name: string = undefined;
  permissions: string[] = undefined;
  users: V1x1GlobalUser[] = undefined;
  tenant_id: string = undefined;
  group_id: string = undefined;
}
