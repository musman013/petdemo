import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { HttpClient } from "@angular/common/http";
import { FastCodeCoreTranslateUiService } from 'projects/fast-code-core/src/public_api';
import { SchedulerTranslateUiService } from 'projects/scheduler/src/public_api';
import { EmailBuilderTranslateUiService } from 'projects/ip-email-builder/src/public_api';
import { UpgradeModule } from "@angular/upgrade/static";
import { TaskAppTranslateUiService } from 'projects/task-app/src/public_api';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
  title = 'fcclient';

  constructor(
    private translate: TranslateService,
    private http: HttpClient,
    private schedulerTranslateUiService: SchedulerTranslateUiService,
    private emailBuilderTranslateUiService: EmailBuilderTranslateUiService,
    private fastCodeCoreTranslateUiService: FastCodeCoreTranslateUiService,
    private upgrade: UpgradeModule,
    private taskAppTranslateUiService: TaskAppTranslateUiService,
  ) {
    let languages = ["en", "fr"];
    let defaultLang = languages[0];
    translate.addLangs(languages);
    translate.setDefaultLang(defaultLang);

    let browserLang = translate.getBrowserLang();
    let lang = "";

    let selectedLanguage = localStorage.getItem('selectedLanguage');
    if (selectedLanguage && languages.includes(selectedLanguage)) {
      lang = selectedLanguage;
    }
    else {
      lang = languages.includes(browserLang) ? browserLang : defaultLang;
      localStorage.setItem('selectedLanguage', lang);
    }

    translate.use(lang).subscribe(() => {
      defaultLang = this.translate.defaultLang;
      if (defaultLang != lang) {
        this.initializeLibrariesTranslations(defaultLang);
      }
      this.initializeLibrariesTranslations(lang);
    });

  }

  initializeLibrariesTranslations(lang: string) {
    this.fastCodeCoreTranslateUiService.init(lang);
    this.schedulerTranslateUiService.init(lang);
    this.emailBuilderTranslateUiService.init(lang);
    this.taskAppTranslateUiService.init(lang);
  }

  ngOnInit(){
    this.upgrade.bootstrap(document.body, ['flowableAdminApp']);
  }
  
  getFontFamily() {
		this.http.get<any>('https://www.googleapis.com/webfonts/v1/webfonts?key=AIzaSyA2NeyaU_iGuS4IDFGZaLOhMwBFgPGYzQs')
			.subscribe(
				res => {
					console.log("res", res);
					let array = res.items;
					let font_string: string = '';
					for (let i = 0; i < array.length; i++) {
						//this.appendLink(array[i].family);
						if (font_string == '') {
							font_string = `${array[i].family}`;
						}
						else {
							font_string = `${font_string}|${array[i].family}`;
						}
					}
					this.appendLink(font_string);
				},
				err => {
					console.log("err", err);
				}
			)
	}
	appendLink(family) {
		var headID = document.getElementsByTagName('head')[0];
		var link = document.createElement('link');
		link.type = 'text/css';
		link.rel = 'stylesheet';
		headID.appendChild(link);
		link.href = `https://fonts.googleapis.com/css?family=${family}:300,400,500`;
	};

}
