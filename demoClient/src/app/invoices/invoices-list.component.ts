import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { IInvoices } from './iinvoices';
import { InvoicesService } from './invoices.service';
import { Router, ActivatedRoute } from '@angular/router';
import { InvoicesNewComponent } from './invoices-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService, ConfirmDialogComponent, ITokenRole } from 'projects/fast-code-core/src/public_api';

import { PetsService } from '../pets/pets.service';
import { VetsService } from '../vets/vets.service';
import { GlobalPermissionService } from '../core/global-permission.service';
import { AuthenticationService } from '../core/authentication.service';
import { VisitsService } from '../visits';
import { VisitStatus } from '../visits/ivisits';

@Component({
	selector: 'app-invoices-list',
	templateUrl: './invoices-list.component.html',
	styleUrls: ['./invoices-list.component.scss']
})
export class InvoicesListComponent extends BaseListComponent<IInvoices> implements OnInit {

	title: string = "Invoices";

	role: ITokenRole;
	ITokenRole = ITokenRole;
	VisitStatus = VisitStatus;
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: InvoicesService,
		public errorService: ErrorService,
		public visitsService: VisitsService,
		public globalPermissionService: GlobalPermissionService,
		public authenticationService: AuthenticationService,
	) {
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
	}

	ngOnInit() {
		this.entityName = 'Invoices';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = ["id",]
		this.role = this.authenticationService.decodeToken().role;
		super.ngOnInit();
	}


	setAssociation() {

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
				descriptiveField: 'visitsDescriptiveField',
				referencedDescriptiveField: 'id',
				service: this.visitsService,
				associatedObj: undefined,
				table: 'visits',
				type: 'OneToOne'
			},
		];
	}

	setColumns() {
		this.columns = [
			{
				column: 'amount',
				label: 'amount',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
				column: 'status',
				label: 'status',
				sort: true,
				filter: true,
				type: listColumnType.Date
			},
			{
				column: 'owner',
				label: 'Owner',
				sort: false,
				filter: false,
				type: listColumnType.String
			},
			{
				column: 'vet',
				label: 'Vet',
				sort: false,
				filter: false,
				type: listColumnType.String
			},
			{
				column: 'visit',
				label: 'Visit',
				sort: false,
				filter: false,
				type: listColumnType.Date
			},
			{
				column: 'actions',
				label: 'Actions',
				sort: false,
				filter: false,
				type: listColumnType.String
			}
		];
		this.selectedColumns = this.columns;
		this.displayedColumns = this.columns.map((obj) => { return obj.column });
	}
	addNew() {
		super.addNew(InvoicesNewComponent);
	}

	payInvoice(item: IInvoices, index) {
		this.dialog.open(ConfirmDialogComponent, {
			data: {
				confirmationType: "confirm"
			}
		}).afterClosed().subscribe(res => {
			if (res) {
				console.log(res);
				this.dataService.payInvoice(item.id).subscribe(res => {
					if (res) {
						this.items[index].status = res.status;
						this.errorService.showError(`Invoice paid`);
					} else {
						this.errorService.showError('An error occurred');
					}
				});

			}
		});
	}

}
