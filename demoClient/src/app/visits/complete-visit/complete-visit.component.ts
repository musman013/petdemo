import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Globals } from 'projects/fast-code-core/src/public_api';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { GlobalPermissionService } from 'src/app/core/global-permission.service';

@Component({
  selector: 'app-complete-visit',
  templateUrl: './complete-visit.component.html',
  styleUrls: ['./complete-visit.component.scss']
})
export class CompleteVisitComponent implements OnInit {
  
  title:string = "New Visits";
  itemForm: FormGroup;
  constructor(
		public formBuilder: FormBuilder,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<CompleteVisitComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public globalPermissionService: GlobalPermissionService,
	) {}
 
	ngOnInit() {
		this.setForm();
  }
 		
	setForm(){
 		this.itemForm = this.formBuilder.group({
      invoiceAmount: ['', Validators.required],
      visitNotes: ['', Validators.required],
    });
  }
  
  onSubmit(){
    this.dialogRef.close(this.itemForm.getRawValue());
  }

  onCancel(){
    this.dialogRef.close(null);
  }


    
}

