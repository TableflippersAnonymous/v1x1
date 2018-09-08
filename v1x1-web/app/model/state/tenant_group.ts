import {GlobalUser} from "./global_user";
import {Tenant} from "./tenant";

export class TenantGroup {
  tenant: Tenant;
  groupId: string;
  name: string;
  permissions: string[];
  users: GlobalUser[];

  constructor(tenant: Tenant, groupId: string, name: string, permissions: string[], users: GlobalUser[]) {
    this.tenant = tenant;
    this.groupId = groupId;
    this.name = name;
    this.permissions = permissions;
    this.users = users;
  }
}
