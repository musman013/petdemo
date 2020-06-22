import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import { IVisits } from './ivisits';
import { VisitsService } from './visits.service';
import { Router, ActivatedRoute } from '@angular/router';
import { VisitsNewComponent } from './visits-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService, ConfirmDialogComponent } from 'projects/fast-code-core/src/public_api';

import { PetsService } from '../pets/pets.service';
import { VetsService } from '../vets/vets.service';
import { GlobalPermissionService } from '../core/global-permission.service';
import { CompleteVisitComponent } from './complete-visit/complete-visit.component';

@Component({
  selector: 'app-visits-list',
  templateUrl: './visits-list.component.html',
  styleUrls: ['./visits-list.component.scss']
})
export class VisitsListComponent extends BaseListComponent<IVisits> implements OnInit {

	title:string = "Visits";
	completeVisitRef: MatDialogRef<CompleteVisitComponent>;
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
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
  }

	ngOnInit() {
		this.entityName = 'Visits';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = [ "id",  ]
		super.ngOnInit();
	}
  
  
	setAssociation(){
  	
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
  
  	setColumns(){
  		this.columns = [
    		{
				column: 'description',
				label: 'description',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
    		{
				column: 'id',
				label: 'id',
				sort: false,
				filter: false,
				type: listColumnType.Number
			},
    		{
				column: 'visitDate',
				label: 'visitDate',
				sort: true,
				filter: true,
				type: listColumnType.Date
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

	completeVisit(){
		this.completeVisitRef = this.dialog.open(CompleteVisitComponent, {
			panelClass: "fc-modal-dialog"
		});
		this.completeVisitRef.afterClosed().subscribe(res => {
			if(res){
				console.log(res);
			}
		})
	}

	changeStatus(status){
		this.dialog.open(ConfirmDialogComponent, {
			data: {
				confirmationType: "confirm"
			}
		}).afterClosed().subscribe(res => {
			if(res){
				console.log(res);
				// this.dataService.changeStatus(status);
			}
		});
	}
  
}
