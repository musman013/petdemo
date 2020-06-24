import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef} from '@angular/material';
import { MainService } from '../../../services/main.service';
import { ConfirmDialogComponent } from '../../../../../../projects/fast-code-core/src/lib/common/components/confirm-dialog/confirm-dialog.component';
import { DashboardService } from '../dashboard.service';
import { IDashboard } from '../Idashboard';
import { take } from 'rxjs/operators';
import { ErrorService } from 'projects/fast-code-core/src/public_api';

@Component({
  selector: "app-dashboard-list",
  templateUrl: "./dashboard-list.component.html",
  styleUrls: ["./dashboard-list.component.scss"]
})
export class DashboardListComponent implements OnInit {
  deleteDialogRef: MatDialogRef<ConfirmDialogComponent>;
  dashboards: IDashboard[];
  isLoading: boolean = true;
  displayedColumns: string[] = [
    "Id",
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
    private errorService: ErrorService
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
        this.service.deleteDashboard(dashboard.id);
      }
    });
  }

  changeRecipientSharingStatus(dashboard: IDashboard) {
    this.dashboardService.updateRecipientSharingStatus(dashboard.id, dashboard.recipientSharingStatus)
      .pipe(take(1))
      .subscribe(res => {
        if (res) {
          this.errorService.showError("Status changed");
        } else {
          this.errorService.showError("An error occurred");
        }
      })
  }
}
