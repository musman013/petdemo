import { Component, OnInit, Inject } from '@angular/core';
import { OwnersService } from './owners.service';
import { IOwners } from './iowners';

import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
	selector: 'app-owners-new',
	templateUrl: './owners-new.component.html',
	styleUrls: ['./owners-new.component.scss']
})
export class OwnersNewComponent extends BaseNewComponent<IOwners> implements OnInit {

	title: string = "New Owners";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<OwnersNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: OwnersService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	}

	ngOnInit() {
		this.entityName = 'Owners';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
		this.checkPassedData();
		this.setPickerSearchListener();
	}

	setForm() {
		this.itemForm = this.formBuilder.group({
			address: [''],
			city: [''],

			emailAddress: ['', Validators.required],
			firstName: ['', Validators.required],
			isActive: [false],
			lastName: ['', Validators.required],
			password: ['', Validators.required],
			confirmPassword: ['', Validators.required],
			phoneNumber: [''],
			userName: ['', Validators.required],
		});
	}

	setAssociations() {

		this.associations = [
		];
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});

	}

}
