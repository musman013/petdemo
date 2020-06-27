import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { AccessOptions } from '../myreports/myreports.component';
import { AuthenticationService } from 'src/app/core/authentication.service';
import { Router, ActivatedRoute } from '@angular/router';
import { viewClassName } from '@angular/compiler';
import { ReportPasswordComponent } from '../myreports/report-password/report-password.component';
import { MatInputModule, MatSnackBar, MatDialog, MatDialogRef, getMatAutocompleteMissingPanelError } from "@angular/material";
import { ReportService } from '../reports/report.service';
import { ResourceViewService } from './resourceView.service';
import * as _ from 'lodash';

@Component({
  selector: 'app-resource-view',
  templateUrl: './resourceView.component.html',
  styleUrls: ['./resourceView.component.scss']
})

export class ResourceViewComponent implements OnInit {
  //   resource = {authentication: 'noLogin',
  //   description: true,
  //   refreshRate: '120',
  //   rendering: '',
  //   resource: 'report',
  //   // resource: 'dashboard',
  //   password: 'abcd',
  //   resourceId: '',
  //   toolbar: true,
  //   title: true,
  //   // data: {
  //   //   description: 'Dashboard',
  //   //   editable: null,
  //   //   id: 2,
  //   //   isAssignedByRole: null,
  //   //   isPublished: true,
  //   //   isRefreshed: null,
  //   //   isResetable: true,
  //   //   isResetted: null,
  //   //   isShareable: true,
  //   //   ownerDescriptiveField: null,
  //   //   ownerId: 1,
  //   //   ownerSharingStatus: null,
  //   //   recipientSharingStatus: null,
  //   //   reportDetails: [{
  //   //     ctype: 'line',
  //   //     description: 'desc',
  //   //     editable: null,
  //   //     id: 1,
  //   //     isAssignedByDashboard: false,
  //   //     isAssignedByRole: null,
  //   //     isPublished: true,
  //   //     isRefreshed: null,
  //   //     isResetable: null,
  //   //     isResetted: null,
  //   //     orderId: 0,
  //   //     ownerId: 1,
  //   //     ownerSharingStatus: null,
  //   //     query: {
  //   //       dimensions: [],
  //   //       filters: [],
  //   //       measures: ['Apps.count'],
  //   //       // order: {Apps.count: "desc"},
  //   //       timeDimensions: [{dimension: 'Apps.createdDate',granularity: 'week'}]
  //   //     },
  //   //     recipientSharingStatus: null,
  //   //     reportType: 'line',
  //   //     reportWidth: 'fullchart',
  //   //     sharedWithMe: null,
  //   //     sharedWithOthers: true,
  //   //     title: 'new report',
  //   //     userId: 1,
  //   //     version: 'running'
  //   //   }],
  //   //   sharedWithMe: null,
  //   //   sharedWithOthers: null,
  //   //   title: 'Test',
  //   //   userId: 1
  //   //   }
  //   data: {
  //     ctype: 'line',
  //     description: 'desc',
  //     editable: false,
  //     id: 1,
  //     isAssignedByDashboard: false,
  //     isAssignedByRole: false,
  //     isPublished: true,
  //     isRefreshed: false,
  //     isResetted: false,
  //     ownerId: 1,
  //     ownerSharingStatus: false,
  //     query: {
  //     dimensions: [],
  //     filters: [],
  //     measures: ["Apps.count"],
  //     // order: {Apps.count: "desc"},
  //     timeDimensions: [{granularity: "week", dimension: "Apps.createdDate"}]},
  //     recipientSharingStatus: false,
  //     reportType: "line",
  //     reportWidth: null,
  //     sharedWithMe: false,
  //     sharedWithOthers: true,
  //     title: "new report",
  //     userId: 1,
  //     version: "running"
  //   }

  // };
  resource: any;
  // resource: any = {
  // data:{
  //   title: '',
  // },
  // resourceInfo: {
  //   description: false,
  //   password: null,
  //   refreshRate: null,
  //   rendering: 'interactive',
  //   resource: 'report',
  //   toolbar: false
  // }
  // };
  accessPassword: string;
  showResourceView = false;
  resourceViewId: any;
  selectedChart: any = "Select Chart";
  query: any;
  queryParam: any;
  chartType: any;
  timeFilterBy: any;
  interval: any;

  passwordDialogRef: MatDialogRef<ReportPasswordComponent>;
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    public dialog: MatDialog,
    public resourceViewService: ResourceViewService,
    private route: ActivatedRoute,
  ) { }

  ngOnInit() {
    this.resourceViewId = this.route.snapshot.paramMap.get('id');
    this.resourceViewService.getResource(this.resourceViewId).subscribe(resource => {

      this.resource = resource;
      this.buildQuery();
      console.log(resource);
      this.interval = setInterval(this.checkreload, this.resource.resourceInfo.refreshRate * 1000);
      this.checkAccessOption();
    });

  }

  checkreload = () => {
    console.log('in checkreload');
    console.log(this.resource);
    if (!this.resource) {
      console.log('calling reload');
      this.interval.clear();
    }
    this.reload();
  }

  buildQuery() {
    this.query = _.clone(this.resource.data.query);
  }

  reload() {
    this.buildQuery();
  }

  showChart(type, chart, icon) {
    this.resource.data.reportType = type;
    this.chartType = type;
    this.selectedChart = chart;
    this.resetDimentions(chart);
  }

  viewresource() {
    this.showResourceView = true;
  }

  checkAccessOption() {

    switch (this.resource.resourceInfo.authentication) {
      case (AccessOptions.Login):
        if (this.authenticationService.token) {
          this.viewresource();
        }
        this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
        return false;
        break;

      case (AccessOptions.noLogin):
        this.viewresource();
        break;


      case (AccessOptions.Password):
        this.passwordDialog();
        break;
    }

  }

  passwordDialog() {
    this.passwordDialogRef = this.dialog.open(ReportPasswordComponent, {
      disableClose: true,
      panelClass: 'fc-modal-dialog',
      data: {
        type: 'Password'
      }
    });
    this.passwordDialogRef.afterClosed().subscribe(action => {
      if (action.confirm) {
        this.accessPassword = action.password;
        console.log('password set', this.accessPassword);
        if (this.accessPassword == this.resource.resourceInfo.password) {
          this.viewresource();
        } else {
          this.passwordDialog();
        }
      } else {
        this.accessPassword = '';
        console.log('no password set');
      }
    });
  }

  resetDimentions(chart) {
    this.selectedChart = chart;
    if (this.resource.data.query.timeDimensions.length > 0) {
      if (this.chartType === 'singleValue') {
        this.resource.data.query.timeDimensions = [{ dimension: this.resource.data.query.timeDimensions[0].dimension }];
        this.timeFilterBy = 'Week';
      } else {
        let gran = 'week';
        if (this.resource.data.query.timeDimensions[0].granularity) {
          gran = this.resource.data.query.timeDimensions[0].granularity;
        }
        this.resource.data.query.timeDimensions = [{ dimension: this.resource.data.query.timeDimensions[0].dimension, granularity: gran }];
      }
    }
    this.buildQuery();
  }
}
