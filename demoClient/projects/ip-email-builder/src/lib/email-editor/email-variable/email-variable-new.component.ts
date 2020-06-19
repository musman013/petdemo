import { Component, OnInit, Inject } from '@angular/core';
import { EmailVariableService } from './email-variable.service';
import { IEmailVariable } from './iemail-variable';

import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BaseNewComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';
@Component({
	selector: 'app-email-variable-new',
	templateUrl: './email-variable-new.component.html',
	styleUrls: ['./email-variable-new.component.scss']
})
export class EmailVariableNewComponent extends BaseNewComponent<IEmailVariable> implements OnInit {

	title: string = "New Email Merge Field";
	entityName: string = 'EmailVariable';
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<EmailVariableNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: EmailVariableService,
		public globalPermissionService: GlobalPermissionService,
		public errorService: ErrorService
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	}

	ngOnInit() {
		this.itemForm = this.formBuilder.group({
			propertyName: ['', Validators.required],
			propertyType: ['', Validators.required],
			defaultValue: [''],
		});
		super.ngOnInit();
		this.checkPassedData();
	}


	// convenience getter for easy access to form fields


}
