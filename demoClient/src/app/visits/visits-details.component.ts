import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { VisitsService } from './visits.service';
import { IVisits } from './ivisits';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { PetsService } from '../pets/pets.service';
import { VetsService } from '../vets/vets.service';

import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-visits-details',
  templateUrl: './visits-details.component.html',
  styleUrls: ['./visits-details.component.scss']
})
export class VisitsDetailsComponent extends BaseDetailsComponent<IVisits> implements OnInit {
	title:string='Visits';
	parentUrl:string='visits';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: VisitsService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public petsService: PetsService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

	ngOnInit() {
		this.entityName = 'Visits';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    this.getItem();
    this.setPickerSearchListener();
  }
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      description: [''],
      id: [{value: '', disabled: true}, Validators.required],
      visitDate: [''],
      petId: ['', Validators.required],
      petsDescriptiveField : [''],
      vetId: ['', Validators.required],
      vetsDescriptiveField : [''],
      
    });
      
  }
  
  onItemFetched(item: IVisits) {
    this.item = item;
		this.itemForm.patchValue(item);
		this.itemForm.get('visitDate').setValue(item.visitDate? new Date(item.visitDate): null);
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
