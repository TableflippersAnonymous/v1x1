import {EventEmitter, Output, Input} from "@angular/core";
export class ConfigurableComponent {
  public originalConfigurationValue: any;
  public configurationValue: any;
  @Output() public configurationChange = new EventEmitter();
  @Output() public configDirtyChange = new EventEmitter();

  public set configuration(val) {
    //console.log("Setting non-input " + JSON.stringify(val));
    if(val === undefined)
      this.configurationValue = undefined;
    else
      this.configurationValue = JSON.parse(JSON.stringify(val));
    this.configurationChange.emit(this.configuration);
  }
  public get configuration() {
    return this.configurationValue;
  }

  @Input('configuration')
  set inputConfiguration(val: any) {
    //console.log("Setting input " + JSON.stringify(val));
    if(val === undefined)
      this.configurationValue = undefined;
    else {
      this.configurationValue = JSON.parse(JSON.stringify(val));
      if(this.originalConfigurationValue === undefined)
        this.originalConfigurationValue = JSON.parse(JSON.stringify(val));
    }
  }

  @Input('originalConfiguration')
  set originalConfiguration(val: any) {
    if(val === undefined)
      this.originalConfigurationValue = undefined;
    else
      this.originalConfigurationValue = JSON.parse(JSON.stringify(val));
  }

  get originalConfiguration() {
    return this.originalConfigurationValue;
  }

  setConfigField(field: string, val: any) {
    if(this.configuration === undefined)
      this.configurationValue = {};
    this.configurationValue[field] = val;
    this.configChanged();
  }

  deleteConfigField(field: string) {
    if(this.configuration === undefined)
      this.configurationValue = {};
    delete this.configurationValue[field];
    this.configChanged();
  }

  configChanged() {
    this.configurationChange.emit(this.configuration);
    this.configDirtyChange.emit(this.configDirty());
  }

  acceptChanges() {
    this.originalConfigurationValue = JSON.parse(JSON.stringify(this.configuration));
    this.configDirtyChange.emit(this.configDirty());
  }

  abandonChanges() {
    this.configurationValue = JSON.parse(JSON.stringify(this.originalConfigurationValue));
    this.configChanged();
  }

  configDirty() {
    return JSON.stringify(this.configurationValue) !== JSON.stringify(this.originalConfigurationValue);
  }
}
