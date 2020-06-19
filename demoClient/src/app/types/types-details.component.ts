import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { TypesService } from './types.service';
import { ITypes } from './itypes';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';


import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-types-details',
  templateUrl: './types-details.component.html',
  styleUrls: ['./types-details.component.scss']
})
export class TypesDetailsComponent extends BaseDetailsComponent<ITypes> implements OnInit {
	title:string='Types';
	parentUrl:string='types';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: TypesService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

	ngOnInit() {
		this.entityName = 'Types';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    this.getItem();
    this.setPickerSearchListener();
  }
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      id: [{value: '', disabled: true}, Validators.required],
      name: [''],
      
    });
      
  }
  
  onItemFetched(item: ITypes) {
    this.item = item;
		this.itemForm.patchValue(item);
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
