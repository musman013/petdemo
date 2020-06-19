import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { PetsService } from './pets.service';
import { IPets } from './ipets';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { TypesService } from '../types/types.service';
import { OwnersService } from '../owners/owners.service';

import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-pets-details',
  templateUrl: './pets-details.component.html',
  styleUrls: ['./pets-details.component.scss']
})
export class PetsDetailsComponent extends BaseDetailsComponent<IPets> implements OnInit {
	title:string='Pets';
	parentUrl:string='pets';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: PetsService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public typesService: TypesService,
		public ownersService: OwnersService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

	ngOnInit() {
		this.entityName = 'Pets';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    this.getItem();
    this.setPickerSearchListener();
  }
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      birthDate: [''],
      id: [{value: '', disabled: true}, Validators.required],
      name: [''],
      typeId: ['', Validators.required],
      typesDescriptiveField : [''],
      ownerId: ['', Validators.required],
      ownersDescriptiveField : [''],
      
    });
      
  }
  
  onItemFetched(item: IPets) {
    this.item = item;
		this.itemForm.patchValue(item);
		this.itemForm.get('birthDate').setValue(item.birthDate? new Date(item.birthDate): null);
  }
  
	setAssociations(){
  	
		this.associations = [
		];
		
		this.childAssociations = this.associations.filter(association => {
			return (association.isParent);
		});

		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}
}
