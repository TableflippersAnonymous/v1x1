import {Injectable} from "@angular/core";
import {V1x1Tenant} from "../model/v1x1_tenant";
import {ObservableVariable} from "./util/observable_variable";
@Injectable()
export class V1x1GlobalState {
  activeTenant: ObservableVariable<V1x1Tenant> = new ObservableVariable<V1x1Tenant>(undefined);
  tenants: ObservableVariable<V1x1Tenant[]> = new ObservableVariable<V1x1Tenant[]>([]);
  loggedIn: ObservableVariable<boolean> = new ObservableVariable<boolean>(false);
  authorization: ObservableVariable<string> = new ObservableVariable<string>(null);
}
