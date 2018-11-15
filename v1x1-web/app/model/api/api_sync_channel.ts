import {ApiConfiguration} from "./api_configuration";

export class ApiSyncChannel {
  id: string = undefined;
  channel_group_id: string = undefined;
  display_name: string = undefined;
  module_configuration: {[moduleName: string]: ApiConfiguration} = undefined;
}
