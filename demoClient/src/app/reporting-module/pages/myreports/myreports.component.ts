import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { MainService } from "../../services/main.service";
import { MatInputModule, MatSnackBar, MatDialog, MatDialogRef, getMatAutocompleteMissingPanelError } from "@angular/material";
import { ActivatedRoute, Router } from "@angular/router";
import * as _ from 'lodash';
import { Report, Dashboard } from "../../models/dashboard.model";
import { AddExReportsToDashboardComponent } from "../../modalDialogs/addExReportsToDashboard/addExReportsToDashboard.component";
import { Globals, BaseListComponent, PickerDialogService, ErrorService, ISearchField, operatorType, PickerComponent, listProcessingType } from 'projects/fast-code-core/src/public_api';
import { ShareComponent } from '../share/share.component';
import { ReportPasswordComponent } from './report-password/report-password.component';
import { ReportService } from '../reports/report.service';
import { PermalinkComponent } from '../permalink/permalink.component';

import { IReport } from '../reports/ireport';
import { DashboardService } from '../dashboard/dashboard.service';
import { sharingType } from '../share/ishare-config';
import { ConfirmDialogComponent } from "projects/fast-code-core/src/lib/common/components/confirm-dialog/confirm-dialog.component";
import { Observable, ReplaySubject } from 'rxjs';
import { UserService } from 'src/app/admin/user-management/user/index';
import { takeUntil } from 'rxjs/operators';
import { ChartComponent } from '../../pages/chart/chart.component';

export enum AccessOptions {
  Login  = 'Login',
  noLogin = 'noLogin',
  Password = 'Password'
}

@Component({
  selector: "app-myreports",
  templateUrl: "./myreports.component.html",
  styleUrls: ["./myreports.component.scss"]
})

export class MyreportsComponent extends BaseListComponent<IReport> implements OnInit {
  isMediumDeviceOrLess: boolean;
  showList: boolean = true;
  listFlexWidth = 30;
  detailsFlexWidth = 70;
  dialogRef: MatDialogRef<ShareComponent>;
  passwordDialogRef: MatDialogRef<ReportPasswordComponent>;
  permalinkDialogRef: MatDialogRef<PermalinkComponent>;
  confirmDialogRef: MatDialogRef<ConfirmDialogComponent>;
  userDialogRef: MatDialogRef<PickerComponent>;
  isLoading: boolean = true;
  externalShareView: boolean = false;
  accessOption: string;
  accessOptionIcon: string;
  refreshrate: any = 120;
  accessURL: string = "http://localhost:4400/resourceView/2341234-412342-123414";
  accessOptionTitle: string;
  accessPassword: string;

