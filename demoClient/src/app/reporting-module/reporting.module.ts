import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClipboardModule } from 'ngx-clipboard';
import { ReportingRoutingModule } from './reporting-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CubejsClientModule } from '@cubejs-client/ngx';
import { PermalinkComponent } from './pages/permalink/permalink.component';
import { ReportsComponent } from './pages/reports/reports.component';
import { SchemaComponent } from './pages/schema/schema.component';
import { EditDashboardComponent } from './pages/dashboard/editdashboard/editdashboard.component';
import { AddReportsToDashboardComponent } from './modalDialogs/addReportsToDashboard/addReportsToDashboard.component';
import { DropdownDirective } from './directives/dropdown.directive';
import { SchemadropdownDirective } from './directives/schemadropdown.directive';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ManageDashboardsComponent } from './pages/dashboard/manage-dashboards/manage-dashboards.component';
import { PreviewComponent } from './pages/dashboard/preview/preview.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { MyreportsComponent } from './pages/myreports/myreports.component';
import { SaveReportsComponent } from './modalDialogs/saveReports/saveReports.component';
import { AddExReportsToDashboardComponent } from './modalDialogs/addExReportsToDashboard/addExReportsToDashboard.component';
import { SharedDashboardsComponent } from './pages/dashboard/shared-dashboards/shared-dashboards.component';
import { ShareComponent } from './pages/share/share.component';
import { SharePickerComponent } from './pages/share/share-picker/share-picker.component';

import { MatSlideToggleModule, MatMenuModule, MatProgressSpinnerModule } from '@angular/material';
import { DashboardMainComponent } from './pages/dashboard/dashboard-main/dashboard-main.component';
import { MyreportsMainComponent } from './pages/myreports/myreports-main/myreports-main.component';
import { SharedReportsComponent } from './pages/myreports/shared-reports/shared-reports.component';
import { MyreportsDetailComponent } from './pages/myreports/myreports-detail/myreports-detail.component';
import { FastCodeCoreModule } from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';
import { UpdateDashboardComponent } from './modalDialogs/update-dashboard/update-dashboard.component';
import { SharedModule } from '../shared/shared.module';
import { ReportPasswordComponent } from './pages/myreports/report-password/report-password.component';
const cubejsOptions = {
  token:
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.K9PiJkjegbhnw4Ca5pPlkTmZihoOm42w8bja9Qs2qJg",
  options: {
    apiUrl: "http://localhost:4200/cubejs-api/v1"
  }
};

@NgModule({
  declarations: [
    ReportsComponent,
    PermalinkComponent,
    SchemaComponent,
    DropdownDirective,
    SchemadropdownDirective,
    EditDashboardComponent,
    ManageDashboardsComponent,
    PreviewComponent,
    DashboardComponent,
    MyreportsComponent,
    AddReportsToDashboardComponent,
    AddExReportsToDashboardComponent,
    SaveReportsComponent,
    SharedDashboardsComponent,
    ShareComponent,
    SharePickerComponent,
    DashboardMainComponent,
    MyreportsMainComponent,
    SharedReportsComponent,
    MyreportsDetailComponent,
    UpdateDashboardComponent
  ],
  imports: [
    CommonModule,
    ClipboardModule,
    ReportingRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    FlexLayoutModule,
    SharedModule,
    CubejsClientModule.forRoot(cubejsOptions),
    DragDropModule,
    MatSlideToggleModule,
    MatMenuModule,
    FastCodeCoreModule.forRoot({
      apiUrl: environment.apiUrl
    }),
  ],
  entryComponents: [
    AddReportsToDashboardComponent,
    AddExReportsToDashboardComponent,
    SaveReportsComponent,
    UpdateDashboardComponent,
    PermalinkComponent,
    ReportPasswordComponent
  ]
})
export class ReportingModule { }
