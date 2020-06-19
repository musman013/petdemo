import { Component, OnInit } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { first } from 'rxjs/operators';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

import { UserroleService } from './userrole.service';
import { IUserrole } from './iuserrole';
import { PickerDialogService, ErrorService, BaseDetailsComponent, Globals } from 'projects/fast-code-core/src/public_api';

import { UserService } from '../user/user.service';
import { RoleService } from '../role/role.service';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';

@Component({
  selector: 'app-userrole-details',
  templateUrl: './userrole-details.component.html',
  styleUrls: ['./userrole-details.component.scss']
})
export class UserroleDetailsComponent extends BaseDetailsComponent<IUserrole> implements OnInit {
  title:string='Userrole';
  parentUrl:string='userrole';
  
  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog,
    public global: Globals,
    public dataService: UserroleService,
    public pickerDialogService: PickerDialogService,
    public errorService: ErrorService,
    public userService: UserService,
    public roleService: RoleService,
    public globalPermissionService: GlobalPermissionService,
  ) {
    super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
  }

  ngOnInit() {
    this.entityName = "Userrole";
    this.setAssociations();
    super.ngOnInit();
    this.setForm();
    this.getItem();
    this.setPickerSearchListener();
  }
  
  setForm(){
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
    this.childAssociations = this.associations.filter(association => {
      return (association.isParent);
    });

    this.parentAssociations = this.associations.filter(association => {
      return (!association.isParent);
    });
  }
}
