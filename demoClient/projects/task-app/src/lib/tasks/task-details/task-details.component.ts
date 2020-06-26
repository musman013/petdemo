import { Component, OnInit, Input, OnChanges, SimpleChanges, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material';
import {MatAutocompleteSelectedEvent, MatAutocomplete} from '@angular/material/autocomplete';
import { Globals } from '../../globals';
import {MatChipInputEvent} from '@angular/material/chips';
import { TaskService } from '../task.service';
import { FormService } from '../../form/form.service';
import { ProcessService } from '../../processes/process.service';
import { CommentService } from '../../common/services/comment.service';
import { UserService } from '../../common/services/user.service';
import { RelatedContentService } from '../../common/services/related-content.service';
import { MatSelectionList, MatListOption } from '@angular/material';
import { CommentNewComponent } from './comment-new/comment-new.component';
import { InvolvePeopleComponent } from './involve-people/involve-people.component';
import { TaskNewComponent } from '../../tasks/task-new/task-new.component';
import { UpdateDueDateComponent } from './update-due-date/update-due-date.component';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import { IInvolvePeopleDialogConfig } from './involve-people/involve-people-dialog-config';




@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.scss']
})
export class TaskDetailsComponent implements OnInit, OnChanges {

  @Input() task: any;
  @Output() onTaskCompletion: EventEmitter<any> = new EventEmitter();
  @Output() onOpenTask: EventEmitter<any> = new EventEmitter();
  @Output() onCreateTask: EventEmitter<any> = new EventEmitter();
  @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;
  @ViewChild('fileInput', { read: ElementRef, static: false }) fileInput: ElementRef;
  @ViewChild('assigneeInput', { read: ElementRef, static: false }) assigneeInput: ElementRef;
  @ViewChild(MatSelectionList, { static: true }) selectionList: MatSelectionList;
  assigneLIst = new FormControl();
  formData: any;
  assignee:any;
  commentForm:FormGroup;
  account: any = {};
  processInstance: any = {};
  involvementSummary: any = {};
  contentSummary: any = {
    loading: false
  };
  content: any = {};
  commentSummary: any = {
    loading: true
  };
  comments: any = {};
  subTasks: any;
  subTaskSummary: any = {
    loading: false
  };
  hasNextPage: boolean;
  activeTab = 'form';
  taskUpdating: boolean = true;
  textLimit:[];
  commentsColSpan: number;
  detailsColSpan: number;

  dialogRef: MatDialogRef<any>;
  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "50%";
  largerDeviceDialogHeightSize: string = "85%";
  taskType:string='pending';
  completeButtonDisabled: boolean = false;
  uploadInProgress: boolean = false;
  users: any[] = [];
  constructor(
    private taskService: TaskService,
    private processService: ProcessService,
    private commentService: CommentService,
    public dialog: MatDialog,
    private global: Globals,
    private userService: UserService,
    private relatedContentService: RelatedContentService,
    private formService: FormService,
    private formBuilder: FormBuilder,
    // public data: IInvolvePeopleDialogConfig
  ) { 


    this.commentForm = this.formBuilder.group({
      comment: ['', [Validators.required,Validators.maxLength(4000)]]
    });

  }

  ngOnInit() {
  
    this.setCurrentUser();
    // this.loadAllDetails();
    if (this.task && this.task.id){
      this.getTask(this.task.id);     
    
    }
    this.manageScreenResizing();   


  }

  getUsers(excludeTaskId,excludeProcessId, tenantId,group) {
    let excludePeopleIds: string[];
    this.userService.getFilteredUsers("", excludeTaskId, excludeProcessId, tenantId, group).subscribe((response) => {
     if (response.start === 0) {
        if (excludePeopleIds && excludePeopleIds.length > 0)
          this.users = response.data.filter(
            user => excludePeopleIds.indexOf(user.id) == -1);
        else
          this.users = response.data;
      }
      else {
        if (excludePeopleIds && excludePeopleIds.length > 0)
          this.users = this.users.concat(response.data.filter(
            user => excludePeopleIds.indexOf(user.id) == -1));
        else
          this.users = this.users.concat(response.data);
      }
      this.hasNextPage = (response.start + response.size < response.total);
    })
  }


  onSelect() {
    let selectedOption = null;
    if (this.selectionList.selectedOptions.selected.length > 0)
      selectedOption = this.selectionList.selectedOptions.selected[0].value;
    // this.dialogRef.close(selectedOption);

  }
  selectAssinee(user){
    if (user) {
      this.taskService.assignTask(this.task.id, user.id).subscribe((data) => {
        this.task = data;
      });
    }
  }

