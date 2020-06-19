import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals } from '../../globals';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { TriggerService } from '../trigger.service';
import { ActivatedRoute, Router } from "@angular/router";
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { SelectJobComponent } from '../select-job/select-job.component';
import { Trigger } from '../trigger';
import { JobData } from '../../jobs/jobData';
import { ExecutionHistory } from '../../execution-history/executionHistory';
import { plainToClass } from "class-transformer";

@Component({
  selector: 'app-trigger-details',
  templateUrl: './trigger-details.component.html',
  styleUrls: ['./trigger-details.component.scss']
})
export class TriggerDetailsComponent implements OnInit {

  trigger = new Trigger();
  selectJobDialogRef: MatDialogRef<any>;

  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "80%";
  largerDeviceDialogWidthSize: string = "65%";
  largerDeviceDialogHeightSize: string = "65%";

  errorMessage = '';
  triggerForm: FormGroup;
  loading = false;
  submitted = false;

  triggerTypes: string[] = ['Simple', 'Cron'];

  // table data for jobMapData
  ELEMENT_DATA: JobData[] = [];
  displayedColumns: string[] = ['position', 'name', 'actions'];
  dataSourceJobData = of(this.ELEMENT_DATA);
  triggerNameParam = this.route.snapshot.paramMap.get('triggerName');
  triggerGroupParam = this.route.snapshot.paramMap.get('triggerGroup');
  // table data for triggers
  displayedColumnsExecutionHistory: string[] = ['triggerName', 'triggerGroup', 'status', 'duration', 'fireTime', 'finishedTime']
  // displayedColumnsExecutionHistory: string[] = ['triggerName', 'triggerGroup']
  executionHistory: ExecutionHistory[] = [];
  dataSourceExecutionHistory = of(this.executionHistory)

  options: string[] = ['group1', 'sample group2', 'g3'];
  filteredOptions: Observable<string[]>;
  constructor(private formBuilder: FormBuilder, private router: Router,
    private global: Globals, private triggerService: TriggerService,
    private route: ActivatedRoute,
    public dialog: MatDialog, private changeDetectorRefs: ChangeDetectorRef
  ) { }

  ngOnInit() {


    console.log(this.triggerNameParam)
    console.log(this.triggerGroupParam)
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
      if (this.selectJobDialogRef)
        this.selectJobDialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);

    });

    this.triggerForm = this.formBuilder.group({
      jobName: [{ value: '' }, Validators.required],
      jobGroup: [{ value: '' }, Validators.required],
      triggerName: ['', Validators.required],
      triggerGroup: ['', Validators.required],
      triggerType: ['', Validators.required],
      startDate: [''],
      startTime: ['',],
      endDate: [''],
      endTime: [''],
      lastExecutionTime:[''],
      nextExecutionTime:[''],
      cronExpression: [''],
      repeatInterval: ['', Validators.required],
      repeatIndefinitely: [''],
      repeatCount: [''],
      description: [''],
    });
    if (this.triggerNameParam && this.triggerGroupParam) {
      this.getTrigger(this.triggerNameParam, this.triggerGroupParam);
    }

    this.filteredOptions = this.triggerForm.get('jobGroup').valueChanges
      .pipe(startWith(''),
        map(value => this._filter(value))
      );

  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  getTrigger(triggerName: string, triggerGroup: string) {
    this.triggerService.get(triggerName,triggerGroup).subscribe(
      trigger => {
        if (!trigger)
          console.log("null")
        this.trigger = trigger;
        this.triggerForm.patchValue({
          id: trigger.id,
          jobName: trigger.jobName,
          jobGroup: trigger.jobGroup,
          jobMapData: trigger.jobMapData,
          triggerName: trigger.triggerName,
          triggerGroup: trigger.triggerGroup,
          triggerType: trigger.triggerType,
          startTime: this.formatDateStringToAMPM(trigger.startTime),
          startDate: trigger.startTime ? new Date(trigger.startTime) : null,
          endTime: this.formatDateStringToAMPM(trigger.endTime),
          endDate: trigger.endTime ? new Date(trigger.endTime) : null,
          lastExecutionTime: trigger.lastExecutionTime? new Date(trigger.lastExecutionTime):null,
          nextExecutionTime: trigger.nextExecutionTime? new Date(trigger.nextExecutionTime):null,
          cronExpression: trigger.cronExpression,
          repeatInterval: trigger.repeatInterval,
          repeatIndefinitely: trigger.repeatIndefinitely,
          repeatCount: trigger.repeatCount,
          description: [''],
          executionHistory: trigger.executionHistory
        });

        console.log(this.trigger)
        // setting jobMapData 
        this.setJobData();

        this.executionHistory = this.trigger.executionHistory;
        this.dataSourceExecutionHistory = of(this.executionHistory);

      },
      error => this.errorMessage = <any>error);
  }


  formatDateStringToAMPM(d) {
    console.log(d);
    if (d) {
      var date = new Date(d);
      var hours = date.getHours();
      var minutes = date.getMinutes();
      var ampm = hours >= 12 ? 'pm' : 'am';
      hours = hours % 12;
      hours = hours ? hours : 12; // the hour '0' should be '12'
      var minutes_str = minutes < 10 ? '0' + minutes : minutes;
      var strTime = hours + ':' + minutes_str + ' ' + ampm;
      return strTime;
    }
    return null;
  }

  private setJobData() {
    let jobMapData = this.trigger["jobMapData"]
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

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.triggerForm.invalid) {
      return;
    }

    this.loading = true;
    console.log(this.triggerForm.value)
    let newTrigger = {};
    newTrigger = this.triggerForm.value;
    newTrigger["jobMapData"] = {};
    this.ELEMENT_DATA.forEach(function (obj) {
      let tmp = {};
      tmp[obj.dataKey] = obj.dataValue;
      newTrigger["jobMapData"][obj.dataKey] = obj.dataValue;
    })
    this.triggerService.create(newTrigger)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['/triggers']);
        },
        error => {
          this.loading = false;
        });
  }

  onCancel(){
    this.router.navigate(['/triggers']);
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

}
