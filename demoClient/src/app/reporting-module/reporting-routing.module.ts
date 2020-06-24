import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { HomeComponent } from "./pages/home/home.component";
import { SchemaComponent } from "./pages/schema/schema.component";
import { ReportsComponent } from "./pages/reports/reports.component";
import { EditDashboardComponent } from "./pages/dashboard/editdashboard/editdashboard.component";
import { ManageDashboardsComponent } from "./pages/dashboard/manage-dashboards/manage-dashboards.component";
import { PreviewComponent } from "./pages/dashboard/preview/preview.component";
import { DashboardComponent } from "./pages/dashboard/dashboard.component";
import { MyreportsMainComponent } from "./pages/myreports/myreports-main/myreports-main.component";
import { MyreportsDetailComponent } from "./pages/myreports/myreports-detail/myreports-detail.component";
import { PagesComponent } from "./pages/pages.component";
import { AuthGuard } from "src/app/core/auth-guard";
import { DashboardMainComponent } from "./pages/dashboard/dashboard-main/dashboard-main.component";
import { ShareComponent } from './pages/share/share.component';
import { CalculateFieldComponent } from './pages/calculate-field/calculate-field.component';

const routes: Routes = [
  {
    path: "",
    canActivate: [AuthGuard],
    children: [
      { path: "schema", component: SchemaComponent },
      //{ path: 'dashboards', component: DashboardMainComponent },
      { path: "reports/:id", component: ReportsComponent },
      {
        path: "myreports",
        component: MyreportsMainComponent
      },
      { path: "myreports/edit/:id", component: MyreportsDetailComponent },
      {
        path: "dashboard",
        component: DashboardComponent,
        children: [
          { path: "", component: DashboardMainComponent },
          { path: "edit/:id", component: EditDashboardComponent },
          { path: "preview/:id", component: PreviewComponent }
        ]
      },
      { path: 'share', component: ShareComponent },
      { path: 'calculate', component: CalculateFieldComponent },

      { path: "", redirectTo: "schema", pathMatch: "full" }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportingRoutingModule {}
