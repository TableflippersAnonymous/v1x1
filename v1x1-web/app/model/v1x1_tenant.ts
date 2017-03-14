import {V1x1Channel} from "./v1x1_channel";
export class V1x1Tenant {
  id: string;
  channels: V1x1Channel[];

  constructor(id: string, channels: V1x1Channel[]) {
    this.id = id;
    this.channels = channels;
  }
}
