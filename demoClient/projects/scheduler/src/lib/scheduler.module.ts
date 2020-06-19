import { NgModule, ModuleWithProviders, InjectionToken } from '@angular/core';
import { schedulerComponent } from './scheduler.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import {
  MatButtonModule, MatToolbarModule, MatSidenavModule,
  MatIconModule, MatListModule, MatRadioModule, MatTableModule,
  MatCardModule, MatTabsModule, MatInputModule, MatDialogModule,
  MatSelectModule, MatCheckboxModule, MatAutocompleteModule,
  MatDatepickerModule, MatNativeDateModule, MatMenuModule, MatSortModule,
  MatPaginatorModule, MatProgressSpinnerModule,MatSnackBarModule
} from '@angular/material';
import {MatChipsModule} from '@angular/material/chips';
import { MatExpansionModule } from '@angular/material/expansion';
import { LayoutModule } from '@angular/cdk/layout';

import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { Globals } from './globals';

import { JobsComponent } from './jobs/jobs.component';
import { ExecutionHistoryComponent } from './execution-history/execution-history.component';
import { TriggersComponent } from './triggers/triggers.component';
import { JobNewComponent } from './jobs/job-new/job-new.component';
import { JobDetailsComponent } from './jobs/job-details/job-details.component';
import { ExecutingJobsComponent } from './executing-jobs/executing-jobs.component';
import { TriggerJobComponent } from './jobs/trigger-job/trigger-job.component';
import { TriggerNewComponent } from './triggers/trigger-new/trigger-new.component';
import { SelectJobComponent } from './triggers/select-job/select-job.component';
import { TriggerDetailsComponent } from './triggers/trigger-details/trigger-details.component';

import { SchedulerRoutes } from './scheduler-routing.module';
import { RouterModule } from '@angular/router';

import { IP_CONFIG } from './tokens';
import { IForRootConf } from './IForRootConf';

import { FastCodeCoreModule } from 'projects/fast-code-core/src/public_api';;
import { environment } from './environment';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}

@NgModule({
  imports: [
    RouterModule.forChild(SchedulerRoutes),
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    LayoutModule,
    MatToolbarModule,
    MatSidenavModule,
    MatTabsModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    MatRadioModule,
    MatTableModule,
    MatCardModule,
    MatSelectModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatMenuModule,
    MatSortModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatChipsModule,
    NgxMaterialTimepickerModule,
    FastCodeCoreModule.forRoot({
      apiUrl: environment.apiUrl
    }),
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  declarations: [
    schedulerComponent,
    JobsComponent,
    ExecutionHistoryComponent,
    TriggersComponent,
    JobNewComponent,
    JobDetailsComponent,
    ExecutingJobsComponent,
    TriggerJobComponent,
    TriggerNewComponent,
    SelectJobComponent,
    TriggerDetailsComponent
  ],
  exports: [
    schedulerComponent,
    JobsComponent,
    ExecutionHistoryComponent,
    TriggersComponent,
    JobNewComponent,
    JobDetailsComponent,
    ExecutingJobsComponent,
    TriggerJobComponent,
    TriggerNewComponent,
    SelectJobComponent,
    TriggerDetailsComponent
  ],
  providers: [Globals]
})

export class SchedulerModule1 {}

@NgModule({})
export class SchedulerModule {
  static forRoot(config: IForRootConf): ModuleWithProviders {
    return {
      ngModule: SchedulerModule1,
      providers: [
        {
          provide: IP_CONFIG,
          useValue: config
        },
        Globals
      ]
    };
  }
}