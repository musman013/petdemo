import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { EmailVariableService } from './email-variable.service';
import { IEmailVariable } from './iemail-variable';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';
@Component({
	selector: 'app-role-detail',
	templateUrl: './email-variable-detail.component.html',
	styleUrls: ['./email-variable-detail.component.scss']
})
export class EmailVariableDetailComponent extends BaseDetailsComponent<IEmailVariable> implements OnInit {
	title: string = 'Email Merge Fields';
	parentUrl: string = './emailvariables';
	entityName: string = 'EmailVariable';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: EmailVariableService,
		public globalPermissionService: GlobalPermissionService,
		public errorService: ErrorService
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
		var u = this.route.parent.toString();
	}

	ngOnInit() {
		super.ngOnInit();
		this.setForm();
		this.getItem();
	}

	setForm() {
		this.itemForm = this.formBuilder.group({
			id: [''],
			propertyName: ['', Validators.required],
			propertyType: ['', Validators.required],
			defaultValue: ['']
		});
	}

	onItemFetched(item: IEmailVariable) {
		this.item = item;
		this.itemForm.patchValue(item);
	}
}
