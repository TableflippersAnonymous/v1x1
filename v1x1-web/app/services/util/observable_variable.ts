import {Observable, Observer} from "rxjs";
import {publishReplay, refCount} from "rxjs/operators";

export class ObservableVariable<T> {
  private value: T;
  private observers: Observer<T>[] = [];
  private observable: Observable<T> = Observable.create(obs => {
    this.observers.push(obs);
    if(this.value !== undefined)
      obs.next(this.value);
    return () => {
      delete this.observers[this.observers.indexOf(obs)];
    };
  }).pipe(publishReplay(1), refCount());

  constructor(value: T) {
    this.value = value;
  }

  get(): Observable<T> {
    return this.observable;
  }

  getCurrent(): T {
    return this.value;
  }

  set(newValue: T) {
    this.value = newValue;
    this.observers.forEach(obs => obs.next(this.value));
  }
}
