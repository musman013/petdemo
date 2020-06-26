import { Component, OnInit, OnChanges } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from "@angular/router";

import { TranslateService } from '@ngx-translate/core';

import { ProcessNewComponent } from './process-new/process-new.component'
import { ProcessService } from './process.service'
import { RuntimeAppDefinitionService } from '../runtime-app-definition/runtime-app-definition.service';
import { Globals } from '../globals';

@Component({
  selector: 'app-processes',
  templateUrl: './processes.component.html',
  styleUrls: ['./processes.component.scss']
})
export class ProcessesComponent implements OnInit {

  stateFilterOptions = [
    { 'id': 'running', 'title': 'PROCESS.FILTER.STATE-RUNNING' },
    { 'id': 'completed', 'title': 'PROCESS.FILTER.STATE-COMPLETED' },
    { 'id': 'all', 'title': 'PROCESS.FILTER.STATE-ALL' }
  ];

  runtimeSorts = [
    { 'id': 'created-desc', 'title': 'PROCESS.FILTER.CREATED-DESC' },
    { 'id': 'created-asc', 'title': 'PROCESS.FILTER.CREATED-ASC' }
  ];

  completedSorts: any[] = [];
  sorts: any[] = [];
  state: any = {};

  hasNextPage: boolean;
  page: number = 0;
  initialLoad: boolean = false;
  mode: string = 'process-list';

  showList: boolean = false;
  listFlexWidth = 30;
  detailsFlexWidth = 70;

  selectedSort: string;

  filter: any = {
    loading: false,
    expanded: false,
    param: {
      state: this.stateFilterOptions[0],
      sort: this.runtimeSorts[0].id
    }
  }
pageType:String ='';
  appDefinitionKey: string = "";
  apps: any[] = [];
  defaultApps: any[] = [];
  customApps: any[] = [];

  processInstances: any[] = [];
  selectedProcessInstance: any;
lableShow:boolean= false;
  dialogRef: MatDialogRef<any>;
  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "50%";
  largerDeviceDialogHeightSize: string = "85%";

  constructor(
    private processService: ProcessService,
    public dialog: MatDialog,
    private global: Globals,
    private formBuilder: FormBuilder,
    private translate: TranslateService,
    private runtimeAppDefinitionService: RuntimeAppDefinitionService,
    private route: ActivatedRoute,
    private router: Router,
  ) { 
      this.router.events.subscribe((res:any) => {
        this.pageType = res.url;
        if(this.pageType){
          let val = this.pageType.split('/');
        if(val[val.length - 1] ==  'all'){
          this.lableShow= true;
        }else{
          this.lableShow= false;
        }
         this.stateFilterOptions.filter((items)=>{              
            if(items.id == val[val.length - 1]){
              this.selectStateFilter(items);
           }
         })
                     
        }
        
      });
  }

