import { Component, OnInit, Inject } from '@angular/core';
import { InvoicesService } from './invoices.service';
import { IInvoices } from './iinvoices';

import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { PetsService } from '../pets/pets.service';
import { VetsService } from '../vets/vets.service';
import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
	selector: 'app-invoices-new',
	templateUrl: './invoices-new.component.html',
	styleUrls: ['./invoices-new.component.scss']
})
export class InvoicesNewComponent extends BaseNewComponent<IInvoices> implements OnInit {

	title: string = "New Invoices";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<InvoicesNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: InvoicesService,
		public errorService: ErrorService,
		public petsService: PetsService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	}

	ngOnInit() {
		this.entityName = 'Invoices';
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
				referencedDescriptiveField: 'id',

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
		delete newVisit['visitTime'];
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
