
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { CanDeactivateGuard } from 'projects/fast-code-core/src/public_api';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SwaggerComponent } from 'src/app/swagger/swagger.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import { LoginComponent } from './login/index';
import { AuthGuard } from './core/auth-guard';
import { SchedulerRoutes } from 'projects/scheduler/src/public_api';
import { EmailRoutes } from 'projects/ip-email-builder/src/public_api';


import { PetsListComponent, PetsDetailsComponent, PetsNewComponent } from './pets/index';
import { VisitsListComponent, VisitsDetailsComponent, VisitsNewComponent } from './visits/index';
import { TypesListComponent, TypesDetailsComponent, TypesNewComponent } from './types/index';
import { SpecialtiesListComponent, SpecialtiesDetailsComponent, SpecialtiesNewComponent } from './specialties/index';
import { VetsListComponent, VetsDetailsComponent, VetsNewComponent } from './vets/index';
import { OwnersListComponent, OwnersDetailsComponent, OwnersNewComponent } from './owners/index';
import { VetSpecialtiesListComponent, VetSpecialtiesDetailsComponent, VetSpecialtiesNewComponent } from './vet-specialties/index';
import { InvoicesListComponent, InvoicesDetailsComponent, InvoicesNewComponent } from './invoices/index';
import { CompleteVisitComponent } from './visits/complete-visit/complete-visit.component';
import { MainNavComponent } from './common/components/main-nav/main-nav.component';
import { ResourceViewComponent } from './reporting-module/pages/resourceView/resourceView.component';
import { TaskAppRoutes } from 'projects/task-app/src/public_api';

const routes: Routes = [
	{ path: 'resourceView/:id', component: ResourceViewComponent },
	{ path: '', pathMatch: "full", component: HomeComponent },

	{ path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
	{ path: 'swagger-ui', component: SwaggerComponent, canActivate: [AuthGuard] },
	{
		path: '',
		loadChildren: './admin/admin.module#AdminModule'
	},
	{
		path: '',
		loadChildren: './account/account.module#AccountModule'
	},
	{
		path: 'reporting',
		loadChildren: './reporting-module/reporting.module#ReportingModule',
		canActivate: [AuthGuard]
	},
	{ path: 'login', component: LoginComponent },
	{ path: 'login/:returnUrl', component: LoginComponent },
	{ path: 'pets', component: PetsListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'pets/new', component: PetsNewComponent, canActivate: [AuthGuard] },
	{ path: 'pets/:id', component: PetsDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'visits', component: VisitsListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'visits/new', component: VisitsNewComponent, canActivate: [AuthGuard] },
	{ path: 'visits/:id', component: VisitsDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'visits/complete-visit', component: CompleteVisitComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'types', component: TypesListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'types/new', component: TypesNewComponent, canActivate: [AuthGuard] },
	{ path: 'types/:id', component: TypesDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'specialties', component: SpecialtiesListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'specialties/new', component: SpecialtiesNewComponent, canActivate: [AuthGuard] },
	{ path: 'specialties/:id', component: SpecialtiesDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'vets', component: VetsListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'vets/new', component: VetsNewComponent, canActivate: [AuthGuard] },
	{ path: 'vets/:id', component: VetsDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'owners', component: OwnersListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'owners/new', component: OwnersNewComponent, canActivate: [AuthGuard] },
	{ path: 'owners/:id', component: OwnersDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'vetspecialties', component: VetSpecialtiesListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'vetspecialties/new', component: VetSpecialtiesNewComponent, canActivate: [AuthGuard] },
	{ path: 'vetspecialties/:id', component: VetSpecialtiesDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'invoices', component: InvoicesListComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'invoices/new', component: InvoicesNewComponent, canActivate: [AuthGuard] },
	{ path: 'invoices/:id', component: InvoicesDetailsComponent, canDeactivate: [CanDeactivateGuard], canActivate: [AuthGuard] },
	{ path: 'scheduler', children: SchedulerRoutes, canActivate: [AuthGuard] },
	{ path: 'email', children: EmailRoutes, canActivate: [AuthGuard] },
	{ path: 'task-app', children: TaskAppRoutes, canActivate: [AuthGuard] },
	{ path: '**', component: ErrorPageComponent },

];

export const routingModule: ModuleWithProviders = RouterModule.forRoot(routes, {
	useHash: true,
	enableTracing: true
});