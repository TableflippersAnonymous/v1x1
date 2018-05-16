import {Injectable} from "@angular/core";
import {V1x1Api} from "./api";
import {V1x1Module} from "../model/v1x1_module";
import {Observable} from "rxjs";
import {V1x1PermissionDefinition} from "../model/v1x1_permission_definition";
import {publishReplay, refCount} from "rxjs/operators";

@Injectable()
export class V1x1ApiCache {
  private modules: Observable<V1x1Module[]>;
  private permissions: Observable<V1x1PermissionDefinition[]>;

  constructor(private api: V1x1Api) {}

  preload() {
    this.getModules().subscribe();
    this.getPermissions().subscribe();
  }

  getModules(): Observable<V1x1Module[]> {
    if(!this.modules)
      this.modules = this.api.getModules().pipe(publishReplay(1), refCount());
    return this.modules;
  }

  getPermissions(): Observable<V1x1PermissionDefinition[]> {
    if(!this.permissions)
      this.permissions = this.api.getPermissionDefinitions().pipe(publishReplay(1), refCount());
    return this.permissions;
  }
}
