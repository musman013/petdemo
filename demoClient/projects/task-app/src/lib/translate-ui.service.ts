import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { en } from "../localization/en";

@Injectable({
    providedIn: 'root'
})
export class TaskAppTranslateUiService {
    private availableLanguages = { en };

    constructor(private translateService: TranslateService) {
    }

    public init(language: string = null): any {
        if (language) {
            //initialize one specific language
            this.translateService.setTranslation(language, this.availableLanguages[language], true);
        } else {
            //initialize all
            Object.keys(this.availableLanguages).forEach((language) => {
                this.translateService.setTranslation(language, this.availableLanguages[language], true);
            });
        }
    }
}