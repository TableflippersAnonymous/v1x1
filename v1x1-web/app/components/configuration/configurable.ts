import {EventEmitter, Input, Output} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1ChannelGroup} from "../../model/v1x1_channel_group";
import {V1x1Channel} from "../../model/v1x1_channel";

export class ConfigurableComponent {
  public originalConfigurationValue: any;
  public configurationValue: any;
  @Input() public activeTenant: V1x1Tenant;
  @Input() public activeChannelGroup: V1x1ChannelGroup;
  @Input() public activeChannel: V1x1Channel;
  @Output() public configurationChange = new EventEmitter();
  @Output() public configDirtyChange = new EventEmitter();
  @Output() public originalConfigurationChange = new EventEmitter();
  originalConfigProvided: boolean = false;

  public set configuration(val) {
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
    if(val === undefined)
      this.configurationValue = undefined;
    else {
      this.configurationValue = JSON.parse(JSON.stringify(val));
      if(this.originalConfigurationValue === undefined && !this.originalConfigProvided)
        this.originalConfigurationValue = JSON.parse(JSON.stringify(val));
    }
  }

  @Input('originalConfiguration')
  set originalConfiguration(val: any) {
    this.originalConfigProvided = true;
    if(val === undefined)
      this.originalConfigurationValue = undefined;
    else
      this.originalConfigurationValue = JSON.parse(JSON.stringify(val));
  }

  get originalConfiguration() {
    return this.originalConfigurationValue || {};
  }

  setConfigField(field: string, val: any) {
    if(this.configuration === undefined)
      this.configurationValue = {};
    this.configurationValue[field] = val;
    this.configChanged();
  }

  renameConfigField(oldField: string, newField: string) {
    if(this.configuration === undefined)
      this.configurationValue = {};
    this.configurationValue[newField] = this.configurationValue[oldField];
    delete this.configurationValue[oldField];
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

  configFieldDirty(field: string) {
    return JSON.stringify((this.configurationValue || {})[field]) !== JSON.stringify((this.originalConfigurationValue || {})[field]);
  }

  configIdxDirty(idx: number) {
    return JSON.stringify((this.configurationValue || [])[idx]) !== JSON.stringify((this.originalConfigurationValue || [])[idx]);
  }
}
