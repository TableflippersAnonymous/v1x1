import {V1x1ChannelConfigurationWrapper} from "./v1x1_channel_configuration_wrapper";

export class V1x1Configuration {
  enabled: boolean;
  originalEnabled: boolean;
  configuration: Object;
  originalConfiguration: Object;

  constructor(enabled: boolean, configuration: Object) {
    this.enabled = enabled;
    this.originalEnabled = enabled;
    this.configuration = configuration;
    this.originalConfiguration = JSON.parse(JSON.stringify(configuration));
  }

  dirty() {
    return JSON.stringify(this.configuration) !== JSON.stringify(this.originalConfiguration) || this.enabled !== this.originalEnabled;
  }

  setOriginal(original: V1x1Configuration) {
    if(original === null || original === undefined)
      return;
    if(this.originalEnabled === this.enabled)
      this.enabled = original.enabled;
    this.originalEnabled = original.enabled;
    if(JSON.stringify(this.configuration) === JSON.stringify(this.originalConfiguration))
      this.configuration = original.configuration;
    this.originalConfiguration = original.configuration;
  }

  setOriginalWrapper(originalWrapper: V1x1ChannelConfigurationWrapper) {
    if(originalWrapper === null || originalWrapper === undefined)
      return;
    this.setOriginal(originalWrapper.config);
  }
}
