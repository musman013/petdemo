import { Component, OnInit,TemplateRef, ChangeDetectorRef, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource, MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { merge, of as observableOf } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { first, filter } from 'rxjs/operators';
import { IJob } from './ijob';
import { JobService } from './job.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Globals } from '../globals';
import { JobNewComponent } from './job-new/job-new.component';
import { ConfirmDialogComponent } from 'projects/fast-code-core/src/public_api';;
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IListColumn, listColumnType, ISearchField, operatorType  } from 'projects/fast-code-core/src/public_api';;
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.scss']
})
export class JobsComponent implements OnInit {

  @ViewChild(MatSort,{  static: true }) sort: MatSort;
	@ViewChild("deleteBox", { static: true }) deleteBox: TemplateRef<any>;
  userId: number;
	filterSelect: string = '';
  jobs: IJob[] = [];
  dataSource;

  isLoadingResults = true;
  filterList:any={};
  currentPage: number;
  pageSize: number;
  lastProcessedOffset: number;
  hasMoreRecords = true;
  searchValue: ISearchField[] = [];

  errorMessage = '';
  columns: IListColumn[] = [
    {
      column: 'jobName',
      label: this.translate.instant('JOBS.FIELDS.NAME'),
      sort: true,
      filter: true,
      type: listColumnType.String
    },
    {
      column: 'jobGroup',
      label: this.translate.instant('JOBS.FIELDS.GROUP'),
      sort: true,
      filter: true,
      type: listColumnType.String
    },
    {
      column: 'jobClass',
      label: this.translate.instant('JOBS.FIELDS.CLASS'),
      sort: true,
      filter: true,
      type: listColumnType.String
    },
    {
      column: 'actions',
      label: this.translate.instant('SCHEDULER-GENERAL.ACTIONS.ACTIONS'),
      sort: false,
      filter: false,
      type: listColumnType.String
    },
  ]
  selectedColumns = this.columns;

  //since columns are different from filters fields at backend
  allDisplayedColumns: string[] = ["jobName", "jobGroup", "jobClass", "actions"];
  displayedColumns: string[] = this.allDisplayedColumns;

  isMediumDeviceOrLess: boolean;
  dialogRef: MatDialogRef<any>;
  confirmDialogRef: MatDialogRef<any>;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "75%";
  largerDeviceDialogHeightSize: string = "90%";

