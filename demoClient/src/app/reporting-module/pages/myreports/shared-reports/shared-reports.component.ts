import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportService } from '../../reports/report.service';
import { IReport } from '../../reports/ireport';
import { ErrorService, listProcessingType } from 'projects/fast-code-core/src/public_api';
import { take } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

export interface PeriodicElement {
  Title: string;
  Description: string;
  position: number;
  Owner: string;
  SharingStatus: boolean;
}

@Component({
  selector: 'app-shared-reports',
  templateUrl: './shared-reports.component.html',
  styleUrls: ['./shared-reports.component.scss']
})
export class SharedReportsComponent implements OnInit {
  displayedColumns: string[] = [
    "Title",
    "Description",
    "Owner",
    "OwnerSharingStatus",
    "RecipientSharingStatus",
    "Actions"
  ];
  // dataSource = ELEMENT_DATA;
  reports: IReport[] = [];
  isLoadingResults: boolean = true;
  reportsObs: Observable<IReport[]>;
  constructor(
    private router: Router,
    private reportService: ReportService,
    private errorService: ErrorService,
    private translate: TranslateService
  ) { }

  ngOnInit() {
    this.reportService.getShared("", 0, 1000, "").subscribe(reports => {
      this.initializePageInfo();
      this.isLoadingResults = false;
      this.reports = reports;
      this.updatePageInfo(reports)
    })
  }

  reportDetails(report: IReport) {
    this.router.navigate([`reporting/myreports/edit/${report.id}`]);
  }

  deleteReport(report: IReport) {
  }

  changeRecipientSharingStatus(report: IReport) {
    this.reportService.updateRecipientSharingStatus(report.id, report.recipientSharingStatus)
      .pipe(take(1))
      .subscribe(res => {
        if (res) {
          this.errorService.showError(this.translate.instant('REPORTING.MESSAGES.STATUS-CHANGED'));
        } else {
          this.errorService.showError(this.translate.instant('REPORTING.MESSAGES.ERROR-OCCURRED'));
        }
      })
  }

  currentPage: number;
  pageSize: number;
  lastProcessedOffset: number;
  hasMoreRecords: boolean;
  searchValue: string = ''; // report title

  /**
   * Initializes/Resets paging information.
   */
  initializePageInfo() {
    this.hasMoreRecords = true;
    this.pageSize = 30;
    this.lastProcessedOffset = -1;
    this.currentPage = 0;
  }

  /**
   * Manages paging for virtual scrolling.
   * @param data Item data from the last service call.
   */
  updatePageInfo(data) {
    if (data.length > 0) {
      this.currentPage++;
      this.lastProcessedOffset += data.length;
    }
    else {
      this.hasMoreRecords = false;
    }
  }

  /**
   * Loads more item data when list is
   * scrolled to the bottom (virtual scrolling).
   */
  onTableScroll() {
    if (!this.isLoadingResults && this.hasMoreRecords && this.lastProcessedOffset < this.reports.length) {
      this.isLoadingResults = true;
      let sortVal = this.getSortValue();
      this.reportsObs = this.reportService.getShared(this.searchValue, this.currentPage * this.pageSize, this.pageSize, sortVal);
      this.processListObservable(this.reportsObs, listProcessingType.Append);
    }
  }

  /**
   * Fetches item data based on given filter criteria.
   * @param filterCriteria Filters to be applied.
   */
  applyFilter(filterCriteria: string): void {
    this.searchValue = filterCriteria;
    this.isLoadingResults = true;
    this.initializePageInfo();
    let sortVal = this.getSortValue();
    this.reportsObs = this.reportService.getShared(
      this.searchValue,
      this.currentPage * this.pageSize,
      this.pageSize,
      sortVal
    )
    this.processListObservable(this.reportsObs, listProcessingType.Replace)
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

  /**
   * Processes observable response data gotten from the service.
   * @param listObservable observable item data.
   * @param type processing type to determine whether to append to
   * or replace existing item data.
   */
  processListObservable(listObservable: Observable<IReport[]>, type: listProcessingType) {
    listObservable.subscribe(
      reports => {
        this.isLoadingResults = false;
        if (type == listProcessingType.Replace) {
          this.reports = reports;
        }
        else {
          this.reports = this.reports.concat(reports);
        }
        this.updatePageInfo(reports);
      },
      error => {
        this.isLoadingResults = false;
        this.errorService.showError(this.translate.instant('REPORTING.MESSAGES.ERROR-FETCHING-RESULT'));
      }
    )
  }
}