  ngOnInit() {
    this.manageScreenResizing();
    console.log("May Page :",this.pageType);
    this.completedSorts.push(this.runtimeSorts[0]); // needs to be same reference as runtime sort!
    this.completedSorts.push(this.runtimeSorts[1]); // needs to be same reference as runtime sort!
    this.completedSorts.push({ 'id': 'ended-asc', 'title': 'PROCESS.FILTER.ENDED-DESC' });
    this.completedSorts.push({ 'id': 'ended-desc', 'title': 'PROCESS.FILTER.ENDED-ASC' });

    this.getRunTimeApplications();
    // this.setPassedAppDefinition();
    this.sorts = this.runtimeSorts;
    this.loadProcessInstances(false);

    this.selectedSort = this.filter.param.sort;
  }
  ngOnChanges(){

  }

  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
      if (this.dialogRef)
        this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);

      if (this.isMediumDeviceOrLess) {
        console.log("ASFD", this.isMediumDeviceOrLess)
        this.listFlexWidth = 100;
        this.detailsFlexWidth = 100;
      }
      else {
        this.listFlexWidth = 30;
        this.detailsFlexWidth = 70;
      }
    });
  }
  getRunTimeApplications() {
    this.runtimeAppDefinitionService.getApplications().subscribe((response) => {
      this.transformAppsResponse(response);
      this.apps = this.defaultApps.concat(this.customApps);
    })
  }

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

  setPassedAppDefinition() {
    this.appDefinitionKey = this.route.snapshot.paramMap.get('appDefinitionKey');
    if (this.appDefinitionKey) {
      this.filter.param.appDefinitionKey = this.appDefinitionKey;
      this.runtimeAppDefinitionService.appDefinitionKey = this.appDefinitionKey;
    }
    else if (this.runtimeAppDefinitionService.appDefinitionKey) {
      this.filter.param.appDefinitionKey = this.runtimeAppDefinitionService.appDefinitionKey;
      this.router.navigateByUrl('app/processes/' + this.runtimeAppDefinitionService.appDefinitionKey);
    }
    else {
      this.runtimeAppDefinitionService.appDefinitionKey = "";
    }
  }

  // nextPage flag indicates whether its first page or not. false for first page 
  loadProcessInstances(nextPage) {

    this.filter.loading = true;

    var params = this.filter.param;

    if (nextPage) {
      this.page += 1;
    } else {
      this.page = 0;
    }

    var instanceQueryData: any = {
      sort: params.sort,
      page: this.page
    };

    if (params.appDefinitionKey) {
      instanceQueryData.appDefinitionKey = params.appDefinitionKey;
    }

    if (params.state) {
      instanceQueryData.state = params.state.id;
    }
    console.log(instanceQueryData);
    this.getProcesses(instanceQueryData);

  };

  getProcesses(filterData) {
    this.processService.queryProcesses(filterData).
      subscribe((response) => {
        this.initialLoad = true;
        var instances = response.data;

        if (response.start > 0) {
          // Add results instead of removing existing ones
          for (var i = 0; i < instances.length; i++) {
            this.processInstances.push(instances[i]);
          }

          this.state = { noProcesses: false };
        } else {

          this.selectedProcessInstance = undefined;
          this.processInstances = instances;
          if (instances.length > 0)
            this.selectedProcessInstance = instances[0];
          this.state = { noProcesses: (!response.data || response.data.length == 0) };
        }

        if (response.start + response.size < response.total) {
          // More pages available
          this.hasNextPage = true;
        } else {
          this.hasNextPage = false;
        }

        this.filter.loading = false;

      })
  }

  nextPage() {
    this.loadProcessInstances(true);
  };

  selectProcessInstance(processInstance) {
    this.selectedProcessInstance = processInstance;
    this.showList = false;
  }

  onCancelProcess() {
    this.loadProcessInstances(false);
  }

  expandFilter() {
    this.filter.expanded = true;
  };

  collapseFilter() {
    this.filter.expanded = false;
  };

  selectStateFilter(state) {
    console.log("My State :",state)
    if (state != this.filter.param.state) {
      this.filter.param.state = state;
      if (state) {
        if (state.id === 'completed' || state.id === 'all') {
          this.sorts = this.completedSorts;
        }
        this.loadProcessInstances(false);
      }
    }
  };

  changeSort() {
    this.loadProcessInstances(false);
  };

  createProcessInstance() {
    this.dialogRef = this.dialog.open(ProcessNewComponent, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog'
    });
    this.dialogRef.afterClosed().subscribe(processDefinition => {
      if (processDefinition) {
        this.confirmProcessCreation(processDefinition)
      }
    });
  };

  confirmProcessCreation(processDefinition) {
    var createInstanceData = {
      processDefinitionId: processDefinition.processDefinition.id,
      name: processDefinition.name
    };
    this.processService.createProcess(createInstanceData).subscribe((response) => {
      if (response) {
        this.loadProcessInstances(false);
      }
    });
  };
}
