import { Component, OnInit, Inject } from '@angular/core';
import { VetSpecialtiesService } from './vet-specialties.service';
import { IVetSpecialties } from './ivet-specialties';

import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { SpecialtiesService } from '../specialties/specialties.service';
import { VetsService } from '../vets/vets.service';
import { GlobalPermissionService } from '../core/global-permission.service';

@Component({
  selector: 'app-vet-specialties-new',
  templateUrl: './vet-specialties-new.component.html',
  styleUrls: ['./vet-specialties-new.component.scss']
})
export class VetSpecialtiesNewComponent extends BaseNewComponent<IVetSpecialties> implements OnInit {
  
    title:string = "New VetSpecialties";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<VetSpecialtiesNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: VetSpecialtiesService,
		public errorService: ErrorService,
		public specialtiesService: SpecialtiesService,
		public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	}
 
	ngOnInit() {
		this.entityName = 'VetSpecialties';
		this.setAssociations();
		super.ngOnInit();
    this.setForm();
		this.checkPassedData();
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
	 
	setAssociations(){
  	
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
				table: 'specialties',
				type: 'ManyToOne',
				service: this.specialtiesService,
				descriptiveField: 'specialtiesDescriptiveField',
				referencedDescriptiveField: 'name',
		    
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
				table: 'vets',
				type: 'ManyToOne',
				service: this.vetsService,
				descriptiveField: 'vetsDescriptiveField',
				referencedDescriptiveField: 'id',
		    
			},
		];
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});

	}  
    
}
