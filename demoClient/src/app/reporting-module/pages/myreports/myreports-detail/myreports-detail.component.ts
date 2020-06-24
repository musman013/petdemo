import { Component, OnInit } from "@angular/core";
import { Report } from "../../../models/dashboard.model";
import { MainService } from "../../../services/main.service";
import { MatSnackBar, MatDialog, MatDialogRef } from "@angular/material";
import { Router, ActivatedRoute } from "@angular/router";
import { IReport } from '../../reports/Ireport';
import { ReportService } from '../../reports/report.service';
import { ConfirmDialogComponent } from 'projects/fast-code-core/src/public_api';
@Component({
  selector: "app-myreports-detail",
  templateUrl: "./myreports-detail.component.html",
  styleUrls: ["./myreports-detail.component.scss"]
})
export class MyreportsDetailComponent implements OnInit {
  report_id;
  report: IReport;
  allDashboardsList = [];

  isMediumDeviceOrLess: boolean;
  showList: boolean = false;
  listFlexWidth = 30;
  detailsFlexWidth = 70;
  confirmDialogRef: MatDialogRef<ConfirmDialogComponent>;
  isLoading: boolean = true;

  items: IReport[] = [];
  selectedReport: IReport;
  reportUnderAction: IReport;
  searchText: string = "";

  constructor(
    private reportService: ReportService,
    private _snackBar: MatSnackBar,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.report_id = +this.route.snapshot.paramMap.get("id");
    if (this.report_id >= 0) {
      console.log(this.report_id);
      this.reportService.getById(this.report_id).subscribe(report => {
        this.report = report;
      });
    }
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
          this.router.navigate(['reporting/myreports'])
        });
      }
    });
  }

  showMessage(msg: string): void {
    this._snackBar.open(msg, "close", {
      duration: 2000
    });
  }
}
