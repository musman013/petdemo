
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { CanDeactivateGuard } from 'projects/fast-code-core/src/public_api';
import { AuthGuard } from 'src/app/core/auth-guard';

import { EntityHistoryComponent } from "./entity-history/entity-history.component";
import { ApiHistoryComponent } from './api-history/api-history.component';
import { ApiHistoryDetailsComponent } from './api-history/api-history-details/api-history-details.component';

import { RolepermissionListComponent, RolepermissionDetailsComponent, RolepermissionNewComponent } from './user-management/rolepermission/index';
import { RoleListComponent, RoleDetailsComponent, RoleNewComponent } from './user-management/role/index';
import { UserpermissionListComponent, UserpermissionDetailsComponent, UserpermissionNewComponent } from './user-management/userpermission/index';
import { PermissionListComponent, PermissionDetailsComponent, PermissionNewComponent } from './user-management/permission/index';
import { UserroleListComponent, UserroleDetailsComponent, UserroleNewComponent } from './user-management/userrole/index';
import { UserListComponent, UserDetailsComponent, UserNewComponent } from './user-management/user/index';

const routes: Routes = [
	{ path: 'rolepermission', component: RolepermissionListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ]},
	{ path: 'rolepermission/new', component: RolepermissionNewComponent, canActivate: [ AuthGuard ] },
	{ path: 'rolepermission/:id', component: RolepermissionDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	{ path: 'role', component: RoleListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ]},
	{ path: 'role/new', component: RoleNewComponent, canActivate: [ AuthGuard ] },
	{ path: 'role/:id', component: RoleDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	{ path: 'userpermission', component: UserpermissionListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ]},
	{ path: 'userpermission/new', component: UserpermissionNewComponent, canActivate: [ AuthGuard ] },
	{ path: 'userpermission/:id', component: UserpermissionDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	{ path: 'permission', component: PermissionListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ]},
	{ path: 'permission/new', component: PermissionNewComponent, canActivate: [ AuthGuard ] },
	{ path: 'permission/:id', component: PermissionDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	{ path: 'userrole', component: UserroleListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ]},
	{ path: 'userrole/new', component: UserroleNewComponent, canActivate: [ AuthGuard ] },
	{ path: 'userrole/:id', component: UserroleDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
	{ path: 'user', component: UserListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ]},
	{ path: 'user/new', component: UserNewComponent, canActivate: [ AuthGuard ] },
	{ path: 'user/:id', component: UserDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [ AuthGuard ] },
  { path: "entityHistory", component: EntityHistoryComponent,canActivate: [ AuthGuard ]},
	{ path: "apiHistory", component: ApiHistoryComponent,canActivate: [ AuthGuard ]},
  { path: "apiHistoryDetails", component: ApiHistoryDetailsComponent,canActivate: [ AuthGuard ]},
];

export const routingModule: ModuleWithProviders = RouterModule.forChild(routes);