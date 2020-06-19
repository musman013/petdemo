import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { VetSpecialtiesService } from './vet-specialties.service';
import { IVetSpecialties } from './ivet-specialties';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';

import { SpecialtiesService } from '../specialties/specialties.service';
import { VetsService } from '../vets/vets.service';

import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-vet-specialties-details',
  templateUrl: './vet-specialties-details.component.html',
  styleUrls: ['./vet-specialties-details.component.scss']
})
export class VetSpecialtiesDetailsComponent extends BaseDetailsComponent<IVetSpecialties> implements OnInit {
	title:string='VetSpecialties';
	parentUrl:string='vetspecialties';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: VetSpecialtiesService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public specialtiesService: SpecialtiesService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

	ngOnInit() {
		this.entityName = 'VetSpecialties';
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
    this.getItem();
    this.setPickerSearchListener();
  }
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      specialtyId: ['', Validators.required],
      vetId: ['', Validators.required],
      specialtiesDescriptiveField : [''],
      vetsDescriptiveField : [''],
      
    });
      
  }
  
  onItemFetched(item: IVetSpecialties) {
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
