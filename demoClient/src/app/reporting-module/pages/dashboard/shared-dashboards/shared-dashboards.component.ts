import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef} from '@angular/material';
import { MainService } from '../../../services/main.service';
import { DashboardService } from '../dashboard.service';
import { IDashboard } from '../Idashboard';
import { take } from 'rxjs/operators';
import { ErrorService, ConfirmDialogComponent } from 'projects/fast-code-core/src/public_api';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: "app-shared-dashboards",
  templateUrl: "./shared-dashboards.component.html",
  styleUrls: ["./shared-dashboards.component.scss"]
})
export class SharedDashboardsComponent implements OnInit {
  deleteDialogRef: MatDialogRef<ConfirmDialogComponent>;
  dashboards: IDashboard[];
  isLoading: boolean = true;
  displayedColumns: string[] = [
    "Title",
    "Description",
    "Owner",
    "OwnerSharingStatus",
    "RecipientSharingStatus",
    "Actions"
  ];
  constructor(
    private router: Router,
    public dialog: MatDialog,
    public service: MainService,
    public dashboardService: DashboardService,
    private errorService: ErrorService,
    private translate: TranslateService,
    ) {

  }

  ngOnInit() {
    this.dashboardService.getShared("", 0, 1000, "").subscribe(reports => {
      this.isLoading = false;
      this.dashboards = reports;
    })
  }

  dashboardDetails(dashboard: IDashboard) {
    this.router.navigate([`reporting/dashboard/edit/${dashboard.id}` ]);
  }

  deleteDashboard(dashboard: IDashboard){
    this.deleteDialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        confirmationType: "delete"
      }
    });

    this.deleteDialogRef.afterClosed().pipe(take(1)).subscribe(action => {
      if (action) {
        this.dashboardService.delete(dashboard.id);
      }
    });
  }

  changeRecipientSharingStatus(dashboard: IDashboard) {
    this.dashboardService.updateRecipientSharingStatus(dashboard.id, dashboard.recipientSharingStatus)
      .pipe(take(1))
      .subscribe(res => {
        if (res) {
          this.errorService.showError(this.translate.instant('REPORTING.MESSAGES.STATUS-CHANGED'));
        } else {
          this.errorService.showError(this.translate.instant('REPORTING.MESSAGES.ERROR-OCCURRED'));
        }
      })
  }
}
