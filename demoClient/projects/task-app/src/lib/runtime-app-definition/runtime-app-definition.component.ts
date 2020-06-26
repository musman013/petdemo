import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

import { RuntimeAppDefinitionService } from './runtime-app-definition.service';


@Component({
  selector: 'app-runtime-app-definition',
  templateUrl: './runtime-app-definition.component.html',
  styleUrls: ['./runtime-app-definition.component.scss']
})
export class RuntimeAppDefinitionComponent implements OnInit {

  url = "";
  apps: any[] = [];
  defaultApps: any[] = [];
  customApps: any[] = [];
  constructor(
    private runtimeAppDefinitionService: RuntimeAppDefinitionService,
    private router: Router
  ) { }

  ngOnInit() {
    this.url = this.runtimeAppDefinitionService.url;
    this.runtimeAppDefinitionService.getApplications().subscribe((response) => {
      let result = this.transformAppsResponse(response);
      this.apps = result.defaultApps.concat(result.customApps);
    })
  }

  transformAppsResponse(response) {
    response.data.forEach(app => {

      if (app.defaultAppId !== undefined && app.defaultAppId !== null) {

        if (app.defaultAppId === 'tasks') {

          this.defaultApps.push(
            {
              id: 'tasks',
              titleKey: 'APP.TASKS.TITLE',
              descriptionKey: 'APP.TASKS.DESCRIPTION',
              defaultAppId: app.defaultAppId,
              theme: 'theme-2',
              icon: 'icon icon-clock',
              fixedBaseUrl: this.url + '/workflow/' + '/#/',
              fixedUrl: this.url + '/workflow/',
              pages: ['tasks', 'processes']
            });
        }

      } else {

        // Custom app
        app.icon = 'glyphicon ' + app.icon;
        app.fixedBaseUrl = this.url + '/workflow/#/apps/' + app.appDefinitionKey + '/';
        app.fixedUrl = app.fixedBaseUrl + 'tasks';
        app.pages = ['tasks', 'processes'];
        app.deletable = true;
        this.customApps.push(app);
      }

    });

    return {
      defaultApps: this.defaultApps,
      customApps: this.customApps
    };
  };

  selectApp(app) {
    let appDefinitionKey = app.appDefinitionKey ? app.appDefinitionKey : "";
    this.runtimeAppDefinitionService.appDefinitionKey = appDefinitionKey;
    this.router.navigateByUrl('app/tasks/' + appDefinitionKey);
  }
}
