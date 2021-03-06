import { Component, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { Router, Event, NavigationEnd, RouterEvent } from '@angular/router';
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
import { UserService } from 'src/app/admin/user-management/user';

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

	isResourceViewer: boolean = false;

	themes = ['default-theme', 'alt-theme'];
	username = "";

	permissions = {};
	authEntityList = AuthEntities;
	allEntities: string[] = [
		...AuthEntities,
		...Entities,
		...SchedulerEntities,
		...EmailEntities,
		"report"
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
		public userService: UserService,
	) {

		this.isSmallDevice$ = Global.isSmallDevice$;
		this.isMediumDevice$ = Global.isMediumDevice$;

		this.router.events.subscribe((event: Event) => {
			this.isCurrentRootRoute = (this.router.url == '/') ? true : false;
		});

		// this.selectedLanguage = localStorage.getItem('selectedLanguage');
		this.authenticationService.permissionsChange.subscribe(() => {
			this.setPermissions();
		});
		this.authenticationService.preferenceChange.subscribe(() => {
			this.setPreferences();
		});
		this.setPermissions();
		// this.changeTheme(this.themes[0]);
		this.setPreferences();

		this.router.events.subscribe((event: RouterEvent) => {
			if (event instanceof NavigationEnd) {
				if (event.url.indexOf('resourceView') > -1) {
					this.isResourceViewer = true;
				} else {
					this.isResourceViewer = false;
				}
			}
		});
	}

	setPreferences() {
		let theme = localStorage.getItem('theme');
		let language = localStorage.getItem('language');

		if (theme) {
			this.changeTheme(theme, false);
		}
		else {
			this.changeTheme(this.themes[0], false);
		}
		if (language) {
			this.selectedLanguage = language;
			this.translate.use(language);
		}
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
		this.selectedLanguage = language;
		this.userService.updateLanguage(language).subscribe(data => {
			localStorage.setItem('language', language);
		});
	}

	setPermissions() {
		this.username = this.authenticationService.decodeToken().sub;
		this.allEntities.forEach(entity => {
			this.permissions[entity] = this.globalPermissionService.hasPermissionOnEntity(entity, "READ");
		});
		this.permissions['ENTITYHISTORY'] = this.globalPermissionService.hasPermission('ENTITYHISTORY');
		this.permissions['AUDITTRAIL'] = this.globalPermissionService.hasPermission('AUDITTRAIL');
		ProcessPermissions.forEach(perm => {
			this.permissions[perm] = this.globalPermissionService.hasPermission(perm);
		})
		this.permissions['showTools'] = false;
		// this.setEmailVisibility();
		// this.setSchedulerVisibility();
		// this.setReportsVisibility();
		this.setModulesVisibility();
		this.setAdminVisiblity();
	}

	modules = {
		email: EmailEntities,
		scheduler: SchedulerEntities,
		report: ["report"]
	}

	setModulesVisibility() {
		Object.keys(this.modules).forEach(module => {
			let modulePermission = `show${module[0].toUpperCase() + module.slice(1)}`
			this.permissions[modulePermission] = false;
			this.modules[module].forEach(entity => {
				if (this.permissions[entity]) {
					this.permissions[modulePermission] = true;
					this.permissions['showTools'] = true;
				}
			});
		});
	}

	setEmailVisibility() {
		this.permissions['showEmail'] = false;
		EmailEntities.forEach(entity => {
			if (this.permissions[entity]) {
				this.permissions['showEmail'] = true;
				this.permissions['showTools'] = true;
			}
		})
	}

	setSchedulerVisibility() {
		this.permissions['showScheduler'] = false;
		SchedulerEntities.forEach(entity => {
			if (this.permissions[entity]) {
				this.permissions['showScheduler'] = true;
				this.permissions['showTools'] = true;
			}
		})
	}

	setReportsVisibility() {
		this.permissions['showReport'] = false;
		if (this.permissions['report']) {
			this.permissions['showReport'] = true;
			this.permissions['showTools'] = true;
		}
	}

	setAdminVisiblity() {
		if (this.authenticationService.decodeToken()) {
			this.permissions['showAdministration'] = this.authenticationService.decodeToken().role != "owner" &&
				this.authenticationService.decodeToken().role != "vet";
		}
	}

	login() {
		this.router.navigate(['/login'], { queryParams: { returnUrl: 'dashboard' } });
	}

	logout() {
		this.authenticationService.logout();
		this.changeTheme(this.themes[0], false);
		this.router.navigate(['/']);
	}
	changeTheme(theme: any, updatePreference: boolean) {
		console.log("add css class");
		for (let i = 0; i < this.themes.length; i++) {
			if (document.body.className.match(this.themes[i])) {
				document.body.classList.remove(this.themes[i]);
			}
		}
		document.body.classList.add(theme);
		if (updatePreference) {
			this.userService.updateTheme(theme).subscribe(data => {
				console.log(data);
				localStorage.setItem("theme", theme);
			});
		}
	}

}