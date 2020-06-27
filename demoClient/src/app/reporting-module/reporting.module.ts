import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClipboardModule } from 'ngx-clipboard';
import { ReportingRoutingModule } from './reporting-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from './material.module';
import { ChartsModule } from 'ng2-charts';
import { CubejsClientModule } from '@cubejs-client/ngx';
import { AuthGuard } from './services/auth.guard';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { PermalinkComponent } from './pages/permalink/permalink.component';
import { ReportsComponent } from './pages/reports/reports.component';
import { SchemaComponent } from './pages/schema/schema.component';
import { EditDashboardComponent } from './pages/dashboard/editdashboard/editdashboard.component';
import { HomeComponent } from './pages/home/home.component';
import { HeaderComponent } from './navigation/header/header.component';
import { SidenavComponent } from './navigation/sidenav/sidenav.component';
import { AddReportsToDashboardComponent } from './modalDialogs/addReportsToDashboard/addReportsToDashboard.component';
import { DropdownDirective } from './directives/dropdown.directive';
import { SchemadropdownDirective } from './directives/schemadropdown.directive';
import { ChartComponent } from './pages/chart/chart.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ManageDashboardsComponent } from './pages/dashboard/manage-dashboards/manage-dashboards.component';
import { PreviewComponent } from './pages/dashboard/preview/preview.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { MyreportsComponent } from './pages/myreports/myreports.component';
import { PagesComponent } from './pages/pages.component';
import { SaveReportsComponent } from './modalDialogs/saveReports/saveReports.component';
import { AddExReportsToDashboardComponent } from './modalDialogs/addExReportsToDashboard/addExReportsToDashboard.component';
import { DashboardListComponent } from './pages/dashboard/dashboard-list/dashboard-list.component';
import { ShareComponent } from './pages/share/share.component';
// import { ReportPasswordComponent } from './pages/myreports/report-password/report-password.component';
import { SharePickerComponent } from './pages/share/share-picker/share-picker.component';

import { MatSlideToggleModule, MatMenuModule, MatProgressSpinnerModule } from '@angular/material';
import { from } from 'rxjs';
import { DashboardMainComponent } from './pages/dashboard/dashboard-main/dashboard-main.component';
import { MyreportsMainComponent } from './pages/myreports/myreports-main/myreports-main.component';
import { MyreportsListComponent } from './pages/myreports/myreports-list/myreports-list.component';
import { MyreportsDetailComponent } from './pages/myreports/myreports-detail/myreports-detail.component';
import { FastCodeCoreModule } from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';
import { UpdateDashboardComponent } from './modalDialogs/update-dashboard/update-dashboard.component';
import { CalculateFieldComponent } from './pages/calculate-field/calculate-field.component';
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
    HomeComponent,
    HeaderComponent,
    SidenavComponent,
    DropdownDirective,
    SchemadropdownDirective,
    EditDashboardComponent,
    ManageDashboardsComponent,
    PreviewComponent,
    DashboardComponent,
    MyreportsComponent,
    PagesComponent,
    AddReportsToDashboardComponent,
    AddExReportsToDashboardComponent,
    SaveReportsComponent,
    DashboardListComponent,
    ShareComponent,
    // ReportPasswordComponent,
    SharePickerComponent,
    DashboardMainComponent,
    MyreportsMainComponent,
    MyreportsListComponent,
    MyreportsDetailComponent,
    UpdateDashboardComponent,
    CalculateFieldComponent
  ],
  imports: [
    CommonModule,
    ClipboardModule,
    ReportingRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    FlexLayoutModule,
    // MaterialModule,
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
    CalculateFieldComponent,
    ReportPasswordComponent
  ]
})
export class ReportingModule { }
