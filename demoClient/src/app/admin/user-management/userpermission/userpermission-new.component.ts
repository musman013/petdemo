import { Component, OnInit, Inject } from '@angular/core';
import { UserpermissionService } from './userpermission.service';
import { IUserpermission } from './iuserpermission';

import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { GlobalPermissionService } from 'src/app/core/global-permission.service';
import { UserService } from '../user/user.service';
import { PermissionService } from '../permission/permission.service';

@Component({
  selector: 'app-userpermission-new',
  templateUrl: './userpermission-new.component.html',
  styleUrls: ['./userpermission-new.component.scss']
})
export class UserpermissionNewComponent extends BaseNewComponent<IUserpermission> implements OnInit {
  
    title:string = "New Userpermission";
		constructor(
			public formBuilder: FormBuilder,
			public router: Router,
			public route: ActivatedRoute,
			public dialog: MatDialog,
			public dialogRef: MatDialogRef<UserpermissionNewComponent>,
			@Inject(MAT_DIALOG_DATA) public data: any,
			public global: Globals,
			public pickerDialogService: PickerDialogService,
			public dataService: UserpermissionService,
			public errorService: ErrorService,
			public userService: UserService,
			public permissionService: PermissionService,
			public globalPermissionService: GlobalPermissionService,
		) {
			super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	  }
 
	ngOnInit() {
		this.entityName = "Userpermission";
		this.setAssociations();
		super.ngOnInit();
    this.setForm();
		this.checkPassedData();
    this.setPickerSearchListener();
  }
  
  setForm() {
    this.itemForm = this.formBuilder.group({
      revoked : [false],
      permissionId: ['', Validators.required],
      permissionDescriptiveField : [''],
      userId: ['', Validators.required],
      userDescriptiveField : [''],
    });
  }
  
  setAssociations() {
  	
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
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});
	}
}
