import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import { IVets } from './ivets';
import { VetsService } from './vets.service';
import { Router, ActivatedRoute } from '@angular/router';
import { VetsNewComponent } from './vets-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-vets-list',
  templateUrl: './vets-list.component.html',
  styleUrls: ['./vets-list.component.scss']
})
export class VetsListComponent extends BaseListComponent<IVets> implements OnInit {

	title:string = "Vets";
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: VetsService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
  }

	ngOnInit() {
		this.entityName = 'Vets';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = [ "id",  ]
		super.ngOnInit();
	}
  
  
	setAssociation(){
  	
		this.associations = [
		];
	}
  
  	setColumns(){
  		this.columns = [
    		{
				column: 'firstName',
				label: 'firstName',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
				column: 'lastName',
				label: 'lastName',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
				column: 'userName',
				label: 'userName',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
				column: 'emailAddress',
				label: 'emailAddress',
				sort: true,
				filter: true,
				type: listColumnType.String
			},
			{
				column: 'isActive',
				label: 'isActive',
				sort: true,
				filter: true,
				type: listColumnType.Boolean
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
		super.addNew(VetsNewComponent);
	}
  
}
