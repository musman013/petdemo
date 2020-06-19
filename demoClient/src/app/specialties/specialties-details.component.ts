import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { SpecialtiesService } from './specialties.service';
import { ISpecialties } from './ispecialties';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';


import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-specialties-details',
  templateUrl: './specialties-details.component.html',
  styleUrls: ['./specialties-details.component.scss']
})
export class SpecialtiesDetailsComponent extends BaseDetailsComponent<ISpecialties> implements OnInit {
	title:string='Specialties';
	parentUrl:string='specialties';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: SpecialtiesService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

	ngOnInit() {
		this.entityName = 'Specialties';
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
  
  onItemFetched(item: ISpecialties) {
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
