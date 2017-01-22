export class V1x1Configuration {
  configuration: Object;
  originalConfiguration: Object;

  constructor(configuration: Object) {
    this.configuration = configuration;
    this.originalConfiguration = JSON.parse(JSON.stringify(configuration));
  }

  dirty() {
    return JSON.stringify(this.configuration) !== JSON.stringify(this.originalConfiguration);
  }
}
