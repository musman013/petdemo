import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

import { JobsComponent } from './jobs/jobs.component';
import { TriggersComponent } from './triggers/triggers.component';
import { JobNewComponent } from './jobs/job-new/job-new.component';
import { JobDetailsComponent } from './jobs/job-details/job-details.component';
import { TriggerNewComponent } from './triggers/trigger-new/trigger-new.component';
import { SelectJobComponent } from './triggers/select-job/select-job.component';
import { TriggerDetailsComponent } from './triggers/trigger-details/trigger-details.component';

import { ExecutingJobsComponent } from './executing-jobs/executing-jobs.component';
import { ExecutionHistoryComponent } from './execution-history/execution-history.component';

export const SchedulerRoutes: Routes = [
    { path: 'jobs', component: JobsComponent },
    { path: 'jobs/:jobName/:jobGroup', component: JobDetailsComponent },
    { path: 'job', component: JobNewComponent },

    { path: 'triggers', component: TriggersComponent },
    { path: 'triggers/:triggerName/:triggerGroup', component: TriggerDetailsComponent },
    { path: 'trigg', component: TriggerNewComponent },
    { path: 'selectJob', component: SelectJobComponent },
    { path: 'executionHistory', component: ExecutionHistoryComponent },
    { path: 'executingJobs', component: ExecutingJobsComponent }
];
