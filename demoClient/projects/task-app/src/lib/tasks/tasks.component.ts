import { Component, OnInit, OnDestroy } from '@angular/core';
import { of, Observable} from 'rxjs';
import { MatDialogRef, MatDialog } from '@angular/material';
import { ActivatedRoute, Router, RouterLinkActive, Params} from "@angular/router";

import { TranslateService } from '@ngx-translate/core';

import { RuntimeAppDefinitionService } from '../runtime-app-definition/runtime-app-definition.service';
import { ProcessService } from '../processes/process.service';
import { TaskService } from './task.service';
import { TaskNewComponent } from './task-new/task-new.component';
import { UserService } from '../common/services/user.service';

import { Globals } from '../globals';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss']
})
export class TasksComponent implements OnInit, OnDestroy {
  state$:any;
  account: any = {}
  isLoadingResults:boolean =false;
  tasks: any[] = [];
  forms: any[] = [
  ];
  selectedTask: any = null;
  state: any = {};

  processDefinitions: any[] = [
    {
      id: "",
      name: 'TASK.FILTER.PROCESS-DEFINITION-PLACEHOLDER'
    }
  ];

  filter: any = {
    param: {
      processDefinitionId: ""
    }
  };
  page: number = 0;
  hasNextPage: boolean;

  appDefinitionKey: string = "";
  apps: any[] = [];
  defaultApps: any[] = [];
  customApps: any[] = [];
  missingAppdefinition = false;

  showList: boolean = false;
  listFlexWidth = 30;
  detailsFlexWidth = 70;

// Init sort options
sorts = [
  { id: 'created-desc', title: 'TASK.FILTER.CREATED-DESC' },
  { id: 'created-asc', title: 'TASK.FILTER.CREATED-ASC' },
  { id: 'due-desc', title: 'TASK.FILTER.DUE-DESC' },
  { id: 'due-asc', title: 'TASK.FILTER.DUE-ASC' }
];

// Init state options
stateFilterOptions = [
  { id: 'open', title: 'TASK.FILTER.STATE-OPEN' },
  { id: 'completed', title: 'TASK.FILTER.STATE-COMPLETED' },
  { id: 'all', title: 'All' }
];

// Init assignment options
assignmentOptions = [
  { id: 'involved', title: this.translate.instant('TASK.FILTER.ASSIGNMENT-INVOLVED') },
  { id: 'assignee', title: this.translate.instant('TASK.FILTER.ASSIGNMENT-ASSIGNEE') },
  { id: 'candidate', title: this.translate.instant('TASK.FILTER.ASSIGNMENT-CANDIDATE') }
];

dialogRef: MatDialogRef<any>;
isMediumDeviceOrLess: boolean;
mediumDeviceOrLessDialogSize: string = "100%";
largerDeviceDialogWidthSize: string = "50%";
largerDeviceDialogHeightSize: string = "85%";

constructor(
  private taskService: TaskService,
  private translate: TranslateService,
  public dialog: MatDialog,
  private global: Globals,
  private processService: ProcessService,
  private userService: UserService,
  private runtimeAppDefinitionService: RuntimeAppDefinitionService,
  private route: ActivatedRoute,
  private router: Router,
) {
  
 }

ngOnInit() {
  this.router.events.subscribe((res:any) => {
    this.state$ = res.url;
    if(this.state$){
      let val = this.state$.split('/');
        this.filter.param.assignment =  val[val.length - 1];
        this.refreshFilter();
    }
    
  });
  
  this.setCurrentUser();

  this.manageScreenResizing();

  this.getRunTimeApplications();
  this.resetFilters(); // First time: init defaults
  // this.setPassedAppDefinition();
  this.refreshFilter();
  this.loadProcessDefinitions(null);
  this.selectPassedTask();

};

ngOnDestroy(){
  this.taskService.selectedTask = null;
}

setCurrentUser(){
  this.userService.getAccount().subscribe(account => {
    this.account = account;
    // if (this.account && this.account.groups && this.account.groups.length > 0) {
    //   for (var i = 0; i < this.account.groups.length; i++) {
    //     if (this.account.groups[i].type == 1) {
    //       this.assignmentOptions.push({ id: 'group_' + this.account.groups[i].id, title: this.translate.instant('TASK.FILTER.ASSIGNMENT-GROUP', this.account.groups[i]) });
    //     }
    //   }
    // }
  })
}

getRunTimeApplications() {
  this.runtimeAppDefinitionService.getApplications().subscribe((response) => {
    this.transformAppsResponse(response);
    this.apps = this.defaultApps.concat(this.customApps);
  })
};

transformAppsResponse(response) {
  response.data.forEach(app => {
    if (app.defaultAppId !== undefined && app.defaultAppId !== null) {
      if (app.defaultAppId === 'tasks') {
        this.defaultApps.push(
          {
            titleKey: 'APP.TASKS.TITLE',
            descriptionKey: 'APP.TASKS.DESCRIPTION',
            defaultAppId: app.defaultAppId
          });
      }
    } else {
      // Custom app
      this.customApps.push(app);
    }
  });
};

manageScreenResizing() {
  this.global.isMediumDeviceOrLess$.subscribe(value => {
    this.isMediumDeviceOrLess = value;
    if (this.dialogRef)
      this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
        value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);

    if (this.isMediumDeviceOrLess) {
      this.listFlexWidth = 100;
      this.detailsFlexWidth = 100;
    }
    else{
      this.listFlexWidth = 30;
      this.detailsFlexWidth = 70;
    }

  });
}

