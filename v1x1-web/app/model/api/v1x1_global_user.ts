import {V1x1User} from "./v1x1_user";

export class V1x1GlobalUser {
  id: string;
  users: V1x1User[];

  constructor(id: string, users: V1x1User[]) {
    this.id = id;
    this.users = users;
  }
}
