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


@Component({
  selector: 'app-trigger-new',
  templateUrl: './trigger-new.component.html',
  styleUrls: ['./trigger-new.component.scss']
})
export class TriggerNewComponent implements OnInit {

  selectJobDialogRef: MatDialogRef<any>;

  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "65%";
  largerDeviceDialogHeightSize: string = "75%";

  triggerForm: FormGroup;
  loading = false;
  submitted = false;
  ELEMENT_DATA = [
  ];

  triggerTypes: string[] = ['Simple', 'Cron'];

  displayedColumns: string[] = ['position', 'name', 'actions'];
  dataSource = of(this.ELEMENT_DATA);

  options: string[] = [];
  filteredOptions: Observable<string[]>;
  constructor(private formBuilder: FormBuilder, private router: Router,
    private global: Globals, private triggerService: TriggerService,
    private route: ActivatedRoute, public dialogRef: MatDialogRef<TriggerNewComponent>,
    public dialog: MatDialog, private changeDetectorRefs: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.manageScreenResizing();
    this.createForm();
    this.getTriggerGroups();
  }
  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  createForm() {
    this.triggerForm = this.formBuilder.group({
      jobName: [{ value: '', disabled: true }, Validators.required],
      jobGroup: [{ value: '', disabled: true }, Validators.required],
      // jobClass: [{ value: '', disabled: true }, Validators.required],
      triggerName: ['', Validators.required],
      triggerGroup: ['', Validators.required],
      triggerType: ['', Validators.required],
      startDate: [''],
      startTime: ['',],
      endDate: [''],
      endTime: [''],
      cronExpression: [''],
      repeatInterval: ['', Validators.required],
      repeatIndefinite: [''],
      repeatCount: [''],
      description: [''],
    });

    this.triggerForm.get('triggerType').setValue(this.triggerTypes[0]);
    this.triggerForm.get('repeatIndefinite').setValue(true);

    this.triggerForm.get('triggerType').valueChanges.subscribe((newForm) => {
      let triggerType = this.triggerForm.get('triggerType').value;

      if (triggerType === this.triggerTypes[0]) {
        //making cron expression not required in case of simple trigger
        this.triggerForm.get('cronExpression').setValidators([]);
        this.triggerForm.get('cronExpression').updateValueAndValidity();

        //making repeat Interval required in case of simple trigger
        this.triggerForm.get('repeatInterval').setValidators([Validators.required]);
        this.triggerForm.get('repeatInterval').updateValueAndValidity();

      } else {
        //making cron expression required in case of cron trigger
        this.triggerForm.get('cronExpression').setValidators([Validators.required]);
        this.triggerForm.get('cronExpression').updateValueAndValidity();

        //making repeat Interval not required in case of cron trigger
        this.triggerForm.get('repeatInterval').setValidators([]);
        this.triggerForm.get('repeatInterval').updateValueAndValidity();
      }
    });

    this.triggerForm.get('repeatIndefinite').valueChanges.subscribe((newForm) => {
      let triggerType = this.triggerForm.get('triggerType').value;
      let repeatIndefinite = this.triggerForm.get('repeatIndefinite').value;

      if (triggerType === this.triggerTypes[1] || repeatIndefinite) {
        //making repeat count not required in case of repeating indefinitely
        this.triggerForm.get('repeatCount').setValidators([]);
        this.triggerForm.get('repeatCount').updateValueAndValidity();

      } else if (triggerType === this.triggerTypes[0] && !repeatIndefinite) {
        //making repeat count required in case of not repeating indefinitely
        this.triggerForm.get('repeatCount').setValidators([Validators.required]);
        this.triggerForm.get('repeatCount').updateValueAndValidity();
      }
    });

  }

  getTriggerGroups() {
    this.triggerService.getTriggerGroups().subscribe(
      groups => {
        this.options = groups;
        this.filteredOptions = this.triggerForm.get('triggerGroup').valueChanges
          .pipe(startWith(''),
            map(value => this._filter(value))
          );
      }
    );
  }

  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
      if (this.selectJobDialogRef)
        this.selectJobDialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);
    });
  }

  onSubmit() {
    this.submitted = true;
    console.log("submitt")
    // stop here if form is invalid
    if (this.triggerForm.invalid) {
      return;
    }

    this.loading = true;
    let newTrigger = {};
    // using getRawValues to get disabled fields values as well 
    newTrigger = this.triggerForm.getRawValue();
    newTrigger["jobMapData"] = this.getJobMapData();
    //to combine both date and time into same object for start and end times of triggers
    newTrigger['startTime'] = this.combineDateAndTime(newTrigger['startDate'], newTrigger['startTime'])
    newTrigger['endTime'] = this.combineDateAndTime(newTrigger['endDate'], newTrigger['endTime'])
    delete newTrigger['startDate'];
    delete newTrigger['endDate'];
    this.triggerService.create(newTrigger)
      .pipe(first())
      .subscribe(
        data => {
          // this.alertService.success('Registration successful', true);
          // this.router.navigate(['/users']);
          console.log("created");
          console.log(data);
          this.dialogRef.close(data);
        },
        error => {
          this.loading = false;
        });
  }

  getJobMapData() {
    let jobMapData: any = {};
    this.ELEMENT_DATA.forEach(function (obj) {
      jobMapData[obj.dataKey] = obj.dataValue;
    });
    return jobMapData;
  }

  combineDateAndTime(date: string, time: string): Date {
    let tmpDate = new Date(date)
    let hours: number = parseInt(time.substring(0, 2));
    let minutes = parseInt(time.substring(3, 5));
    let ampm = time.substring(6, 8)? time.substring(6, 8): "am";
    if (ampm.toLocaleLowerCase() == "pm") {
      hours = hours + 12;
    } else if (ampm.toLocaleLowerCase() == "am" && hours === 12) {
      hours = 0;
    }
    tmpDate.setHours(hours? hours : 0);
    tmpDate.setMinutes(minutes? minutes : 0);
    return tmpDate;
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  addJobData(): void {
    this.ELEMENT_DATA.push({
      "dataKey": "",
      "dataValue": ""
    })
    this.dataSource = of(this.ELEMENT_DATA);
  }

  removeJobData(index): void {
    this.ELEMENT_DATA.splice(index, 1);
    this.dataSource = of(this.ELEMENT_DATA);
  }

  selectJob(): void {
    this.selectJobDialogRef = this.dialog.open(SelectJobComponent, {
      disableClose: true,
      height: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize,
      width: this.isMediumDeviceOrLess ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
      maxWidth: "none",
      panelClass: 'fc-modal-dialog'
    });
    this.selectJobDialogRef.afterClosed().subscribe(result => {
      console.log("result");
      if (result) {
        this.triggerForm.get('jobName').setValue(result.jobName);
        this.triggerForm.get('jobGroup').setValue(result.jobGroup);
        // this.triggerForm.get('jobClass').setValue(result.jobClass);

        this.changeDetectorRefs.detectChanges();
      }
    });
  }

}
