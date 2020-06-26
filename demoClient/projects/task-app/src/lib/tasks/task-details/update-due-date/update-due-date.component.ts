import { Component, OnInit, Inject } from '@angular/core';

import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-update-due-date',
  templateUrl: './update-due-date.component.html',
  styleUrls: ['./update-due-date.component.scss']
})
export class UpdateDueDateComponent implements OnInit {

  dueDateForm: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<UpdateDueDateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit() {
    console.log(this.data);
    this.dueDateForm = this.formBuilder.group({
      dueDate: [this.data.dueDate ? new Date(this.data.dueDate) : null]
    });
  }

  onCancel(): void {
    this.dialogRef.close({
      clearDueDate: false,
      dueDate: null
    });
  }

  clearDueDate(): void {
    this.dialogRef.close({
      clearDueDate: true,
      dueDate: null
    });
  }

  onSubmit() {
    this.dialogRef.close(
      {
        dueDate: this.dueDateForm.value['dueDate'],
        clearDueDate: false
      }
    );
  }

  dueToday() {
    this.dialogRef.close(
      {
        dueDate: new Date(),
        clearDueDate: false
      }
    );
  }

}
