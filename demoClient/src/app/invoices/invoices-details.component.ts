import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { InvoicesService } from './invoices.service';
import { IInvoices } from './iinvoices';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { PetsService } from '../pets/pets.service';
import { VetsService } from '../vets/vets.service';

import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
	selector: 'app-invoices-details',
	templateUrl: './invoices-details.component.html',
	styleUrls: ['./invoices-details.component.scss']
})
export class InvoicesDetailsComponent extends BaseDetailsComponent<IInvoices> implements OnInit {
	title: string = 'Invoices';
	parentUrl: string = 'invoices';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: InvoicesService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public petsService: PetsService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
	}

	ngOnInit() {
		this.entityName = 'Invoices';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
		this.getItem();
		this.setPickerSearchListener();
	}

	setForm() {
		this.itemForm = this.formBuilder.group({
			id: [{ value: '', disabled: true }, Validators.required],
			amount: ['', Validators.required],
			vetId: ['', Validators.required],
			status: ['', Validators.required],
			vetsDescriptiveField: [''],

		});

	}

	onItemFetched(item: IInvoices) {
		this.item = item;
		this.itemForm.patchValue(item);
	}

	setAssociations() {

		this.associations = [
			{
				column: [
					{
						key: 'visitId',
						value: undefined,
						referencedkey: 'id'
					},

				],
				isParent: false,
				table: 'visits',
				type: 'OneToOne',
				service: this.petsService,
				descriptiveField: 'visitsDescriptiveField',
				referencedDescriptiveField: 'id',

			}
		];

		this.childAssociations = this.associations.filter(association => {
			return (association.isParent);
		});

		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}

	onSubmit() {
		if (this.itemForm.invalid) {
			return;
		}
		this.submitted = true;
		this.loading = true;
		this.dataService.update(this.itemForm.getRawValue(), this.idParam)
			.pipe(first())
			.subscribe(
				data => {
					this.loading = false;
					this.router.navigate([this.parentUrl], { relativeTo: this.route.parent });
				},
				error => {
					this.errorService.showError("Error Occured while updating");
					this.loading = false;
				});
	}
}
