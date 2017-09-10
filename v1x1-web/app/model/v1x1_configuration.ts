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
}
