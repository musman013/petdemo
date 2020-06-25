import { Component, OnInit, Inject } from '@angular/core';
import { PetsService } from './pets.service';
import { IPets } from './ipets';

import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { TypesService } from '../types/types.service';
import { OwnersService } from '../owners/owners.service';
import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-pets-new',
  templateUrl: './pets-new.component.html',
  styleUrls: ['./pets-new.component.scss']
})
export class PetsNewComponent extends BaseNewComponent<IPets> implements OnInit {
  
    title:string = "New Pets";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<PetsNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: PetsService,
		public errorService: ErrorService,
		public typesService: TypesService,
		public ownersService: OwnersService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	}
 
	ngOnInit() {
		this.entityName = 'Pets';
		this.setAssociations();
		super.ngOnInit();
    this.setForm();
		this.checkPassedData();
		this.setPickerSearchListener();
  }
 		
	setForm(){
 		this.itemForm = this.formBuilder.group({
      birthDate: [''],
      name: [''],
      typeId: ['', Validators.required],
      typesDescriptiveField : [''],
      ownerId: ['', Validators.required],
      ownersDescriptiveField : [''],
    });
	}
	 
	setAssociations(){
  	
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
				table: 'types',
				type: 'ManyToOne',
				service: this.typesService,
				descriptiveField: 'typesDescriptiveField',
				referencedDescriptiveField: 'name',
		    
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
				table: 'owners',
				type: 'ManyToOne',
				service: this.ownersService,
				descriptiveField: 'ownersDescriptiveField',
				referencedDescriptiveField: 'userName',
		    
			},
		];
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});

	}  
    
}
