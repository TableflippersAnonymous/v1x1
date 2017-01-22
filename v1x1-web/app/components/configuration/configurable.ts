import {EventEmitter, Output, Input} from "@angular/core";
export class ConfigurableComponent {
  public configurationValue: any;
  @Output() public configurationChange = new EventEmitter();
  @Input()
  public set configuration(val) {
    this.configurationValue = val;
    this.configurationChange.emit(this.configuration);
  }
  public get configuration() {
    return this.configurationValue;
  }
}
