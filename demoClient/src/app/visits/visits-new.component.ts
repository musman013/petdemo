import { Component, OnInit, Inject } from '@angular/core';
import { VisitsService } from './visits.service';
import { IVisits } from './ivisits';

import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { PetsService } from '../pets/pets.service';
import { VetsService } from '../vets/vets.service';
import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
	selector: 'app-visits-new',
	templateUrl: './visits-new.component.html',
	styleUrls: ['./visits-new.component.scss']
})
export class VisitsNewComponent extends BaseNewComponent<IVisits> implements OnInit {

	title: string = "New Visits";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<VisitsNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: VisitsService,
		public errorService: ErrorService,
		public petsService: PetsService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	}

	ngOnInit() {
		this.entityName = 'Visits';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
		this.checkPassedData();
		this.setPickerSearchListener();
	}

	setForm() {
		this.itemForm = this.formBuilder.group({
			description: [''],
			visitDate: [''],
      visitTime: [''],
			visitEndDate: [''],
      visitEndTime: [''],
      color: [''],
			petId: ['', Validators.required],
			petsDescriptiveField: [''],
			vetId: ['', Validators.required],
			vetsDescriptiveField: [''],
		});
	}

	setAssociations() {

		this.associations = [
			{
				column: [
					{
						key: 'petId',
						value: undefined,
						referencedkey: 'id'
					},

				],
				isParent: false,
				table: 'pets',
				type: 'ManyToOne',
				service: this.petsService,
				descriptiveField: 'petsDescriptiveField',
				referencedDescriptiveField: 'name',

			},
			{
				column: [
					{
						key: 'vetId',
						value: undefined,
						referencedkey: 'id'
					},

				],
				isParent: false,
				table: 'vets',
				type: 'ManyToOne',
				service: this.vetsService,
				descriptiveField: 'vetsDescriptiveField',
				referencedDescriptiveField: 'userName',

			},
		];
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});

	}

	onSubmit() {
		// stop here if form is invalid
		if (this.itemForm.invalid) {
			return;
		}
		this.submitted = true;
		this.loading = true;

		let newVisit = this.itemForm.getRawValue();
		newVisit['visitDate'] = this.dataService.combineDateAndTime(newVisit['visitDate'], newVisit['visitTime'])
		newVisit['visitEndDate'] = this.dataService.combineDateAndTime(newVisit['visitEndDate'], newVisit['visitEndTime'])
		delete newVisit['visitTime'];
		delete newVisit['visitEndTime'];
		this.dataService.create(newVisit)
			.pipe(first())
			.subscribe(
				data => {
					this.dialogRef.close(data);
				},
				error => {
					this.errorService.showError("Error Occured while updating");
					this.loading = false;
					this.dialogRef.close(null);
				});
	}

}
