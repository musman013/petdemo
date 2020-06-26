import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Globals } from '../../globals';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { Router } from "@angular/router";
import { TranslateService } from '@ngx-translate/core';

import { ProcessService } from "../process.service";

@Component({
  selector: 'app-process-new',
  templateUrl: './process-new.component.html',
  styleUrls: ['./process-new.component.scss']
})
export class ProcessNewComponent implements OnInit {

  selectProcessDefinitionDialogRef: MatDialogRef<any>;

  processDefinitions: any[];
  processForm: FormGroup;
  loading = false;
  submitted = false;

  isMediumDeviceOrLess: boolean;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "50%";
  largerDeviceDialogHeightSize: string = "85%";

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private global: Globals,
    public dialogRef: MatDialogRef<ProcessNewComponent>,
    public dialog: MatDialog,
    public processService: ProcessService,
    private translate: TranslateService,
    private changeDetectorRefs: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
    });

    this.loadProcessDefinitions(null);

    this.processForm = this.formBuilder.group({
      name: [''],
      processDefinition: ['', Validators.required]
    });
  }

  loadProcessDefinitions(appDefinitionKey) {
    this.processService.getProcessDefinitions(appDefinitionKey).subscribe((response) => {
      this.processDefinitions = response.data;
    });
  };

  onCancel(): void {
    this.dialogRef.close(null);
  }

  onSubmit() {
    let process: any = this.processForm.value;
    this.dialogRef.close(process);
  }

}