setPassedAppDefinition() {
  this.appDefinitionKey = this.route.snapshot.paramMap.get('appDefinitionKey');
  if (this.appDefinitionKey) {
    this.filter.param.appDefinitionKey = this.appDefinitionKey;
    this.runtimeAppDefinitionService.appDefinitionKey = this.appDefinitionKey;
  }
  else if (this.runtimeAppDefinitionService.appDefinitionKey) {
    this.filter.param.appDefinitionKey = this.runtimeAppDefinitionService.appDefinitionKey;
    this.router.navigateByUrl('app/tasks/' + this.runtimeAppDefinitionService.appDefinitionKey);
  }
  else {
    this.runtimeAppDefinitionService.appDefinitionKey = "";
  }
}

// when routed from process page     
selectPassedTask() {
  if (this.taskService.selectedTask) {
    this.selectTask(this.taskService.selectedTask);
  }
}

selectTask(task) {
  this.selectedTask = task;
  this.showList = false;
}

refreshFilter() {
  this.filter.loading = true;

  var params = this.filter.param;
  params.nonDefaultFilter = false;

  // Assignee
  var data: any = {
    assignee: this.account.id,
    page: this.page
  };

  // Text filter
  if (params.text) {
    data.text = params.text;
    params.nonDefaultFilter = true;
  }

  // State folderm
  if (params.state) {
    data.state = params.state.id;

    if (params.state.id !== 'open') {
      params.nonDefaultFilter = true;
    }
  }

  // Assignment
  if (params.assignment) {
    data.assignment = params.assignment;

    if (params.assignment !== 'involved') {
      params.nonDefaultFilter = true;
    }
  }

  // Process definition
  if (params.processDefinitionId && params.processDefinitionId !== 'default') { // default = empty choice
    data.processDefinitionId = params.processDefinitionId;
    params.nonDefaultFilter = true;
  }

  // App definition
  if (params.appDefinitionKey) {
    data.appDefinitionKey = params.appDefinitionKey;
  }

  // Sort order
  data.sort = params.sort;

  this.getTasks(data, params);
};

getTasks(filterData, params) {
  this.taskService.queryTasks(filterData).subscribe((response) => {
    this.filter.loading = false;
    console.log(response);
    if (response.start === 0) {
      this.selectedTask = this.taskService.selectedTask;
      if (response.data.length > 0 && !this.selectedTask) {
        this.selectedTask = response.data[0]
      }

      this.tasks = response.data;

      if ((!response.data || response.data.length == 0) && !params.nonDefaultFilter) {
        this.state = { noOwnTasks: true };
      } else {
        this.state = { noOwnTasks: false };
      }

    }
    else {
      for (var taskIndex = 0; taskIndex < response.data.length; taskIndex++) {
        this.tasks.push(response.data[taskIndex]);
      }
      this.state = { noOwnTasks: false };

      if (!this.selectedTask) {
        this.selectedTask = response.data[0];
      }
    }
    this.hasNextPage = (response.start + response.size < response.total);

  });
}

// Sets defaults for all filters
resetFilters() {

  this.page = 0;

  // Init empty filter model
  this.filter = {
    loading: false,
    expanded: false,
    param: {
      state: this.stateFilterOptions[0],
      nonDefaultFilter: false
    }
  };

  // Defaults
  this.filter.param.sort = this.sorts[0].id;
  this.filter.param.assignment = this.assignmentOptions[0].id;
  this.filter.param.processDefinitionId = 'default';

  // this.appDefinitionKey = "test_app";
  this.missingAppdefinition = this.appDefinitionKey === "";

  // In case of viewing tasks in an app-context, need to make filter aware of this
  this.filter.param.appDefinitionKey = this.appDefinitionKey;

  // Propagate to root filter
  // if (forcePropagateToRootScope === true || ($rootScope.taskFilter === null && $rootScope.taskFilter === undefined)) {
  //   $rootScope.taskFilter = { param: this.filter.param };
  // }
};

nextPage() {
  this.page = this.page + 1;
  this.refreshFilter();
};

resetPaging() {
  this.page = 0;
};

onTaskCompletion() {
  this.taskService.selectedTask = null;
  this.resetPaging();
  this.refreshFilter();
}

createTask() {

  this.dialogRef = this.dialog.open(TaskNewComponent, {
    disableClose: true,
    height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
    width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
    maxWidth: "none",
    panelClass: 'fc-modal-dialog'
  });
  this.dialogRef.afterClosed().subscribe(task => {
    this.confirmTaskCreation(task)
  });
}

confirmTaskCreation(newTask) {
  this.isLoadingResults = true;
  if (newTask && newTask.name) {
    var taskData: any = {
      name: newTask.name,
      description: newTask.description,
      assignee: newTask.assignee ? newTask.assignee.id : null
    };

    if (this.filter.param.appDefinitionKey) {
      taskData.category = this.filter.param.appDefinitionKey;
    }

    newTask.loading = true;
    this.taskService.createTask(taskData).subscribe((task) => {
      newTask.loading = false;
      this.isLoadingResults = false;
      // this.resetFilters();
      this.resetPaging();
      this.refreshFilter();
    });
  }
};

// handles event emitted by detail component when a new task is selected
onOpenTask(task) {
  this.selectedTask = task;
}

onTableScroll() {
  if (this.hasNextPage) {
    ++this.page;
    this.refreshFilter();
  }
}

toggleFilter() {
  this.filter.expanded = !this.filter.expanded;
}

loadProcessDefinitions(appDefinitionKey) {
  this.processService.getProcessDefinitions(appDefinitionKey).subscribe((response) => {
    this.processDefinitions = this.processDefinitions.concat(response.data);
  });
};

applyFilters() {
  this.resetPaging();
  this.refreshFilter();
}

selectStateFilter(state) {
  if (state !== this.filter.param.state) {
    this.filter.param.state = state;
    this.applyFilters();
  }
}

sortChanged() {
  this.resetPaging();
  this.refreshFilter();
};

}
