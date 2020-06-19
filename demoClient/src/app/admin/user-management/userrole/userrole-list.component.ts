import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import { IUserrole } from './iuserrole';
import { UserroleService } from './userrole.service';
import { Router, ActivatedRoute } from '@angular/router';
import { UserroleNewComponent} from './userrole-new.component';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';

import { UserService } from '../user/user.service';
import { RoleService } from '../role/role.service';

@Component({
  selector: 'app-userrole-list',
  templateUrl: './userrole-list.component.html',
  styleUrls: ['./userrole-list.component.scss']
})
export class UserroleListComponent extends BaseListComponent<IUserrole> implements OnInit {

  title:string = "Userrole";
  
  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public global: Globals,
    public dialog: MatDialog,
    public changeDetectorRefs: ChangeDetectorRef,
    public pickerDialogService: PickerDialogService,
    public dataService: UserroleService,
    public errorService: ErrorService,
    public userService: UserService,
    public roleService: RoleService,
    public globalPermissionService: GlobalPermissionService,
  ) { 
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
  }

  ngOnInit() {
    this.entityName = "Userrole";
    this.setAssociation();
    this.setColumns();
    this.primaryKeys = [ "roleId", "userId" ]
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
            key: 'roleId',
            value: undefined,
            referencedkey: 'id'
          },  
        ],
        isParent: false,
        descriptiveField: 'roleDescriptiveField',
        referencedDescriptiveField: 'name',
        service: this.roleService,
        associatedObj: undefined,
        table: 'role',
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
          column: 'Role',
        label: 'Role',
        sort: false,
        filter: false,
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
    super.addNew(UserroleNewComponent);
  }
  
}