  extShareOpt = {
    authentication: '',
    description: true,
    title: true,
    refreshRate: '',
    rendering: '',
    resource: 'report',
    password: '',
    resourceId: '',
    toolbar: true,
  }

  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);



  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
      if (this.isMediumDeviceOrLess) {
        this.listFlexWidth = 100;
        this.detailsFlexWidth = 100;
      }
      else {
        this.listFlexWidth = 30;
        this.detailsFlexWidth = 70;
      }

    });
  }

  items: IReport[] = [];
  selectedReport: IReport;
  shareReport: IReport;
  reportUnderAction: IReport;
  allDashboardsList = [];
  searchText: string = "";

  constructor(
    public service: MainService,
    public _snackBar: MatSnackBar,
    public dialog: MatDialog,
    public router: Router,
    public global: Globals,
    public reportService: ReportService,
    public dashboardService: DashboardService,
    public route: ActivatedRoute,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public errorService: ErrorService,
    public userService: UserService,

  ) {
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, reportService, errorService)
  }

  ngOnInit() {
    this.reportService.getAllReports().subscribe(items => {
      this.initializePageInfo();
      this.isLoading = false;
      this.items = items;
      this.updatePageInfo(items);
    });

    this.dashboardService.getAll([], 0, 1000).subscribe(res => {
      this.allDashboardsList = res.map(v => {
        return {
          id: v.id,
          title: v.title
        };
      });
    });

    this.manageScreenResizing();
  }

  /**
   * Gets field based on which table is
   * currently sorted and sort direction
   * from matSort.
   * @returns String containing sort information.
   */
  getSortValue(): string {
    let sortVal = '';
    // if (this.sort.active && this.sort.direction) {
    //   sortVal = this.sort.active + "," + this.sort.direction;
    // }
    return sortVal;
  }

  applyFilters() {
    // let searchField: ISearchField = {
    //   fieldName: "title",
    //   operator: operatorType.Contains,
    //   searchValue: searchValue ? searchValue : ""
    // }
    this.isLoadingResults = true;
    this.initializePageInfo();
    let sortVal = this.getSortValue();
    this.itemsObservable = this.reportService.getAllReports(
      this.searchText,
      this.currentPage * this.pageSize,
      this.pageSize,
      sortVal
    )
    this.processListObservable(this.itemsObservable, listProcessingType.Replace)
  }

  /**
 * Loads more item data when list is
 * scrolled to the bottom (virtual scrolling).
 */
  onTableScroll() {
    if (!this.isLoadingResults && this.hasMoreRecords && this.lastProcessedOffset < this.items.length) {
      this.isLoadingResults = true;
      let sortVal = this.getSortValue();
      this.itemsObservable = this.reportService.getAllReports(this.searchText, this.currentPage * this.pageSize, this.pageSize, sortVal);
      this.processListObservable(this.itemsObservable, listProcessingType.Append);
    }
  }

  viewReport(report: IReport) {
    console.log(report);
    this.selectedReport = report;
    this.showList = false;
  }

  editReport(id) {
    console.log(id);
    this.router.navigate([`reporting/reports/${id}`]);
  }

  resetReport(id) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "confirm"
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        this.reportService.reset(id).subscribe(res => {
          this.items[this.items.findIndex(x => x.id == id)] = res;
        });
      }
    });
  }

  refreshReport(id) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "confirm"
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        this.reportService.refresh(id).subscribe(res => {
          this.showMessage("Report refreshed");
        });
      }
    });
  }

  publishReport(id) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "confirm"
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        this.reportService.publish(id).subscribe(res => {
          this.items[this.items.findIndex(x => x.id == id)] = res;
        });
      }
    });
  }

  deleteReport(report: IReport) {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "delete"
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        this.reportService.delete(report.id).subscribe(res => {
          this.items = this.items.filter(v => v.id !== report.id);
          this.showMessage("Deleted report!");
        });
      }
    });
  }

  showMessage(msg: string): void {
    this._snackBar.open(msg, "close", {
      duration: 2000
    });
  }

  refreshChart(){
    this.selectedReport.query = _.clone(this.selectedReport.query);
  }

  addReporttoDashboardDialog(report: IReport): void {
    const dialogRef = this.dialog.open(AddExReportsToDashboardComponent, {
      panelClass: "fc-modal-dialog",
      data: this.allDashboardsList
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.type === "new") {
          const dashboardDetails = {
            userId: report.userId,
            title: result.title,
            description: result.description,
            reportDetails: [
              {
                id: report.id,
                reportWidth: result.chartSize
              }
            ]
          };
          this.service
            .addExistingReportToNewDashboard(dashboardDetails)
            .subscribe(res => {
              this.showMessage("Added report to " + res.title);
            });
        } else {
          const dashboardDetails = {
            id: result.id,
            userId: report.userId,
            reportDetails: [
              {
                id: report.id,
                reportWidth: result.chartSize
              }
            ]
          };
          this.service
            .addExistingReportToExistingDashboard(dashboardDetails)
            .subscribe(res => {
              this.showMessage("Added report to " + res.title);
            });
        }
      }
    });
  }

  share(report: IReport) {
    this.dialogRef = this.dialog.open(ShareComponent, {
      data: {
        resource: "report",
        id: report.id,
        type: sharingType.Share
      },
      disableClose: true,
      height: '80%',
      width: '50%',
      maxWidth: "none"
    });
    this.dialogRef.afterClosed().subscribe(data => {
      if (data) {
        console.log(data);
        this.reportService.share(report.id, data).subscribe((data) => {
          console.log(data);
        })
      }
    });
  }

  shareExternaly(report: IReport) {
    // this.externalShareView = true;

    this.permalinkDialogRef = this.dialog.open(PermalinkComponent, {
            // disableClose: true,
            width: '50%',
            height: '80%',
            data: {
              resource: 'report',
              resourceId: report.id
            }
          });
    this.permalinkDialogRef.afterClosed().subscribe(action => {
      if (action.confirm) {
        // this.accessPassword = action.password;
        console.log('options set');
      } else {
        console.log('no option set');
      }
    });
  }


  // closeExternalShare(value) {
  //   this.externalShareView = value;
  // }

  // selectAccessOption(opt) {
  //     this.passwordDialogRef = this.dialog.open(ReportPasswordComponent, {
  //       disableClose: true,
  //       data: {
  //         type: 'setPassword'
  //       }
  //     });
  //     this.passwordDialogRef.afterClosed().subscribe(action => {
  //       if (action.confirm) {
  //         this.accessPassword = action.password;
  //         console.log('password set', this.accessPassword  );
  //       } else {
  //         this.accessPassword = '';
  //         console.log('no password set');
  //       }
  //     });
  //   }
  // }


  manageSharing(report: IReport) {
    this.dialogRef = this.dialog.open(ShareComponent, {
      data: {
        resource: "report",
        id: report.id,
        type: sharingType.Unshare
      },
      disableClose: true,
      height: '80%',
      width: '50%',
      maxWidth: "none"
    });
    this.dialogRef.afterClosed().subscribe(data => {
      if (data) {
        console.log(data);
        this.reportService.unshare(report.id, data).subscribe((data) => {
          console.log(data);
        });
      }
    });
  }

  changeOwner(report: IReport) {
    this.reportUnderAction = report;
    this.userDialogRef = this.pickerDialogService.open({
      Title: "Select new owner of report",
      IsSingleSelection: true,
      DisplayField: "userName"
    });
    this.userDialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.reportService.changeOwner(report.id, result.id).subscribe(res => {
          if (res) {
            this.showMessage(`Report ownership transferred to ${result.userName}`);
          }
        });
      }
    });

    this.userDialogRef.componentInstance.onScroll.pipe(takeUntil(this.destroyed$)).subscribe(res => {
      this.onPickerScroll();
    });

    this.userDialogRef.componentInstance.onSearch.pipe(takeUntil(this.destroyed$)).subscribe(searchText => {
      this.onPickerSearch(searchText);
    });

    this.onPickerSearch("");
  }


  isLoadingPickerResults = true;
  currentPickerPage: number;
  pickerPageSize: number = 30;
  lastProcessedOffsetPicker: number;
  hasMoreRecordsPicker: boolean;

  searchValuePicker: ISearchField[] = [];
  pickerItemsObservable: Observable<any>;

  /**
   * Initializes/Resets paging information of data list
   * of association showing in autocomplete options.
   */
  initializePickerPageInfo() {
    this.hasMoreRecordsPicker = true;
    this.pickerPageSize = 30;
    this.lastProcessedOffsetPicker = -1;
    this.currentPickerPage = 0;
  }

  /**
   * Manages paging for virtual scrolling for data list
   * of association showing in autocomplete options.
   * @param data Item data from the last service call.
   */
  updatePickerPageInfo(data) {
    if (data.length > 0) {
      this.currentPickerPage++;
      this.lastProcessedOffsetPicker += data.length;
    }
    else {
      this.hasMoreRecordsPicker = false;
    }
  }

  /**
   * Loads more data of given association when
   * list is scrolled to the bottom (virtual scrolling).
   * @param association
   */
  onPickerScroll() {
    if (!this.isLoadingPickerResults && this.hasMoreRecordsPicker && this.lastProcessedOffsetPicker < this.items.length) {
      this.isLoadingPickerResults = true;
      let userObs: Observable<any[]>;
      userObs = this.userService.getAll(
        this.searchValuePicker,
        this.currentPickerPage * this.pickerPageSize,
        this.pickerPageSize
      )
      userObs.pipe(takeUntil(this.destroyed$)).subscribe(
        items => {
          this.isLoadingPickerResults = false;
          this.userDialogRef.componentInstance.items = this.userDialogRef.componentInstance.items.concat(items);
          this.updatePickerPageInfo(items);
        },
        error => {
        }
      );
    }
  }

  /**
   * Loads the data meeting given criteria of given association.
   * @param searchValue Filters to be applied.
   * @param association
   */
  onPickerSearch(searchValue: string) {
    if (!searchValue) {
      this.searchValuePicker = [];
    }
    else {
      let searchField: ISearchField = {
        fieldName: "userName",
        operator: operatorType.Contains,
        searchValue: searchValue ? searchValue : ""
      }
      this.searchValuePicker = [searchField];
    }

    this.initializePickerPageInfo()
    this.userService.getAll(
      this.searchValuePicker,
      this.currentPickerPage * this.pickerPageSize,
      this.pickerPageSize
    ).pipe(takeUntil(this.destroyed$)).subscribe(items => {
      this.userDialogRef.componentInstance.items = items;
      this.updatePickerPageInfo(items);
      this.isLoadingPickerResults = false;
    });
  }

  ngOnDestroy() {
    this.destroyed$.next(true);
    this.destroyed$.unsubscribe();
  }
}
