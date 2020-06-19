import { Component, OnInit, Inject, ViewChild } from '@angular/core';

import { IPickerItem } from './ipicker-item';
import { IFCDialogConfig } from './ifc-dialog-config';

import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSelectionList, MatListOption } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
@Component({
    selector: 'app-picker',
  templateUrl: './picker.component.html',
  styleUrls: ['./picker.component.scss']
})
export class PickerComponent implements OnInit {
   
  loading = false;
  submitted = false;
  title: string;
  items:IPickerItem[]=[];
  @ViewChild(MatSelectionList,{  static: false }) selectionList: MatSelectionList;

  selectedItem:IPickerItem;
  selectedItems:IPickerItem[]=[];
  errorMessage = '';
    constructor(    public dialogRef: MatDialogRef<PickerComponent>,
       @Inject(MAT_DIALOG_DATA) public data: IFCDialogConfig
     //  @Inject(MAT_DIALOG_DATA) public data: Observable<IPickerItem[]>
        ) { }
 
    ngOnInit() {
      if(this.data.IsSingleSelection)
        this.selectionList.selectedOptions = new SelectionModel<MatListOption>(false);
      this.title = this.data.Title;
      this.data.DataSource.subscribe(  items => {
        this.items = items;
       // this.users[0].firstName
      },
      error => this.errorMessage = <any>error);
     /*  this.permissionService.getAll().subscribe(
        items => {
          this.items = items;
         // this.users[0].firstName
        },
        error => this.errorMessage = <any>error
       );*/
    }
 
    // convenience getter for easy access to form fields
  
 
   /* onAdd(item:IPickerItem) {
    
       if(this.data.IsSingleSelection)
          this.selectedItem = item;    
    }*/
    onOk() {
      //   return;
     // if(this.selectedItem)
     let selectedOptions =this.selectionList.selectedOptions.selected;
     if(selectedOptions.length > 0)
     {
      for(let option of selectedOptions) {
        this.selectedItems.push(option.value);
      }
      this.dialogRef.close( this.data.IsSingleSelection?this.selectedItems[0]:
        this.selectedItems);
     }
       

     }
    onCancel(): void {
        this.dialogRef.close();
      }
    
}
