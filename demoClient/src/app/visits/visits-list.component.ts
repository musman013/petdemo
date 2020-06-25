import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { IVisits, IChangeStatusObj, VisitStatus } from './ivisits';
import { VisitsService } from './visits.service';
import { Router, ActivatedRoute } from '@angular/router';
import { VisitsNewComponent } from './visits-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService, ConfirmDialogComponent, ITokenRole } from 'projects/fast-code-core/src/public_api';

import { PetsService } from '../pets/pets.service';
import { VetsService } from '../vets/vets.service';
import { GlobalPermissionService } from '../core/global-permission.service';
import { CompleteVisitComponent } from './complete-visit/complete-visit.component';
import { AuthenticationService } from '../core/authentication.service';

@Component({
	selector: 'app-visits-list',
	templateUrl: './visits-list.component.html',
	styleUrls: ['./visits-list.component.scss']
})
export class VisitsListComponent extends BaseListComponent<IVisits> implements OnInit {

	title: string = "Visits";
	completeVisitRef: MatDialogRef<CompleteVisitComponent>;

	ITokenRole = ITokenRole;
	VisitStatus = VisitStatus;
	role: ITokenRole;

	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: VisitsService,
		public errorService: ErrorService,
		public petsService: PetsService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
		public authenticationService: AuthenticationService
	) {
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
	}

	ngOnInit() {
		this.entityName = 'Visits';
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
						key: 'petId',
						value: undefined,
						referencedkey: 'id'
					},

				],
				isParent: false,
				descriptiveField: 'petsDescriptiveField',
				referencedDescriptiveField: 'name',
				service: this.petsService,
				associatedObj: undefined,
				table: 'pets',
				type: 'ManyToOne'
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
				descriptiveField: 'vetsDescriptiveField',
				referencedDescriptiveField: 'id',
				service: this.vetsService,
				associatedObj: undefined,
				table: 'vets',
				type: 'ManyToOne'
			},
		];
	}

	setColumns() {
		this.columns = [
			{
				column: 'description',
				label: 'description',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
				column: 'visitDate',
				label: 'visitDate',
				sort: true,
				filter: true,
				type: listColumnType.Date
			},
			{
				column: 'status',
				label: 'status',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
				column: 'Pets',
				label: 'Pets',
				sort: false,
				filter: false,
				type: listColumnType.String
			},
			{
				column: 'Vets',
				label: 'Vets',
				sort: false,
				filter: false,
				type: listColumnType.String
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
		super.addNew(VisitsNewComponent);
	}

	completeVisit(item: IVisits, index) {
		this.completeVisitRef = this.dialog.open(CompleteVisitComponent, {
			panelClass: "fc-modal-dialog"
		});
		this.completeVisitRef.afterClosed().subscribe(res => {
			if (res) {
				console.log(res);
				let changeStatusObj: IChangeStatusObj = {
					status: VisitStatus.Completed,
					invoiceAmount: res.invoiceAmount,
					visitNotes: res.visitNotes
				}

				this.dataService.changeStatus(item.id, changeStatusObj).subscribe(res => {
					if (res) {
						this.items[index].status = res.status;
						this.items[index].visitNotes = res.visitNotes;
						this.errorService.showError('Visit Completed');
					} else {
						this.errorService.showError('An error occurred');
					}
				});
			}
		})
	}

	changeStatus(item: IVisits, index, status: VisitStatus) {
		this.dialog.open(ConfirmDialogComponent, {
			data: {
				confirmationType: "confirm"
			}
		}).afterClosed().subscribe(res => {
			if (res) {
				console.log(res);
				let changeStatusObj: IChangeStatusObj = {
					status: status,
				}

				this.dataService.changeStatus(item.id, changeStatusObj).subscribe(res => {
					if (res) {
						this.errorService.showError(`Visit status changed to: ${status}`);
						this.items[index].status = res.status;
					} else {
						this.errorService.showError('An error occurred');
					}
				});
			}
		});
	}

}
