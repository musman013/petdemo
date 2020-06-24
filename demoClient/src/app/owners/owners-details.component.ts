import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { OwnersService } from './owners.service';
import { IOwners } from './iowners';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';


import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
	selector: 'app-owners-details',
	templateUrl: './owners-details.component.html',
	styleUrls: ['./owners-details.component.scss']
})
export class OwnersDetailsComponent extends BaseDetailsComponent<IOwners> implements OnInit {
	title: string = 'Owners';
	parentUrl: string = 'owners';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: OwnersService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
	}

	ngOnInit() {
		this.entityName = 'Owners';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
		this.getItem();
		this.setPickerSearchListener();
	}

	setForm() {
		this.itemForm = this.formBuilder.group({
			address: [''],
			city: [''],
			id: [{ value: '', disabled: true }, Validators.required],

			emailAddress: ['', Validators.required],
			firstName: ['', Validators.required],
			isActive: [false],
			lastName: ['', Validators.required],
			phoneNumber: [''],
			userName: ['', Validators.required],

		});

	}

	onItemFetched(item: IOwners) {
		this.item = item;
		this.itemForm.patchValue(item);
	}

	setAssociations() {

		this.associations = [
		];

		this.childAssociations = this.associations.filter(association => {
			return (association.isParent);
		});

		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}
}
