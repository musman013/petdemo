import { Component, OnInit,  Output, Inject, EventEmitter ,Input, ChangeDetectorRef } from "@angular/core";
import { ReportPasswordComponent } from '../myreports/report-password/report-password.component';
import { Globals, BaseListComponent, PickerDialogService, ErrorService, ISearchField, operatorType, PickerComponent, listProcessingType } from 'projects/fast-code-core/src/public_api';
import { IPermalink } from '../permalink/ipermalink';
import { PermalinkService } from '../permalink/permalink.service';
import { ChartComponent } from '../../pages/chart/chart.component';
import { MatInputModule, MAT_DIALOG_DATA, MatSnackBar, MatDialog, MatDialogRef, getMatAutocompleteMissingPanelError } from "@angular/material";
import { IReport } from '../reports/Ireport';
import { ClipboardModule  } from 'ngx-clipboard';

export enum AccessOptions {
  Login  = 'Login',
  noLogin = 'noLogin',
  Password = 'Password'
}

@Component({
  selector: "app-permalink",
  templateUrl: "./permalink.component.html",
  styleUrls: ["./permalink.component.scss"]
})

export class PermalinkComponent implements OnInit {

  // @Output() externalShareView: EventEmitter<any> = new EventEmitter();
  selectedReport: IReport;
  // externalShareView: boolean = true;
  accessOption: string;
  accessOptionIcon: string;
  refreshrate: any = 120;
  accessURL: string = "http://localhost:4400/resourceView/";
  embedlink: string = '<iframe frameborder=0 width=`${width}` height=`${height}` src= "http://localhost:4400/resourceView/`${resource.data.id}`"'
  accessOptionTitle: string;
  accessPassword: string;
  permalink: IPermalink;
  passwordDialogRef: MatDialogRef<ReportPasswordComponent>;

  title: any;

  constructor(
    public permalinkservice: PermalinkService,
    public _snackBar: MatSnackBar,
    public dialog: MatDialog,
    public global: Globals,
    public dialogRef: MatDialogRef<ReportPasswordComponent>,
    public permalindialogRef: MatDialogRef<PermalinkComponent>,
    public snackBar: MatSnackBar,
    public errorService: ErrorService,

    @Inject(MAT_DIALOG_DATA) public data: any) {
        this.title = "External Share Options";
        this.selectedReport = data.report;
    }


  ngOnInit() {
    this.createPermalink();

  }

  createPermalink() {
    console.log(this.selectedReport);
    this.permalink = {authentication: 'Login',
      description: false,
      refreshRate: 10,
      rendering: 'interactive',
      height: 600,
      width: 800,
      resource: this.data.type,
      password: null,
      resourceId: this.selectedReport.id,
      toolbar: false};
    this.permalinkservice.create(this.permalink).subscribe(permalink => {
    this.permalink = permalink;
    this.selectAccessOption(this.permalink.authentication);
    console.log(permalink);
    });

  }

  closeExternalShare() {
    // this.externalShareView.emit(false);
  }

  selectAccessOption(opt) {
    if (AccessOptions.Login == opt) {
      this.accessOptionIcon = 'group';
      this.accessOption = opt;
      this.permalink.authentication = opt;
      this.accessOptionTitle = "Access with Login"
    }
    if (AccessOptions.noLogin == opt) {
      this.accessOptionIcon = 'link_off';
      this.accessOption = opt;
      this.permalink.authentication = opt;
      this.accessOptionTitle = 'Access without Login';
    }
    if (AccessOptions.Password == opt) {
      this.accessOptionIcon = 'vpn_key';
      this.accessOption = opt;
      this.permalink.authentication = opt;
      this.accessOptionTitle = 'Access with password';
      if (!this.permalink.password) {
        this.passwordDialogRef = this.dialog.open(ReportPasswordComponent, {
          disableClose: true,
          data: {
            type: 'setPassword'
          }
        });
      }

      this.passwordDialogRef.afterClosed().subscribe(action => {
        if (action.confirm) {
          this.accessPassword = action.password;
          console.log('password set', this.accessPassword  );
        } else {
          this.accessPassword = '';
          console.log('no password set');
        }
      });
    }
  }

  saveAndShareExternally() {
    this.permalink.authentication = this.accessOption;
    // this.permalink.refreshRate = this.refreshrate;
    this.permalink.password = this.accessPassword;
    this.permalink.rendering = 'interactive';
    this.permalink.resourceId = this.selectedReport.id;
    console.log(this.permalink);
    this.permalinkservice.update(this.permalink, this.permalink.id).subscribe(permalink => {
      this.permalink = permalink;
      console.log(permalink);
      this.permalindialogRef.close({confirm : true});
    });

  }

}
