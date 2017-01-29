import {V1x1Module} from "../model/v1x1_module";
import {Injectable} from "@angular/core";
import {V1x1ConfigurationDefinitionSet} from "../model/v1x1_configuration_definition_set";
import {V1x1ConfigurationDefinition} from "../model/v1x1_configuration_definition";
import {Permission, V1x1ConfigurationDefinitionField, ConfigType} from "../model/v1x1_configuration_definition_field";
import {Http} from "@angular/http";
import {Observable} from "rxjs";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {V1x1List} from "../model/v1x1_list";
import {JsonConvert} from "json2typescript";

@Injectable()
export class V1x1Api {
  private v1x1ApiBase = '/api/v1';

  constructor(private http: Http) {}

  getConfigurationDefinitionList(type: string): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + '/platform/config-definitions/' + type).map((r) => JsonConvert.deserializeObject(r.json(), V1x1List)).map((l: V1x1List<string>) => l.entries);
  }

  getModuleList(): Observable<string[]> {
    return Observable.forkJoin([
      this.getConfigurationDefinitionList("global"),
      this.getConfigurationDefinitionList("tenant"),
      this.getConfigurationDefinitionList("channel")
    ]).map(r => [].concat.apply([], r).filter((v, idx, self) => self.indexOf(v) === idx));
  }

  getConfigurationDefinition(type: string, moduleName: string): Observable<V1x1ConfigurationDefinition> {
    return this.http.get(this.v1x1ApiBase + '/platform/config-definitions/' + type + '/' + moduleName).map((r) => JsonConvert.deserializeObject(r.json(), V1x1ConfigurationDefinition)).catch((err, caught) => Observable.of(null));
  }

  getConfigurationDefinitionSet(moduleName: string): Observable<V1x1ConfigurationDefinitionSet> {
    return Observable.forkJoin([
      this.getConfigurationDefinition("global", moduleName),
      this.getConfigurationDefinition("tenant", moduleName),
      this.getConfigurationDefinition("channel", moduleName)
    ]).map(r => new V1x1ConfigurationDefinitionSet(r[0], r[1], r[2]));
  }

  getModule(moduleName: string): Observable<V1x1Module> {
    return Observable.zip(
      this.getConfigurationDefinitionSet(moduleName),
      (configDefinitionSet: V1x1ConfigurationDefinitionSet) => new V1x1Module(moduleName, configDefinitionSet.tenant.display_name, configDefinitionSet.tenant.description, configDefinitionSet)
    );
  }

  getModules(): Observable<V1x1Module[]> {
    return this.getModuleList().map(
      modules =>
        Observable.forkJoin(modules.map(
          moduleName => this.getModule(moduleName)
        ))
    ).mergeAll();
  }
}
