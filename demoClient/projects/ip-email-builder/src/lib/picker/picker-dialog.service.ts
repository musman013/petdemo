import { Injectable, ChangeDetectorRef } from '@angular/core';

import { Observable, throwError } from 'rxjs';
import { Globals } from '../globals';
import { MatDialog, MatDialogRef } from '@angular/material';
import { IPickerItem } from './ipicker-item';
//import { PermissionService } from '../permissions/permission.service';
import {PickerComponent} from '../picker/picker.component';

export interface IFCDialogConfig {
 DataSource: Observable<any>;
 Title: string;
 //OnClose: Observable<any>;

}
@Injectable({
  providedIn: 'root'
})
export class PickerDialogService  {
  //environment.apiUrl;//'https://jsonplaceholder.typicode.com/users';
 
  isSmallDeviceOrLess:boolean;
  dialogRef: MatDialogRef <any> ;
  smallDeviceOrLessDialogSize:string = "100%";
  largerDeviceDialogWidthSize:string = "50%";
  largerDeviceDialogHeightSize:string = "100%";
  constructor(private global:Globals, 
    public dialog: MatDialog) { 
      this.global.isSmallDevice$.subscribe(value=> {
        this.isSmallDeviceOrLess=value;
        if(this.dialogRef)
        this.dialogRef.updateSize(value?this.smallDeviceOrLessDialogSize:this.largerDeviceDialogWidthSize,
          value?this.smallDeviceOrLessDialogSize:this.largerDeviceDialogHeightSize);
       
      });
  }
  open(config:IFCDialogConfig):Observable<any> {
   // let permissionLookUpObs =   this.permissionService.getAll();
    this.dialogRef = this.dialog.open(PickerComponent,{data:config, disableClose: true,
    height: '100%',
     width: this.isSmallDeviceOrLess? this.smallDeviceOrLessDialogSize:this.largerDeviceDialogWidthSize,
     maxWidth:"none",      
   panelClass:'fc-modal-dialog'} ); 
   return this.dialogRef.afterClosed();
  // config.OnClose = this.dialogRef.afterClosed();
   /*this.dialogRef.afterClosed().subscribe(result => {
     console.log(`Dialog result: ${result}`);
     
     if(result) {
      // result.userId = this.userId;
      // this.permissionService.update(result, result.id);
       //this.permissions = [...this.permissions,result];
      // this.changeDetectorRefs.detectChanges();
     }
   });*/
  }
  
}