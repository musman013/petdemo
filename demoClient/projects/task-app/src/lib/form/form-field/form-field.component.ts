import { Component, OnInit, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialogRef, MatDialog } from '@angular/material';

import { Globals } from '../../globals';
import { InvolvePeopleComponent } from '../../tasks/task-details/involve-people/involve-people.component';
import { RelatedContentService } from '../../common/services/related-content.service';
import { SelectGroupComponent } from '../../common/components/select-group/select-group.component';



@Component({
  selector: 'app-form-field',
  templateUrl: './form-field.component.html',
  styleUrls: ['./form-field.component.scss']
})
export class FormFieldComponent implements OnInit {
  @Input() field: any;
  @Input() parentForm: FormGroup;

  uploads: any[] = [];

  dialogRef: MatDialogRef<any>;
  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "50%";
  largerDeviceDialogHeightSize: string = "85%";

  constructor(
    private global: Globals,
    public dialog: MatDialog,
    public relatedContentService: RelatedContentService
  ) {};

  ngOnInit() {
    this.prepareField();
  };

  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;

      if (this.dialogRef)
        this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);

    });
  };

  selectPeople() {
    this.dialogRef = this.dialog.open(InvolvePeopleComponent, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog',
      data: {
        excludeTaskId: null,
        excludeProcessId: null,
        tenantId: null,
        group: null
      }
    });
    this.dialogRef.afterClosed().subscribe(user => {
      if (user) {
        this.field.value = user;
        this.parentForm.get(this.field.id).setValue(user);
      }
    });
  };

  selectGroup() {
    this.dialogRef = this.dialog.open(SelectGroupComponent, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog',
      data: {
        tenantId: null,
        groupId: null
      }
    });
    this.dialogRef.afterClosed().subscribe(group => {
      if (group) {
        this.field.value = group;
        this.parentForm.get(this.field.id).setValue(group);
      }
    });
  };

  fieldGroupRemoved() {
    this.field.value = null;
    this.parentForm.get(this.field.id).setValue(null);
    console.log(this.parentForm.value);
  };

  fieldPersonRemoved() {
    this.field.value = undefined;
  };

  prepareField() {
    if (this.field.type == 'upload' && this.field.value) {
      var newUploadValue = '';
      this.field.value.forEach(value => {
        this.uploads.push(value);
        if (newUploadValue.length > 0) {
          newUploadValue += ',';
        }
        newUploadValue += value.id;
        this.parentForm.get(this.field.id).setValue(newUploadValue);
      });
    }
  }

  onContentUploaded(content) {
    this.uploads.push(content);
    this.updateContentValue();
  };

  updateContentValue() {
    console.log(this.uploads)
    if (!this.uploads || this.uploads.length == 0) {
      this.parentForm.get(this.field.id).setValue(undefined);
    } else {
      var newValue = '';
      this.uploads.forEach((upload, index) => {
        if (index > 0) {
          newValue += ',';
        }
        newValue += upload.id;
        this.parentForm.get(this.field.id).setValue(newValue);
      });
    }
  };

  fileChange(event) {
    let fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      let file: File = fileList[0];
      this.relatedContentService.addRelatedContent(null, null, null, file, null).subscribe((result) => {
        if (result)
          this.onContentUploaded(result);
      })
    }
  }

  removeContent(content) {
    this.uploads = this.uploads.filter(obj => {
      return obj.id !== content.id;
    });
    this.updateContentValue();
  }

}
