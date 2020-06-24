import { Component, Inject } from '@angular/core';
import { MatSnackBar, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-report-password',
  templateUrl: 'report-password.component.html',
  styleUrls: ['report-password.component.scss']
})

export class ReportPasswordComponent {

  reportpassword: any;
  confirmreportpassword: any = '';
  confirmation: any;
  disableOk = true;
  title: string;
  constructor(
    public dialogRef: MatDialogRef<ReportPasswordComponent>,
    public snackBar: MatSnackBar,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) public data: any) {
      if (this.data.type == 'Password') {
        this.title = "Password";
      } else {
        this.title = "Set Password";
      }
    }

  confirm() {
      this.dialogRef.close({ 'password': this.reportpassword , 'confirm': true });
  }

  confirmPassword(value: string): void {
    if (this.data.type == 'setPassword') {
      console.log(value);
      if (value == this.reportpassword) {
        this.confirmation = true;
        this.disableOk = false;
      } else {
        this.disableOk = true;
        this.confirmation = false;
      }
    } else {
      this.disableOk = false;
    }
  }

}
