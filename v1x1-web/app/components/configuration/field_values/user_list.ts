import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
import {V1x1Api} from "../../../services/api";

@Component({
  selector: 'configuration-field-value-user-list',
  template: `
    <div *ngFor="let elem of configuration; let i = index" style="padding-left: 10px; margin-bottom: 1rem;" class="config-group" [class.config-group-dirty]="configIdxDirty(i)">
      <div class="input-group">
        <span><user-formatter [globalUser]="configuration[i]"></user-formatter></span>
        <span><button mat-raised-button color="accent" style="margin-left: 1rem;" (click)="delIdx(i);">&times;</button></span>
      </div>
    </div>
    <div>
      <div class="input-group">
        <span style="padding: .25rem .75rem;">
          <mat-menu #appMenu="matMenu">
            <button mat-menu-item (click)="newPlatform = 'TWITCH';">
              <platform-formatter platform="TWITCH">Twitch</platform-formatter>
            </button>
            <button mat-menu-item (click)="newPlatform = 'DISCORD';">
              <platform-formatter platform="DISCORD">Discord</platform-formatter>
            </button>
          </mat-menu>
          <button mat-button color="accent" [matMenuTriggerFor]="appMenu">
            <platform-formatter [platform]="newPlatform"><i class="far fa-sort-down"></i></platform-formatter>
          </button>
        </span>
        <mat-form-field>
            <input matInput type="text" placeholder="Username" [(ngModel)]="newUsername">
        </mat-form-field>
        <span class="input-group-append">
          <button mat-button color="accent" (click)="addList()">+</button>
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
      globalUser => {
        this.newUsername = "";
        this.configuration.push(globalUser);
        this.configChanged();
      }
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