  searchList:boolean =false;
  onSearch(searchValue: string) {
    if(searchValue !==''){
      this.searchList = true;
    }
    this.userService.getFilteredUsers(searchValue, this.task.id, this.task.processInstanceId,null,null).subscribe((response) => {
      console.log('Search User : ',searchValue, response);
      if (response.start === 0) {
        this.users = response.data;
      }
      else {
        this.users = this.users.concat(response.data);
      }
      this.hasNextPage = (response.start + response.size < response.total);
    })
  }


  add(event: MatChipInputEvent): void {
    // Add fruit only when MatAutocomplete is not open
    // To make sure this does not conflict with OptionSelected Event
    if (!this.matAutocomplete.isOpen) {
      const input = event.input;
      const value = event.value;

      // Add our fruit
      if ((value || '').trim()) {
        this.assignee.push(value.trim());
      }

      // Reset the input value
      if (input) {
        input.value = '';
      }

      this.assigneLIst.setValue(null);
    }
  }
  removeAssignee(fruit: string): void {
    const index = this.assignee.indexOf(fruit);

    if (index >= 0) {
      this.assignee.splice(index, 1);
    }
  }
  selected(event: MatAutocompleteSelectedEvent): void {
    this.assignee.push(event.option.viewValue);
    this.assigneeInput.nativeElement.value = '';
    this.assigneLIst.setValue(null);

  }








  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;

