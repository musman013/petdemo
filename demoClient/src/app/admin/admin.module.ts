import { NgModule } from '@angular/core';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { EntityHistoryComponent } from "./entity-history/entity-history.component";
import { ApiHistoryComponent } from "./api-history/api-history.component";
import { ApiHistoryDetailsComponent } from './api-history/api-history-details/api-history-details.component';

import { RolepermissionListComponent, RolepermissionDetailsComponent, RolepermissionNewComponent } from './user-management/rolepermission/index';
import { RoleListComponent, RoleDetailsComponent, RoleNewComponent } from './user-management/role/index';
import { UserpermissionListComponent, UserpermissionDetailsComponent, UserpermissionNewComponent } from './user-management/userpermission/index';
import { PermissionListComponent, PermissionDetailsComponent, PermissionNewComponent } from './user-management/permission/index';
import { UserroleListComponent, UserroleDetailsComponent, UserroleNewComponent } from './user-management/userrole/index';
import { UserListComponent, UserDetailsComponent, UserNewComponent } from './user-management/user/index';

import { routingModule } from './admin.routing';
import { SharedModule } from 'src/app/shared/shared.module'
import { FastCodeCoreModule } from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}

const entities = [
		RolepermissionListComponent,
		RolepermissionDetailsComponent,
		RolepermissionNewComponent,
		RoleListComponent,
		RoleDetailsComponent,
		RoleNewComponent,
		UserpermissionListComponent,
		UserpermissionDetailsComponent,
		UserpermissionNewComponent,
		PermissionListComponent,
		PermissionDetailsComponent,
		PermissionNewComponent,
		UserroleListComponent,
		UserroleDetailsComponent,
		UserroleNewComponent,
		UserListComponent,
		UserDetailsComponent,
		UserNewComponent,
		EntityHistoryComponent,
		ApiHistoryComponent,
    ApiHistoryDetailsComponent,
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
  	routingModule,
    SharedModule,
    FastCodeCoreModule.forRoot({
      apiUrl: environment.apiUrl
    }),
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),

  ]
})
export class AdminModule {
}
