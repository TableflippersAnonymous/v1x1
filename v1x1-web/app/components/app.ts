import {Component} from '@angular/core';

@Component({
  selector: 'v1x1-app',
  template: `<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle Navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <a class="navbar-brand" href="#">v1x1</a>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item active">
          <a class="nav-link" href="#">Configuration</a>
        </li>
      </ul>
    </div>
  </nav>
  <config></config>`,
})
export class AppComponent { name = 'v1x1'; }
