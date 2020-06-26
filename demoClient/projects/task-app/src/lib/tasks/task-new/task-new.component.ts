import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Globals } from '../../globals';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from "@angular/router";
import { InvolvePeopleComponent } from '../../tasks/task-details/involve-people/involve-people.component'
import { UserService } from '../../common/services/user.service';

@Component({
  selector: 'app-task-new',
  templateUrl: './task-new.component.html',
  styleUrls: ['./task-new.component.scss']
})
export class TaskNewComponent implements OnInit {

  taskForm: FormGroup;
  loading = false;
  submitted = false;

  assignee: any = {};

  addAssigneeDialogRef: MatDialogRef<any>;
  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "50%";
  largerDeviceDialogHeightSize: string = "85%";

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private global: Globals,
    public dialogRef: MatDialogRef<TaskNewComponent>,
    public dialog: MatDialog,
    private userService:UserService
  ) { }

  ngOnInit() {

    this.getUsers();
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
    });

    this.taskForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.maxLength(4000)]
    });

  }

  onCancel(): void {
    this.dialogRef.close(null);
  }

  onSubmit(){
    let task: any = this.taskForm.value;
    task.assignee = this.assignee;
    this.dialogRef.close(task);
  }

  addAssignee(userID) {
    
    // this.addAssigneeDialogRef = this.dialog.open(InvolvePeopleComponent, {
    //   disableClose: true,
    //   height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
    //   width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
    //   maxWidth: "none",
    //   panelClass: 'fc-modal-dialog',
    //   data: {
    //     excludeTaskId: null,
    //     excludeProcessId: null,
    //     tenantId: null,
    //     group: null
    //   }
    // });
    // this.addAssigneeDialogRef.afterClosed().subscribe(user => {
    //   if (user) {
    //     this.assignee = user;
    //   }
    // });

    this.assignee = this.assignee.filter( items => {if(items.id == userID.value) return items; })




  }

  getUsers() {
    this.userService.getFilteredUsers("", null, null, null, null).subscribe((response) => {
          if(response.data.length > 0) this.assignee = response.data;          
    })
  }

}
