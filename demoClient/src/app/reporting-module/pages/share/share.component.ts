import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { of } from 'rxjs';
import { IShareConfig, sharingType } from './ishare-config';
import { UserService } from 'src/app/admin/user-management/user/index';
import { RoleService } from 'src/app/admin/user-management/role/index';

@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['./share.component.scss']
})
export class ShareComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<ShareComponent>,
    @Inject(MAT_DIALOG_DATA) public data: IShareConfig,
    public userService: UserService,
    public roleService: RoleService
  ) {
    console.log(this.data)
   }
    
  shareData = {
    users: [],
    roles: []
  }

  userDescriptiveField: string = "userName";
  roleDescriptiveField: string = "displayName"

  ngOnInit() {
  }

  cancel(){
    this.dialogRef.close(null);
  }

  select(){
    this.dialogRef.close(this.shareData);
  }

  userServiceMethod = (parentId: any, searchText, offset: number, limit: number)=>{
    if(this.data.type == sharingType.Share)
      return this.userService.getAvailableAssociations(this.data.resource, this.data.id, searchText, offset, limit)
    return this.userService.getAssociations(this.data.resource, this.data.id, searchText, offset, limit)
  }

  roleServiceMethod = (parentId: any, searchText, offset: number, limit: number)=>{
    if(this.data.type == sharingType.Share)
      return this.roleService.getAvailableAssociations(this.data.resource, parentId, searchText, offset, limit)
    return this.roleService.getAssociations(this.data.resource, parentId, searchText, offset, limit)
  }

  userSelectionUpdated(data){
    this.shareData['users'] = data;
    console.log(this.shareData);
  }

  selectionUpdated(data, key){
    this.shareData[key] = data;
    console.log(this.shareData);
  }
  
}
