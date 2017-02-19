import {V1x1Group} from "./v1x1_group";
import {V1x1GlobalUser} from "./v1x1_global_user";
export class V1x1GroupMembership {
  group: V1x1Group;
  members: V1x1GlobalUser[];

  constructor(group: V1x1Group, members: V1x1GlobalUser[]) {
    this.group = group;
    this.members = members;
  }
}
