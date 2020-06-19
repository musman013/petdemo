import { Component, OnInit, Inject } from '@angular/core';
import { UserroleService } from './userrole.service';
import { IUserrole } from './iuserrole';

import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals, BaseNewComponent, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { GlobalPermissionService } from 'src/app/core/global-permission.service';
import { UserService } from '../user/user.service';
import { RoleService } from '../role/role.service';

@Component({
  selector: 'app-userrole-new',
  templateUrl: './userrole-new.component.html',
  styleUrls: ['./userrole-new.component.scss']
})
export class UserroleNewComponent extends BaseNewComponent<IUserrole> implements OnInit {
  
    title:string = "New Userrole";
    constructor(
      public formBuilder: FormBuilder,
      public router: Router,
      public route: ActivatedRoute,
      public dialog: MatDialog,
      public dialogRef: MatDialogRef<UserroleNewComponent>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      public global: Globals,
      public pickerDialogService: PickerDialogService,
      public dataService: UserroleService,
      public errorService: ErrorService,
      public userService: UserService,
      public roleService: RoleService,
      public globalPermissionService: GlobalPermissionService,
    ) {
      super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
    }
 
  ngOnInit() {
    this.entityName = "Userrole";
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.checkPassedData();
    this.setPickerSearchListener();
  }
  
  setForm() {
    this.itemForm = this.formBuilder.group({
      roleId: ['', Validators.required],
      roleDescriptiveField : [''],
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
            key: 'roleId',
            value: undefined,
            referencedkey: 'id'
          },
        ],
        isParent: false,
        table: 'role',
        type: 'ManyToOne',
        service: this.roleService,
        descriptiveField: 'roleDescriptiveField',
        referencedDescriptiveField: 'name',
      },
    ];
    this.parentAssociations = this.associations.filter(association => {
      return (!association.isParent);
    });
  }
}
