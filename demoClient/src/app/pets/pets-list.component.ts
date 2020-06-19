import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import { IPets } from './ipets';
import { PetsService } from './pets.service';
import { Router, ActivatedRoute } from '@angular/router';
import { PetsNewComponent } from './pets-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { TypesService } from '../types/types.service';
import { OwnersService } from '../owners/owners.service';
import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-pets-list',
  templateUrl: './pets-list.component.html',
  styleUrls: ['./pets-list.component.scss']
})
export class PetsListComponent extends BaseListComponent<IPets> implements OnInit {

	title:string = "Pets";
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: PetsService,
		public errorService: ErrorService,
		public typesService: TypesService,
		public ownersService: OwnersService,
		public globalPermissionService: GlobalPermissionService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
  }

	ngOnInit() {
		this.entityName = 'Pets';
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
					  	key: 'typeId',
					  	value: undefined,
					  	referencedkey: 'id'
					  },
					  
				],
				isParent: false,
				descriptiveField: 'typesDescriptiveField',
				referencedDescriptiveField: 'name',
				service: this.typesService,
				associatedObj: undefined,
				table: 'types',
				type: 'ManyToOne'
			},
			{
				column: [
                      {
					  	key: 'ownerId',
					  	value: undefined,
					  	referencedkey: 'id'
					  },
					  
				],
				isParent: false,
				descriptiveField: 'ownersDescriptiveField',
				referencedDescriptiveField: 'id',
				service: this.ownersService,
				associatedObj: undefined,
				table: 'owners',
				type: 'ManyToOne'
			},
		];
	}
  
  	setColumns(){
  		this.columns = [
    		{
				column: 'birthDate',
				label: 'birthDate',
				sort: true,
				filter: true,
				type: listColumnType.Date
			},
    		{
				column: 'id',
				label: 'id',
				sort: false,
				filter: false,
				type: listColumnType.Number
			},
    		{
				column: 'name',
				label: 'name',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
	  			column: 'Types',
				label: 'Types',
				sort: false,
				filter: false,
				type: listColumnType.String
	  		},
			{
	  			column: 'Owners',
				label: 'Owners',
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
		super.addNew(PetsNewComponent);
	}
  
}
