import { Component, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { Router, Event } from '@angular/router';
import { MatSidenav, MatSidenavContent } from '@angular/material';
import {
	Entities,
	AuthEntities,
	SchedulerEntities,
	EmailEntities,
	ProcessPermissions,
} from './entities';
import { AuthenticationService } from 'src/app/core/authentication.service';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';

import { FastCodeCoreTranslateUiService, Globals } from 'projects/fast-code-core/src/public_api';
import { SchedulerTranslateUiService } from 'projects/scheduler/src/public_api';
import { EmailBuilderTranslateUiService } from 'projects/ip-email-builder/src/public_api';
import { TaskAppTranslateUiService } from 'projects/task-app/src/public_api';

@Component({
	selector: 'app-main-nav',
	templateUrl: './main-nav.component.html',
	styleUrls: ['./main-nav.component.scss', './main-nav-mixin.component.scss']
})
export class MainNavComponent {
	@ViewChild("drawer", { static: false }) drawer: MatSidenav;
	@ViewChild("navContent", { static: false }) navContent: MatSidenavContent;

	appName: string = 'demo';
	selectedLanguage: string;
	entityList = Entities;

	hasTaskAppPermission: boolean = false;
	hasAdminAppPermission: boolean = false;

	isSmallDevice$: Observable<boolean>;
	isMediumDevice$: Observable<boolean>;
	isCurrentRootRoute: boolean = true;

	themes = ['default-theme', 'alt-theme'];

	permissions = {};
	authEntityList = AuthEntities;
	allEntities: string[] = [
		...AuthEntities,
		...Entities,
		...SchedulerEntities,
		...EmailEntities,
	];

	constructor(
		private router: Router,
		public translate: TranslateService,
		public Global: Globals,
		private fastCodeCoreTranslateUiService: FastCodeCoreTranslateUiService,
		private schedulerTranslateUiService: SchedulerTranslateUiService,
		private emailBuilderTranslateUiService: EmailBuilderTranslateUiService,
		private taskAppTranslateUiService: TaskAppTranslateUiService,
		public authenticationService: AuthenticationService,
		public globalPermissionService: GlobalPermissionService,
	) {

		this.isSmallDevice$ = Global.isSmallDevice$;
		this.isMediumDevice$ = Global.isMediumDevice$;

		this.router.events.subscribe((event: Event) => {
			this.isCurrentRootRoute = (this.router.url == '/') ? true : false;
		});

		this.selectedLanguage = localStorage.getItem('selectedLanguage');
		this.authenticationService.permissionsChange.subscribe(() => {
			this.setPermissions();
		});
		this.setPermissions();
		this.changeTheme(this.themes[0]);
	}

	switchLanguage(language: string) {
		if (this.translate.translations[language]) {
			this.translate.use(language);
		} else {
			this.translate.use(language).subscribe(() => {
				this.fastCodeCoreTranslateUiService.init(language);
				this.schedulerTranslateUiService.init(language);
				this.emailBuilderTranslateUiService.init(language);
				this.taskAppTranslateUiService.init(language);
			});
		}
		localStorage.setItem('selectedLanguage', language);
		this.selectedLanguage = language;
	}

	setPermissions() {
		this.allEntities.forEach(entity => {
			this.permissions[entity] = this.globalPermissionService.hasPermissionOnEntity(entity, "READ");
		});
		this.permissions['ENTITYHISTORY'] = this.globalPermissionService.hasPermission('ENTITYHISTORY');
		this.permissions['AUDITTRAIL'] = this.globalPermissionService.hasPermission('AUDITTRAIL');
		ProcessPermissions.forEach(perm => {
			this.permissions[perm] = this.globalPermissionService.hasPermission(perm);
		})
	}

	login() {
		this.router.navigate(['/login'], { queryParams: { returnUrl: 'dashboard' } });
	}

	logout() {
		this.authenticationService.logout();
		this.router.navigate(['/']);
	}
	changeTheme(theme: any) {
		console.log("add css class");
		for (let i = 0; i < this.themes.length; i++) {
			if (document.body.className.match(this.themes[i])) {
				document.body.classList.remove(this.themes[i]);
			}
		}
		document.body.classList.add(theme);
	}

}