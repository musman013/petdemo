
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LayoutModule } from '@angular/cdk/layout';
import { CubejsClientModule } from '@cubejs-client/ngx';

import { UpgradeModule } from "@angular/upgrade/static";
import { UrlHandlingStrategy } from '@angular/router';
import { TaskAppModule } from 'projects/task-app/src/public_api';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/index';
import { SwaggerComponent } from 'src/app/swagger/swagger.component';
import { ErrorPageComponent } from './error-page/error-page.component';
/** core components and filters for authorization and authentication **/

import { AuthenticationService } from './core/authentication.service';
import { AuthGuard } from './core/auth-guard';
import { JwtInterceptor } from './core/jwt-interceptor';
import { JwtErrorInterceptor } from './core/jwt-error-interceptor';
import { GlobalPermissionService } from './core/global-permission.service';

import { LoginComponent } from './login/index';

/** end of core components and filters for authorization and authentication **/

import { IpEmailBuilderModule } from 'projects/ip-email-builder/src/public_api';
import { SchedulerModule } from 'projects/scheduler/src/public_api';

import { routingModule } from './app.routing';
import { FastCodeCoreModule } from 'projects/fast-code-core/src/public_api';

/** common components and filters **/

import { MainNavComponent } from './common/components/main-nav/main-nav.component';
import { BottomTabNavComponent } from './common/components/bottom-tab-nav/bottom-tab-nav.component';

/** end of common components and filters **/

import { environment } from '../environments/environment';
import { SharedModule } from './shared/shared.module';

import { PetsListComponent, PetsDetailsComponent, PetsNewComponent } from './pets/index';
import { VisitsListComponent, VisitsDetailsComponent, VisitsNewComponent } from './visits/index';
import { TypesListComponent, TypesDetailsComponent, TypesNewComponent } from './types/index';
import { SpecialtiesListComponent, SpecialtiesDetailsComponent, SpecialtiesNewComponent } from './specialties/index';
import { VetsListComponent, VetsDetailsComponent, VetsNewComponent } from './vets/index';
import { OwnersListComponent, OwnersDetailsComponent, OwnersNewComponent } from './owners/index';
import { VetSpecialtiesListComponent, VetSpecialtiesDetailsComponent, VetSpecialtiesNewComponent } from './vet-specialties/index';
import { InvoicesListComponent, InvoicesDetailsComponent, InvoicesNewComponent } from './invoices/index';
import { CompleteVisitComponent } from './visits/complete-visit/complete-visit.component';
import { ResourceViewComponent } from './reporting-module/pages/resourceView/resourceView.component';
import { ReportPasswordComponent } from './reporting-module/pages/myreports/report-password/report-password.component';
import { RegisterCompleteComponent } from 'src/app/account/register/register-complete/register-complete.component';

const cubejsOptions = {
	token:
		"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.K9PiJkjegbhnw4Ca5pPlkTmZihoOm42w8bja9Qs2qJg",
	options: {
		apiUrl: "http://localhost:4200/cubejs-api/v1"
	}
};
export function HttpLoaderFactory(httpClient: HttpClient) {
	return new TranslateHttpLoader(httpClient);
}

export class CustomHandlingStrategy implements UrlHandlingStrategy {
	shouldProcessUrl(url) {
		let urlStr = url.toString().split('/');
		return url.toString() == "/" || (urlStr.length > 1 && urlStr[1] != "flowable-admin")
	}
	extract(url) { return url; }
	merge(url, whole) { return url; }
}

@NgModule({
	declarations: [
		ErrorPageComponent,
		HomeComponent,
		DashboardComponent,
		SwaggerComponent,
		LoginComponent,
		AppComponent,
		MainNavComponent,
		BottomTabNavComponent,
		PetsListComponent,
		PetsDetailsComponent,
		PetsNewComponent,
		VisitsListComponent,
		VisitsDetailsComponent,
		VisitsNewComponent,
		TypesListComponent,
		TypesDetailsComponent,
		TypesNewComponent,
		SpecialtiesListComponent,
		SpecialtiesDetailsComponent,
		SpecialtiesNewComponent,
		VetsListComponent,
		VetsDetailsComponent,
		VetsNewComponent,
		OwnersListComponent,
		OwnersDetailsComponent,
		OwnersNewComponent,
		VetSpecialtiesListComponent,
		VetSpecialtiesDetailsComponent,
		VetSpecialtiesNewComponent,
		CompleteVisitComponent,
		InvoicesListComponent,
		InvoicesDetailsComponent,
		InvoicesNewComponent,
		ResourceViewComponent,
	],
	imports: [
		BrowserModule,
		routingModule,
		HttpClientModule,
		BrowserAnimationsModule,
		LayoutModule,
		SharedModule,
		CubejsClientModule.forRoot(cubejsOptions),
		IpEmailBuilderModule.forRoot({
			xApiKey: 't7HdQfZjGp6R96fOV4P8v18ggf6LLTQZ1puUI2tz',
			apiPath: environment.apiUrl
		}),
		SchedulerModule.forRoot({
			apiPath: environment.apiUrl
		}),
		FastCodeCoreModule.forRoot({
			apiUrl: environment.apiUrl
		}),
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: HttpLoaderFactory,
				deps: [HttpClient]
			},
			isolate: false
		}),
		UpgradeModule,
		TaskAppModule.forRoot({
			apiPath: environment.apiUrl // url where task backend app is running
		}),

	],
	providers: [
		AuthenticationService,
		GlobalPermissionService,
		{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
		{ provide: HTTP_INTERCEPTORS, useClass: JwtErrorInterceptor, multi: true },
		AuthGuard,
		{ provide: UrlHandlingStrategy, useClass: CustomHandlingStrategy },
	],
	bootstrap: [AppComponent],
	entryComponents: [
		ReportPasswordComponent
	]
})
export class AppModule {
}
