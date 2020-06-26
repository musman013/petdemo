import { Component, OnInit, Input, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { Globals } from '../../globals';

import { TranslateService } from '@ngx-translate/core';

import { ProcessService } from '../process.service';
import { CommentService } from '../../common/services/comment.service';
import { TaskService } from '../../tasks/task.service';
import { CommentNewComponent } from '../../tasks/task-details/comment-new/comment-new.component';
import { UserService } from '../../common/services/user.service';

import { ConfirmDialogComponent } from '../../common/components/confirm-dialog/confirm-dialog.component';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-process-details',
  templateUrl: './process-details.component.html',
  styleUrls: ['./process-details.component.scss']
})
export class ProcessDetailsComponent implements OnInit {

  @Input() processInstance: any;
  @Output() onCancelProcess: EventEmitter<any> = new EventEmitter();
  @Output() onCreateProcess: EventEmitter<any> = new EventEmitter();

  account: any = {};
  comments: any = {
    data: []
  };
  commentLoading: boolean;
  processTasks: any[] = [];
  completedProcessTasks: any[] = [];
  commentForm:FormGroup
  dialogRef: MatDialogRef<any>;
  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "50%";
  largerDeviceDialogHeightSize: string = "85%";

  tasksColSpan: number;
  commentsColSpan: number;

  constructor(
    private taskService: TaskService,
    private processService: ProcessService,
    private commentService: CommentService,
    public dialog: MatDialog,
    private global: Globals,
    private router: Router,
    private translateService: TranslateService,
    private userService: UserService,
    private formBuilder:FormBuilder
    ) { 
      this.commentForm = this.formBuilder.group({
        comment: ['', [Validators.required,Validators.maxLength(4000)]]
      });
  


    }

  ngOnInit() {
    this.setCurrentUser();
    if(this.processInstance){
      this.getProcessInstance(this.processInstance.id);
    }
    this.manageScreenResizing();
  }

  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;

      if (this.dialogRef)
        this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);

      if (this.isMediumDeviceOrLess) {
        this.tasksColSpan = 2;
        this.commentsColSpan = 2;
      }
      else {
        this.tasksColSpan = 1;
        this.commentsColSpan = 1;
      }

    });
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      // only run when property "process" changed
      if (propName === 'processInstance') {
        //  update task value when a process is selected
        this.processInstance = changes[propName].currentValue;
        console.log(this.processInstance)
        if(this.processInstance){
          this.getProcessInstance(this.processInstance.id);
        }
      }
    }
  }

  setCurrentUser(){
    this.userService.getAccount().subscribe(account => {
      this.account = account;
    })
  }

  getProcessInstance(processInstanceId) {
    this.processService.get(processInstanceId).subscribe((response) => {
      this.processInstance = response;
      this.loadProcessTasks();
      this.loadComments();
    })
  };

  loadProcessTasks() {

    // Runtime tasks
    this.taskService.getProcessInstanceTasks(this.processInstance.id, false).subscribe((response) => {
      this.processTasks = response.data;
    });

    this.taskService.getProcessInstanceTasks(this.processInstance.id, true).subscribe((response) => {
      if (response.data && response.data.length > 0) {
        this.completedProcessTasks = response.data;
      } else {
        this.completedProcessTasks = [];
      }

      // // Calculate duration
      // for(var i=0; i<response.data.length; i++) {
      //     var task = response.data[i];
      //     if(task.duration) {
      //         task.duration = moment.duration(task.duration).humanize();
      //     }
      // }
    });
  };

  cancelProcessActionResult = (action) => {
    if (action && this.processInstance) {
      this.processService.deleteProcess(this.processInstance.id).
        subscribe((response) => {
          this.onCancelProcess.emit();
        })
    }
  }

  openConfirmMessage(title, message, callback): void {
    this.dialogRef = this.dialog.open(ConfirmDialogComponent, {
      disableClose: true,
      data: {
        title: title,
        message: message
      }
    });
    this.dialogRef.afterClosed().subscribe(action => {
      if (action) {
        callback(action);
      }
    });
  }

  cancelProcess() {
    this.openConfirmMessage(this.translateService.instant('PROCESS.POPUP.CANCEL-TITLE'),this.translateService.instant('PROCESS.POPUP.CANCEL-DESCRIPTION'), this.cancelProcessActionResult)
  };

  deleteProcess() {
    this.openConfirmMessage(this.translateService.instant('PROCESS.POPUP.DELETE-TITLE'),this.translateService.instant('PROCESS.POPUP.DELETE-DESCRIPTION'), this.cancelProcessActionResult)
  };

  loadComments() {
    this.commentService.getProcessInstanceComments(this.processInstance.id).subscribe((data) => {
      this.comments = data;
    });
  };

 


  addComment() {
    this.dialogRef = this.dialog.open(CommentNewComponent, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog'
    });
    this.dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.confirmNewComment(result);
      }
    });
  }


  onSubmitComments(){
    this.confirmNewComment(this.commentForm.value['comment']);
    this.commentForm.controls['comment'].reset();
  }


  confirmNewComment(comment) {
    this.commentLoading = true;
    this.commentService.createProcessInstanceComment(this.processInstance.id, comment.trim())
      .subscribe((comment) => {
        console.log("Comment response:",comment)
        this.commentLoading = false;
        this.loadComments();
      });
  };

  openTask(task) {
    this.taskService.selectedTask = task;
    this.router.navigate(['/task-app/tasks/all']);
  };

  createProcessInstance(){
    this.onCreateProcess.emit();
  }

  openStartForm(){
   
  }

}
