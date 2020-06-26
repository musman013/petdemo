import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

import { SelectGroupComponent } from './common/components/select-group/select-group.component';
import { ProcessNewComponent } from './processes/process-new/process-new.component';
import { ProcessesComponent } from './processes/processes.component';

import { RuntimeAppDefinitionComponent } from './runtime-app-definition/runtime-app-definition.component';

import { CommentNewComponent } from './tasks/task-details/comment-new/comment-new.component';
import { InvolvePeopleComponent } from './tasks/task-details/involve-people/involve-people.component';
import { UpdateDueDateComponent } from './tasks/task-details/update-due-date/update-due-date.component';
import { TaskNewComponent } from './tasks/task-new/task-new.component';
import { TasksComponent } from './tasks/tasks.component';

import { ConfirmDialogComponent } from './common/components/confirm-dialog/confirm-dialog.component';

export const TaskAppRoutes: Routes = [
    { path: 'tasks/:type', component: TasksComponent },
    { path: 'tasks/taskDetails/comment', component: CommentNewComponent },
    { path: 'tasks/taskDetails/involvePeople', component: InvolvePeopleComponent },
    { path: 'tasks/taskDetails/selectGroup', component: SelectGroupComponent },
    { path: 'tasks/new', component: TaskNewComponent },
    { path: 'tasks/updateDueDate', component: UpdateDueDateComponent },

    { path: 'processes/:type', component: ProcessesComponent },
    { path: 'processes/new', component: ProcessNewComponent },

    { path: 'runtimeAppDefinitions', component: RuntimeAppDefinitionComponent },
    { path: 'confirmDialog', component: ConfirmDialogComponent }

];
