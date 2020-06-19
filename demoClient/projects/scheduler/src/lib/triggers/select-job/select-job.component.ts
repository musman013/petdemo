import { Component, OnInit } from '@angular/core';
import { MatDialogRef,MatDialog } from '@angular/material/dialog';
import { JobService } from '../../jobs/job.service';
import { IJob } from '../../jobs/ijob';

@Component({
  selector: 'app-select-job',
  templateUrl: './select-job.component.html',
  styleUrls: ['./select-job.component.scss']
})
export class SelectJobComponent implements OnInit {
  
  displayedColumns: string[] = ['jobName', 'jobGroup', 'jobClass'];
  jobs: IJob[] = [];
  errorMessage = '';

  constructor(private jobService:JobService, public dialogRef: MatDialogRef<SelectJobComponent>) { }

  ngOnInit() {
    this.jobService.getAll(null,null,null,null).subscribe(
      perms => {
        this.jobs = perms;
      },
      error => this.errorMessage = <any>error
    );
  }

  selectJob(job):void{
    this.dialogRef.close(job);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