      if (this.dialogRef)
        this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);

      if (this.isMediumDeviceOrLess) {
        this.detailsColSpan = 2;
        this.commentsColSpan = 2;
      }
      else {
        this.detailsColSpan = 1;
        this.commentsColSpan = 1;
      }

    });
  }

  ngOnChanges(changes: SimpleChanges) {
    this.ngOnInit();
    for (let propName in changes) {
      // only run when property "task" changed
      if (propName === 'task') {
        //  update task value when a task is selected 
        this.task = changes[propName].currentValue;
        // this.loadAllDetails();
        if (this.task){ 
           this.checkStatus(this.task);
          this.getTask(this.task.id);
          this.getUsers(this.task.id, null,null,null);
          // this.involvePerson(this.task.assignee)
        }
      }
    }
    console.log('Current task :', this.task);
  }


  checkStatus(data){
   let currentDate = new Date()
    let dueDate = null
    if(data.dueDate != null){
       dueDate = new Date(data.dueDate);
     }
   
    if(data.endDate == null && data.dueDate == null){
      this.taskType = 'pending';      
    }else if(data.dueDate == null){
      this.taskType = 'process';
    }else if(data.dueDate > data.endDate || dueDate < currentDate){
      this.taskType = 'due'
    }else if(data.endDate && data.duration){
        this.taskType = 'completed';
    }else{
        this.taskType = 'process';
    }

  }



  setCurrentUser() {
    this.userService.getAccount().subscribe(account => {
      this.account = account;
    })
  }

  
  loadAllDetails() {    
    this.getProcessInstance();
    this.refreshInvolvmentSummary();
    this.loadRelatedContent();
    this.loadComments();
    this.loadSubTasks();
    if (this.task.formKey) {
      this.loadForm();
      this.activeTab = 'form';
    }
    else
      this.activeTab = 'details';
  }

  loadForm() {
    if (this.task.formKey !== null && this.task.formKey !== undefined) {
      this.formService.getTaskForm(this.task.id).subscribe((formData) => {
        this.formData = formData;
      });
    } else {
      this.formData = undefined;
    }
  }

  getProcessInstance() {
    if (this.task.processInstanceId) {
      this.processService.get(this.task.processInstanceId).subscribe(
        process => {
          this.processInstance = process;
        }
      )
    }
  }

  refreshInvolvmentSummary() {
    if (this.task) {
      var newValue = this.task.involvedPeople;
      this.involvementSummary.loading = false;
      if (newValue && newValue.length > 0) {
        this.involvementSummary.count = newValue.length;

        if (newValue > 8) {
          this.involvementSummary.overflow = true;
          this.involvementSummary.items = [];

          for (var i = 0; i < 8; i++) {
            this.involvementSummary.items.push(newValue[i]);
          }
        } else {
          this.involvementSummary.overflow = false;
          this.involvementSummary.items = newValue;
        }

      } else {
        this.involvementSummary.count = 0;
        this.involvementSummary.items = [];
        this.involvementSummary.overflow = false;
      }
    }
  };

  loadRelatedContent() {
    this.taskService.getRelatedContent(this.task.id).subscribe((data) => {
      this.content = data;
      this.refreshContentSummary();
    });
  };

  refreshContentSummary() {
    if (this.task) {
      this.contentSummary.loading = false;
      if (this.content.data && this.content.data.length > 0) {
        this.contentSummary.count = this.content.data.length;

        if (this.content.data.length > 8) {
          this.contentSummary.overflow = true;
          this.contentSummary.items = [];

          for (var i = 0; i < 8; i++) {
            this.contentSummary.items.push(this.content.data[i]);
          }
        } else {
          this.contentSummary.overflow = false;
          this.contentSummary.items = this.content.data;
        }

      } else {
        this.contentSummary.items = [];
        this.contentSummary.count = 0;
        this.contentSummary.overflow = false;
      }
    }
  };

  loadComments() {
    this.commentService.getTaskComments(this.task.id).subscribe((data) => {
      this.comments = data;
      this.refreshCommentSummary();
    });
  };

  refreshCommentSummary() {
    if (this.task) {
      var newValue = this.comments ? this.comments.data : undefined;
      this.commentSummary.loading = false;

      if (newValue) {
        this.commentSummary.count = newValue.length;
      } else {
        this.commentSummary.loading = true;
        this.commentSummary.count = undefined;
      }
    }
  };

  loadSubTasks() {
    this.taskService.getSubTasks(this.task.id).subscribe((data) => {
      this.subTasks = data;

      this.refreshSubTaskSummary();
    });
  };

  refreshSubTaskSummary() {
    if (this.task) {
      var newValue = this.subTasks ? this.subTasks : undefined;
      this.subTaskSummary.loading = false;

      if (newValue) {
        this.subTaskSummary.count = newValue.length;
      } else {
        this.subTaskSummary.loading = true;
        this.subTaskSummary.count = undefined;
      }
    }
  };

  toggleForm() {
    if (this.activeTab == 'form') {
      this.activeTab = 'details';
    } else {
      this.activeTab = 'form';
    }
  };

  getTask(taskId) {
    this.taskService.get(taskId).subscribe((response) => {
      this.task = response;
      this.loadAllDetails();
      this.taskUpdating = false;
    })
  };

  showDetails() {
    this.activeTab = 'details';
  };

  removeInvolvedUser(user, index) {

    this.taskService.removeInvolvedUserInTask(user, this.task.id).subscribe(() => {
      this.task.involvedPeople.splice(index, 1);;
    });
  };

  hasDetails = function () {

    if (this.loading == true
      || (this.involvementSummary === null || this.involvementSummary === undefined || this.involvementSummary.loading === true)
      || (this.contentSummary === null || this.contentSummary === undefined || this.contentSummary.loading === true)
      || (this.commentSummary === null || this.commentSummary === undefined || this.commentSummary.loading === true)
      || (this.subTaskSummary === null || this.subTaskSummary === undefined || this.subTaskSummary.loading === true)) {
      return false;
    }

    if (this.task !== null && this.task !== undefined) {

      // Returning true by default, or the screen will flicker until all the data (people/comments/content) have been fetched
      var hasPeople = false;
      var hasContent = false;
      var hasComments = false;
      var hasSubTasks = false;

      // Involved people
      if (this.task.involvedPeople !== null
        && this.task.involvedPeople !== undefined
        && this.task.involvedPeople.length > 0) {
        hasPeople = true;
      }

      // Content
      if (this.content !== null
        && this.content !== undefined
        && this.content.data
        && this.content.data.length > 0) {
        hasContent = true;
      }

      // Comments
      if (this.comments !== null
        && this.comments !== undefined
        && this.comments.data.length > 0) {
        hasComments = true;
      }

      // SubTasks
      if (this.subTasks !== null
        && this.subTasks !== undefined
        && this.subTasks.length > 0) {
        hasSubTasks = true;
      }

      return hasPeople || hasContent || hasComments || hasSubTasks;

    }
    return false;
  };

  toggleCreateComment() {
    if (this.commentSummary.addComment) {
      this.commentSummary.newComment = undefined;
    }

    this.commentSummary.addComment = !this.commentSummary.addComment;

    // if (this.commentSummary.addComment) {
    //   $timeout(function () {
    //     angular.element('.focusable').focus();
    //   }, 100);

    // }
  };

  confirmNewComment(comment) {
    this.commentSummary.loading = true;
    this.commentService.createTaskComment(this.task.id, comment.trim())
      .subscribe((comment) => {
        this.commentSummary.loading = false;
        // $rootScope.addAlertPromise($translate('TASK.ALERT.COMMENT-ADDED', this.task));
        this.loadComments();
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

  involvePerson(user) {

    // this.dialogRef = this.dialog.open(InvolvePeopleComponent, {
    //   disableClose: true,
    //   height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
    //   width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
    //   maxWidth: "none",
    //   panelClass: 'fc-modal-dialog',
    //   data: {
    //     excludeTaskId: this.task.id,
    //     excludeProcessId: null,
    //     tenantId: null,
    //     group: null
    //   }
    // });
    // this.dialogRef.afterClosed().subscribe(user => {
      if (user) {
        this.taskService.involveUserInTask(user.id, this.task.id).subscribe((response) => {
           console.log("shrikant =====================================>",this.task, user, response);
          if (!this.task.involvedPeople) {
            this.task.involvedPeople = [user];
          } else {
            this.task.involvedPeople.push(user);
          }
        })
      }
    // });
  }

  createSubTask() {

    this.dialogRef = this.dialog.open(TaskNewComponent, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog'
    });
    this.dialogRef.afterClosed().subscribe(task => {
      this.confirmTaskCreation(task)
    });
  }

  confirmTaskCreation(newTask) {
    if (newTask && newTask.name) {
      var taskData: any = {
        name: newTask.name,
        description: newTask.description,
        assignee: newTask.assignee ? newTask.assignee.id : null
      };

      taskData.parentTaskId = '' + this.task.id

      // if (this.activeAppDefinition) {
      //     taskData.category = '' + $rootScope.activeAppDefinition.id;
      // }

      newTask.loading = true;
      this.taskService.createTask(taskData).subscribe((task) => {
        newTask.loading = false;
      });
    }
  };

  toggleCreateContent() {
    let event = new MouseEvent('click', { bubbles: false });
    this.fileInput.nativeElement.dispatchEvent(event);
  }

  onContentUploaded(content) {
    this.uploadInProgress = false;
    if (this.content && this.content.data) {
      this.content.data.push(content);
      this.relatedContentService.addUrlToContent(content);
    }
  };

  onContentDeleted(content) {
    if (this.content && this.content.data) {
      this.content.data.forEach(function (value, i, arr) {
        if (content === value) {
          arr.splice(i, 1);
        }
      })
    }
  };

  fileChange(event) {
    let fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      let file: File = fileList[0];
      this.uploadInProgress = true;
      this.relatedContentService.addRelatedContent(this.task.id, null, null, file, null).subscribe((result) => {
        if (result)
          this.onContentUploaded(result);

      })
    }
  }

  openTaskInstance(task) {
    if (task.assignee && (task.assignee.id == this.account.id)) {
      this.taskService.get(task.id).subscribe((response) => {
        this.task = response;
        this.loadAllDetails();
        this.onOpenTask.emit(this.task);
      })
    }
  }

  completeTask() {
    this.completeButtonDisabled = true;
    this.taskService.completeTask(this.task.id).subscribe((response) => {
      this.emitOnTaskCompletion();
    });
  };

  emitOnTaskCompletion() {
    this.onTaskCompletion.emit();
  }

  updateDueDate(val) {

    let data={
      dueDate:val
    }
    // this.dialogRef = this.dialog.open(UpdateDueDateComponent, {
    //   disableClose: true,
    //   height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
    //   width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
    //   maxWidth: "none",
    //   panelClass: 'fc-modal-dialog',
    //   data: {
    //     dueDate: this.task.dueDate
    //   }
    // });
    // this.dialogRef.afterClosed().subscribe(data => {
    //   if (data.clearDueDate || data.dueDate) {
    //     var updateData = {
    //       dueDate: null
    //     }
    //     this.task.dueDate = null;
    //     if (!data.clearDueDate) {
    //       let d = new Date(data.dueDate)
    //       d.setHours(23);
    //       d.setMinutes(59);
    //       d.setSeconds(59);

    //       updateData.dueDate = d;
    //     }

        this.taskService.updateTask(this.task.id, data).subscribe(response => {
          if (response)
            this.task.dueDate = response.dueDate;
        })
    //   }
    // });
  }

  openSetTaskAssigneeDialog() {
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
        group: null,
        excludePeopleIds: this.task.assignee ? [this.task.assignee.id] : []
      }
    });
    this.dialogRef.afterClosed().subscribe(user => {
      if (user) {
        this.taskService.assignTask(this.task.id, user.id).subscribe((data) => {
          this.task = data;
        });
      }
    })
  }

  createTask() {
    this.onCreateTask.emit();
  }

  claimTask = function () {
    this.loading = true;
    this.claimButtonDisabled = true;
    this.taskService.claimTask(this.task.id).subscribe((data) => {
      // Refetch data on claim success
      this.getTask(this.task.id);
    });
  };

  openProcessInstance(processId) {

  }
}