  filterForm: FormGroup;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private global: Globals,
    private jobService: JobService,
    public dialog: MatDialog,
    private changeDetectorRefs: ChangeDetectorRef,
    private translate: TranslateService
  ) {

  }

  ngOnInit() {
    this.manageScreenResizing();
    this.setSort();
    this.filterList = this.selectedColumns.filter(items => {
			if (items.filter) {
				return items
			}
		});

  }

  selectedFilter: String;
	selctFilter(event) {
		this.filterSelect = event;
		this.selectedFilter = event;

	}
	applyFilter1(filterValue: string) {
		if (filterValue !== '') {
    }
	}

  setSort() {
    this.sort.sortChange.pipe(
      startWith({}),
      switchMap(() => {
        this.isLoadingResults = true;
        this.initializePageInfo();
        return this.jobService.getAll(this.searchValue, 0, this.pageSize, this.getSortValue());
      }),
      map(data => {
        // Flip flag to show that loading has finished.
        this.isLoadingResults = false;
        return data;
      }),
      catchError(() => {
        this.isLoadingResults = false;
        // Catch if some error occurred. Return empty data.
        return observableOf([]);
      })
    ).subscribe(data => {
      this.jobs = data;
      this.dataSource = new MatTableDataSource(this.jobs);
      //manage pages for virtual scrolling
      this.updatePageInfo(data);
    });
  }

  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
      if (value) {
        this.selectedColumns = this.columns.slice(0, 3);
        this.displayedColumns = this.allDisplayedColumns.slice(0, 3)
      }
      else {
        this.selectedColumns = this.columns;
        this.displayedColumns = this.allDisplayedColumns;
      }

      if (this.dialogRef)
        this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);

    });
  }

  add() {
    this.openDialog();
  }

  openDialog() {

    this.dialogRef = this.dialog.open(JobNewComponent, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog'
    });
    this.dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      if (result) {
        this.jobs = [...this.jobs, result];
        this.dataSource = new MatTableDataSource(this.jobs);
        this.changeDetectorRefs.detectChanges();
      }
    });

  }

  closeUserDialog() {
    var result: any;
    //this.dialogRef.close(result);
  }

  triggerJob(job, index): void {
    this.openConfirmMessage(this.translate.instant('JOBS.MESSAGES.CONFIRM.TRIGGER'), this.triggerJobActionResult, job, index)
  }

  triggerJobActionResult = (action, job) => {
    if (action) {
      console.log(job, "job triggered");
    }
  }

  pauseJob(job, index): void {
    this.openConfirmMessage(this.translate.instant('JOBS.MESSAGES.CONFIRM.PAUSE'), this.pauseJobActionResult, job, index)
  }

  pauseJobActionResult = (action, job: IJob) => {
    if (action) {
      this.jobService.pauseJob(job.jobName, job.jobGroup).subscribe(
        resp => {
          if(resp){
            job.jobStatus = "PAUSED";
          }
          // this.initializePageInfo();
          // this.jobService.getAll(
          //   this.searchValue,
          //   this.currentPage * this.pageSize,
          //   this.pageSize,
          //   this.getSortValue()
          // ).subscribe(
          //   perms => {
          //     this.jobs = perms;
          //   },
          //   error => this.errorMessage = <any>error
          // );
        },
        error => this.errorMessage = <any>error
      );
    }
  }

  resumeJob(job, index): void {
    this.openConfirmMessage(this.translate.instant('JOBS.MESSAGES.CONFIRM.RESUME'), this.resumeJobActionResult, job, index)
  }

  resumeJobActionResult = (action, job) => {
    if (action) {
      this.jobService.resumeJob(job.jobName, job.jobGroup).subscribe(
        resp => {
          if(resp){
            job.jobStatus = "ACTIVE";
          }
          // this.initializePageInfo();
          // this.jobService.getAll(
          //   this.searchValue,
          //   this.currentPage * this.pageSize,
          //   this.pageSize,
          //   this.getSortValue()
          // ).subscribe(
          //   perms => {
          //     this.jobs = perms;
          //   },
          //   error => this.errorMessage = <any>error
          // );
        },
        error => this.errorMessage = <any>error
      );
    }
  }

  deleteJob(job, index): void {
    this.openConfirmMessage(this.translate.instant('JOBS.MESSAGES.CONFIRM.DELETE'), this.deleteJobActionResult, job, index)
  }

  deleteJobActionResult = (action, job, index) => {

    if (action) {

      this.jobService.delete(job.jobName, job.jobGroup).subscribe(
        resp => {
          this.jobs.splice(index, 1);
          this.dataSource = new MatTableDataSource(this.jobs);
          this.changeDetectorRefs.detectChanges();
        },
        error => this.errorMessage = <any>error
      );
    }
  }

  openConfirmMessage(message, callback, job, index): void {
    this.confirmDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        "message": message
      }
    });
    this.confirmDialogRef.afterClosed().subscribe(action => {
      if (action) {
        callback(action, job, index);
      }
    });
  }

  applyFilter(searchValue) {
    this.initializePageInfo();
    this.searchValue = searchValue;
    this.isLoadingResults = true;
    this.jobService.getAll(
      this.searchValue,
      this.currentPage * this.pageSize,
      this.pageSize,
      this.getSortValue()
    ).subscribe(
      data => {
        this.isLoadingResults = false;
        this.jobs = data;
        this.dataSource = new MatTableDataSource(this.jobs);
        this.updatePageInfo(data);
      },
      error => this.errorMessage = <any>error
    );
  }

  initializePageInfo() {
    this.pageSize = 10;
    this.lastProcessedOffset = -1;
    this.currentPage = 0;
  }

  //manage pages for virtual scrolling
  updatePageInfo(data) {
    if (data.length > 0) {
      this.currentPage++;
      this.lastProcessedOffset += data.length;
    }
    else {
      this.hasMoreRecords = false;
    }
  }

  onTableScroll() {

    if (!this.isLoadingResults && this.hasMoreRecords && this.lastProcessedOffset < this.jobs.length) {
      this.isLoadingResults = true;
      this.jobService.getAll(
        this.searchValue,
        this.currentPage * this.pageSize,
        this.pageSize,
        this.getSortValue()
      ).subscribe(
        data => {
          this.isLoadingResults = false;
          this.jobs = this.jobs.concat(data);
          this.dataSource = new MatTableDataSource(this.jobs);

          this.updatePageInfo(data);
        },
        error => this.errorMessage = <any>error
      );
    }
  }

  getSortValue(): string {
    let sortVal = '';
    if (this.sort.active && this.sort.direction) {
      sortVal = this.sort.active + "," + this.sort.direction;
    }
    return sortVal;
  }


  deleteItem: any;
	delete1(val) {
		this.deleteItem = val;
		this.dialog.open(this.deleteBox,{
			panelClass:'delete-dialog-box'
		});
	}

}
