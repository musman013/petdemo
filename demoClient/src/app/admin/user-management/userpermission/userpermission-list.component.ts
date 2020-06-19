import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import { IUserpermission } from './iuserpermission';
import { UserpermissionService } from './userpermission.service';
import { Router, ActivatedRoute } from '@angular/router';
import { UserpermissionNewComponent} from './userpermission-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';

import { UserService } from '../user/user.service';
import { PermissionService } from '../permission/permission.service';

@Component({
  selector: 'app-userpermission-list',
  templateUrl: './userpermission-list.component.html',
  styleUrls: ['./userpermission-list.component.scss']
})
export class UserpermissionListComponent extends BaseListComponent<IUserpermission> implements OnInit {

	title:string = "Userpermission";
	
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: UserpermissionService,
		public errorService: ErrorService,
		public userService: UserService,
		public permissionService: PermissionService,
		public globalPermissionService: GlobalPermissionService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
  }

	ngOnInit() {
		this.entityName = "Userpermission";
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = [ "permissionId", "userId" ]
		super.ngOnInit();
	}
  
	setAssociation(){
  	
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
				descriptiveField: 'userDescriptiveField',
				referencedDescriptiveField: 'userName',
				service: this.userService,
				associatedObj: undefined,
				table: 'user',
				type: 'ManyToOne'
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
				descriptiveField: 'permissionDescriptiveField',
				referencedDescriptiveField: 'name',
				service: this.permissionService,
				associatedObj: undefined,
				table: 'permission',
				type: 'ManyToOne'
			},
		];
	}
  
	setColumns(){
		this.columns = [
			{
	  		column: 'User',
				label: 'User',
				sort: false,
				filter: false,
				type: listColumnType.Boolean
	  	},
			{
	  		column: 'Permission',
				label: 'Permission',
				sort: false,
				filter: false,
				type: listColumnType.Boolean
	  	},
	  	{
        column: 'revoked',
        label: 'revoked',
        sort: true,
        filter: true,
        type: listColumnType.Boolean
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
		super.addNew(UserpermissionNewComponent);
	}
  
}
