import { NgModule } from '@angular/core';

import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { UpdateProfileComponent } from './update-profile/update-profile.component';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { SharedModule } from 'src/app/shared/shared.module';
import { routingModule } from './account.routing';
import { FastCodeCoreModule } from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';
 
export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}

@NgModule({
  declarations: [
		ForgotPasswordComponent,
		UpdatePasswordComponent,
		ResetPasswordComponent,
		UpdateProfileComponent,
  ],
  exports: [
		ForgotPasswordComponent,
		UpdatePasswordComponent,
		ResetPasswordComponent,
		UpdateProfileComponent,
  ],
  imports: [
    routingModule,
    SharedModule,
    FastCodeCoreModule.forRoot({
      apiUrl: environment.apiUrl
    }),
    // TranslateModule.forChild({
    //   loader: {
    //     provide: TranslateLoader,
    //     useFactory: HttpLoaderFactory,
    //     deps: [HttpClient]
    //   }
    // })
  ]
})
export class AccountModule { }
