import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import { IVetSpecialties } from './ivet-specialties';
import { VetSpecialtiesService } from './vet-specialties.service';
import { Router, ActivatedRoute } from '@angular/router';
import { VetSpecialtiesNewComponent } from './vet-specialties-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { SpecialtiesService } from '../specialties/specialties.service';
import { VetsService } from '../vets/vets.service';
import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-vet-specialties-list',
  templateUrl: './vet-specialties-list.component.html',
  styleUrls: ['./vet-specialties-list.component.scss']
})
export class VetSpecialtiesListComponent extends BaseListComponent<IVetSpecialties> implements OnInit {

	title:string = "VetSpecialties";
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: VetSpecialtiesService,
		public errorService: ErrorService,
		public specialtiesService: SpecialtiesService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
  }

	ngOnInit() {
		this.entityName = 'VetSpecialties';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = [ "specialtyId", "vetId",  ]
		super.ngOnInit();
	}
  
  
	setAssociation(){
  	
		this.associations = [
			{
				column: [
                      {
					  	key: 'specialtyId',
					  	value: undefined,
					  	referencedkey: 'id'
					  },
					  
				],
				isParent: false,
				descriptiveField: 'specialtiesDescriptiveField',
				referencedDescriptiveField: 'name',
				service: this.specialtiesService,
				associatedObj: undefined,
				table: 'specialties',
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
	  			column: 'Specialties',
				label: 'Specialties',
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
		super.addNew(VetSpecialtiesNewComponent);
	}
  
}
