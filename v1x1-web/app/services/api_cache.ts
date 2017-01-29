import {Injectable} from "@angular/core";
import {V1x1Api} from "./api";
import {V1x1Module} from "../model/v1x1_module";
import {Observable} from "rxjs";
@Injectable()
export class V1x1ApiCache {
  private modules: Observable<V1x1Module[]>;

  constructor(private api: V1x1Api) {}

  preload() {
    this.getModules().subscribe();
  }

  getModules(): Observable<V1x1Module[]> {
    if(!this.modules)
      this.modules = this.api.getModules().publishReplay(1).refCount();
    return this.modules;
  }
}
