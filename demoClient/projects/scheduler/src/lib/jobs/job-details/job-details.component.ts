import { Component, OnInit } from '@angular/core';
import { JobService } from '../job.service';
import { Job } from '../ijob';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map, startWith, first } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { JobData } from '../jobData';
import { Trigger } from '../../triggers/trigger';
import { ExecutionHistory } from '../../execution-history/executionHistory';
import { plainToClass } from "class-transformer";

@Component({
  selector: 'app-job-details',
  templateUrl: './job-details.component.html',
  styleUrls: ['./job-details.component.scss']
})
export class JobDetailsComponent implements OnInit {

  errorMessage = '';
  job = new Job();
  jobForm: FormGroup;
  loading = false;
  submitted = false;

  filteredOptions: Observable<string[]>;
  options: string[];

  jobClasses: string[];


  // table data for jobData
  ELEMENT_DATA: JobData[] = [];
  displayedColumns: string[] = ['position', 'name', 'actions'];
  dataSourceJobData = of(this.ELEMENT_DATA);

  // table data for triggers
  displayedColumnsTriggers: string[] = ['triggerName', 'triggerGroup', 'type', 'startTime', 'endTime', 'lastExecutionTime', 'nextExecutionTime']
  triggers: Trigger[] = [];
  dataSourceTriggers = of(this.triggers)

  // table data for triggers
  displayedColumnsExecutionHistory: string[] = ['triggerName', 'triggerGroup', 'status', 'duration', 'fireTime', 'finishedTime']
  // displayedColumnsExecutionHistory: string[] = ['triggerName', 'triggerGroup']
  executionHistory: ExecutionHistory[] = [];
  dataSourceExecutionHistory = of(this.executionHistory)
  jobNameParam;
  jobGroupParam;
  constructor(private formBuilder: FormBuilder, private route: ActivatedRoute,
    private router: Router,
    private jobService: JobService) {
    this.jobNameParam = this.route.snapshot.paramMap.get('jobName');
    this.jobGroupParam = this.route.snapshot.paramMap.get('jobGroup');
  }

  ngOnInit() {

    console.log(this.jobNameParam)
    console.log(this.jobGroupParam)
    this.jobForm = this.formBuilder.group({
      jobName: ['', Validators.required],
      jobGroup: ['', Validators.required],
      jobClass: ['', Validators.required],
      isDurable: [false],
      jobDescription: [''],

    });
    if (this.jobNameParam && this.jobGroupParam) {
      this.getJob(this.jobNameParam, this.jobGroupParam);
    }


    this.jobService.getJobGroups().subscribe(
      groups => {
        console.log(groups);
        this.options = groups;
        this.filteredOptions = this.jobForm.get('jobGroup').valueChanges
          .pipe(startWith(''),
            map(value => this._filter(value))
          );
      }
    );

    this.jobService.getJobClasses().subscribe(
      jobClasses => {
        console.log(jobClasses);
        this.jobClasses = jobClasses;
      }
    );


  }

  getJob(jobName: string, jobGroup: string) {
    this.jobService.get(jobName, jobGroup).subscribe(
      job => {
        this.job = job;
        this.jobForm.patchValue({
          id: job.id,
          jobName: job.jobName,
          jobGroup: job.jobGroup,
          jobClass: job.jobClass,
          jobDescription: job.jobDescription,
          jobMapData: job.jobMapData,
          triggers: job.triggers,
          executionHistory: job.executionHistory
        });
        // setting jobMapData 
        this.setJobData();

        this.triggers = this.job.triggers;
        this.dataSourceTriggers = of(this.triggers);


        this.executionHistory = this.job.executionHistory;
        this.dataSourceExecutionHistory = of(this.executionHistory);

      },
      error => this.errorMessage = <any>error);
  }

  private setJobData() {
    let jobMapData = this.job["jobMapData"]
    if (jobMapData) {
      let jobDataKeys = Object.keys(jobMapData);
      jobDataKeys.forEach((key) => {
        this.ELEMENT_DATA.push(plainToClass(JobData, {
          dataKey: key,
          dataValue: jobMapData[key]
        } as Object))
      })

      this.dataSourceJobData = of(this.ELEMENT_DATA);
    }

  }

  private _filter(value: string): string[] {
    if (!value)
      value = "";
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  addJobData(): void {
    this.ELEMENT_DATA.push(plainToClass(JobData, {
      "dataKey": "",
      "dataValue": ""
    }));
    this.dataSourceJobData = of(this.ELEMENT_DATA);
  }

  removeJobData(index): void {
    this.ELEMENT_DATA.splice(index, 1);
    this.dataSourceJobData = of(this.ELEMENT_DATA);
  }

  compareFn: ((f1: any, f2: any) => boolean) | null = this.compareByValue;

  compareByValue(f1: any, f2: any) {
    return f1 && f2 && f1 === f2;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.jobForm.invalid) {
      return;
    }

    this.loading = true;
    let newJob = {};
    newJob = this.jobForm.value;
    newJob["jobMapData"] = {};
    this.ELEMENT_DATA.forEach(function (obj) {
      let tmp = {};
      tmp[obj.dataKey] = obj.dataValue;
      newJob["jobMapData"][obj.dataKey] = obj.dataValue;
    })
    console.log(newJob)
    this.jobService.update(newJob, newJob["jobName"], newJob["jobGroup"])
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['/jobs'])
        },
        error => {
          this.loading = false;
        });
  }

  back() {
    this.router.navigate(['/jobs'])
  }

}
