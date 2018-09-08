import {User} from "./user";

export class GlobalUser {
  id: string;
  users: Map<string, User>;

  constructor(id: string, users: Map<string, User>) {
    this.id = id;
    this.users = users;
  }
}
