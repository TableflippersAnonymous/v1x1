import {GlobalUser} from "./global_user";

export class User {
  globalUser: GlobalUser;
  platform: string;
  id: string;
  displayName: string;

  constructor(globalUser: GlobalUser, platform: string, id: string, displayName: string) {
    this.globalUser = globalUser;
    this.platform = platform;
    this.id = id;
    this.displayName = displayName;
  }
}
