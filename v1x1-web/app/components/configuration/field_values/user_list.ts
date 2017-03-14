import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
import {V1x1Api} from "../../../services/api";
@Component({
  selector: 'configuration-field-value-user-list',
  template: `
    <div *ngFor="let elem of configuration; let i = index" style="padding-left: 10px; margin-bottom: 1rem;" class="config-group" [class.config-group-dirty]="configIdxDirty(i)">
      <div class="input-group">
        <user-formatter [globalUser]="configuration[i]"></user-formatter>
        <button class="btn btn-danger btn-sm" style="margin-left: 1rem;" (click)="delIdx(i);">&times;</button>
      </div>
    </div>
    <div>
      <div class="input-group">
        <div class="input-group-addon" style="padding: .25rem .75rem;">
          <select class="form-control" style="font-family: 'FontAwesome', Arial; width: auto; height: auto; padding: .25rem .75rem;" [(ngModel)]="newPlatform">
            <option value="TWITCH">&#xf1e8;/</option>
          </select>
        </div>
        <input type="text" class="form-control" placeholder="Username" [(ngModel)]="newUsername">
        <span class="input-group-btn">
          <button class="btn btn-warning" (click)="addList()">+</button>
        </span>
      </div>
    </div>
`
})
export class ConfigurationFieldValueUserListComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  newUsername: string;
  newPlatform: string = "TWITCH";

  constructor(private api: V1x1Api) {
    super();
  }

  public addList() {
    if(!(this.configuration instanceof Array))
      this.configuration = [];
    this.api.getGlobalUserByPlatformAndUsername(this.newPlatform, this.newUsername).subscribe(
      globalUser => { this.configuration.push(globalUser); this.configChanged(); }
    );
  }

  public delIdx(index: number) {
    this.configuration.splice(index, 1);
    this.configChanged();
  }

  configDirty(): boolean {
    let idsToAdd = {};
    let idsToRemove = {};
    for(let idx in this.configuration) {
      idsToAdd[this.configuration[idx].id] = true;
    }
    for(let idx in this.originalConfiguration) {
      idsToRemove[this.originalConfiguration[idx].id] = true;
      delete idsToAdd[this.originalConfiguration[idx].id];
    }
    for(let idx in this.configuration) {
      delete idsToRemove[this.configuration[idx].id];
    }
    for(let id in idsToAdd) {
      return true;
    }
    for(let id in idsToRemove) {
      return true;
    }
    return false;
  }

  configIdxDirty(idx: number): boolean {
    for(let idx2 in this.originalConfiguration)
      if(this.originalConfiguration[idx2].id === this.configuration[idx].id)
        return false;
    return true;
  }
}
