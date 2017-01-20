import {V1x1Configuration} from "./v1x1_configuration";
export class V1x1ConfigurationSet {
  global: V1x1Configuration;
  tenant: V1x1Configuration;
  channel: V1x1Configuration;


  constructor(global: V1x1Configuration, tenant: V1x1Configuration, channel: V1x1Configuration) {
    this.global = global;
    this.tenant = tenant;
    this.channel = channel;
  }
}
