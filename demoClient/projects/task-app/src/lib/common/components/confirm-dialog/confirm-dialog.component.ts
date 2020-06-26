import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';


@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    private translateService: TranslateService) { }

  public confirmMessage: string;
  public confirmTitle: string;
  ngOnInit() {
    this.confirmMessage = this.data.message;
    this.confirmTitle = this.data.title ? this.data.title : this.translateService.instant('GENERAL.ACTION.CONFIRM');
  }
}

