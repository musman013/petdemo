import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { UserpermissionService } from './userpermission.service';
import { IUserpermission } from './iuserpermission';
import { PickerDialogService, ErrorService, BaseDetailsComponent, Globals } from 'projects/fast-code-core/src/public_api';

import { UserService } from '../user/user.service';
import { PermissionService } from '../permission/permission.service';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';

@Component({
  selector: 'app-userpermission-details',
  templateUrl: './userpermission-details.component.html',
  styleUrls: ['./userpermission-details.component.scss']
})
export class UserpermissionDetailsComponent extends BaseDetailsComponent<IUserpermission> implements OnInit {
  title:string='Userpermission';
  parentUrl:string='userpermission';
  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public dataService: UserpermissionService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public userService: UserService,
		public permissionService: PermissionService,
		public globalPermissionService: GlobalPermissionService,
	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

	ngOnInit() {
		this.entityName = "Userpermission";
		this.setAssociations();
		super.ngOnInit();
		this.setForm();
		this.getItem();
    this.setPickerSearchListener();
  }
  
  setForm(){
    this.itemForm = this.formBuilder.group({
      revoked : [{ value: false }],
      permissionId: ['', Validators.required],
      permissionDescriptiveField : [''],
      userId: ['', Validators.required],
      userDescriptiveField : [''],
    });  
  }
  
	setAssociations(){
  	
		this.associations = [
			{
				column: [
					{
						key: 'userId',
						value: undefined,
						referencedkey: 'id'
					},
				],
				isParent: false,
				table: 'user',
				type: 'ManyToOne',
				service: this.userService,
				descriptiveField: 'userDescriptiveField',
				referencedDescriptiveField: 'userName',
				
			},
			{
				column: [
					{
						key: 'permissionId',
						value: undefined,
						referencedkey: 'id'
					},
				],
				isParent: false,
				table: 'permission',
				type: 'ManyToOne',
				service: this.permissionService,
				descriptiveField: 'permissionDescriptiveField',
				referencedDescriptiveField: 'name',
			},
		];
		this.childAssociations = this.associations.filter(association => {
			return (association.isParent);
		});

		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}
}
