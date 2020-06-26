import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  MatButtonModule, MatToolbarModule, MatSidenavModule,
  MatIconModule, MatRadioModule, MatTableModule,
  MatCardModule, MatTabsModule, MatInputModule, MatDialogModule,
  MatSelectModule, MatCheckboxModule, MatAutocompleteModule,
  MatDatepickerModule, MatNativeDateModule, MatMenuModule, MatSortModule,
  MatPaginatorModule, MatProgressSpinnerModule,MatSnackBarModule, MatProgressBarModule,
  MatChipsModule, MatExpansionModule, MatListModule, MatLineModule, MatGridListModule,MatTooltipModule
} from '@angular/material';
import { FlexLayoutModule } from '@angular/flex-layout';


import { TaskAppComponent } from './task-app.component';
import { IForRootConf } from './IForRootConf';
import { IP_CONFIG } from './tokens';
import { Globals } from './globals';

import { SelectGroupComponent } from './common/components/select-group/select-group.component';
import { UserNameComponent } from './common/components/user-name/user-name.component';
import { UserPictureComponent } from './common/components/user-picture/user-picture.component';

import { FormFieldComponent } from './form/form-field/form-field.component';
import { FormComponent } from './form/form.component';

import { ProcessDetailsComponent } from './processes/process-details/process-details.component';
import { ProcessNewComponent } from './processes/process-new/process-new.component';
import { ProcessesComponent } from './processes/processes.component';

import { RuntimeAppDefinitionComponent } from './runtime-app-definition/runtime-app-definition.component';

import { CommentNewComponent } from './tasks/task-details/comment-new/comment-new.component';
import { DocumentPreviewComponent } from './tasks/task-details/document-preview/document-preview.component';
import { InvolvePeopleComponent } from './tasks/task-details/involve-people/involve-people.component';
import { TaskDetailsComponent } from './tasks/task-details/task-details.component';
import { UpdateDueDateComponent } from './tasks/task-details/update-due-date/update-due-date.component';
import { TaskNewComponent } from './tasks/task-new/task-new.component';
import { TasksComponent } from './tasks/tasks.component';

import { ConfirmDialogComponent } from './common/components/confirm-dialog/confirm-dialog.component';
import { ActiveDirective } from './tasks/active.directive';

import { TaskAppRoutes } from './task-app-routing.module';
import { RouterModule } from '@angular/router';

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}

@NgModule({
  imports: [
    RouterModule.forChild(TaskAppRoutes),
    CommonModule, FormsModule, ReactiveFormsModule,
    MatButtonModule, MatToolbarModule, MatSidenavModule,MatTooltipModule,
    MatIconModule, MatRadioModule, MatTableModule,
    MatCardModule, MatTabsModule, MatInputModule, MatDialogModule,
    MatSelectModule, MatCheckboxModule, MatAutocompleteModule,
    MatDatepickerModule, MatNativeDateModule, MatMenuModule, MatSortModule,
    MatPaginatorModule, MatProgressSpinnerModule,MatSnackBarModule, MatProgressBarModule,
    MatChipsModule, MatExpansionModule, MatListModule, MatLineModule, FlexLayoutModule, MatGridListModule,
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  declarations: [
    TaskAppComponent,
    SelectGroupComponent,
    UserNameComponent,
    UserPictureComponent,
    UserNameComponent,
    FormComponent,
    FormFieldComponent,
    ProcessDetailsComponent,
    ProcessNewComponent,
    ProcessesComponent,
    RuntimeAppDefinitionComponent,
    CommentNewComponent,
    DocumentPreviewComponent,
    InvolvePeopleComponent,
    TaskDetailsComponent,
    UpdateDueDateComponent,
    TaskNewComponent,
    TasksComponent,
    ConfirmDialogComponent,
    ActiveDirective
  ],
  exports: [
    TaskAppComponent,
    SelectGroupComponent,
    UserNameComponent,
    UserPictureComponent,
    UserNameComponent,
    FormComponent,
    FormFieldComponent,
    ProcessDetailsComponent,
    ProcessNewComponent,
    ProcessesComponent,
    RuntimeAppDefinitionComponent,
    CommentNewComponent,
    DocumentPreviewComponent,
    InvolvePeopleComponent,
    TaskDetailsComponent,
    UpdateDueDateComponent,
    TaskNewComponent,
    TasksComponent,
    ConfirmDialogComponent,
    ActiveDirective
  ]
})
export class TaskAppModule1 { }

@NgModule({})
export class TaskAppModule {
  static forRoot(config: IForRootConf): ModuleWithProviders {
    return {
      ngModule: TaskAppModule1,
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