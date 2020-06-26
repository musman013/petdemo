import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSelectionList, MatListOption } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { FunctionalGroupService } from '../../services/functional-group.service';
import { ISelectGroupDialogConfig } from './select-group-dialog-config';

@Component({
  selector: 'app-select-group',
  templateUrl: './select-group.component.html',
  styleUrls: ['./select-group.component.scss']
})
export class SelectGroupComponent implements OnInit {
  functionalGroups: any[] = [];
  @ViewChild(MatSelectionList, { static: true }) selectionList: MatSelectionList;

  hasNextPage: boolean;

  groupForm: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ISelectGroupDialogConfig,
    private functionalGroupService: FunctionalGroupService,
    public dialogRef: MatDialogRef<SelectGroupComponent>,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit() {
    this.selectionList.selectedOptions = new SelectionModel<MatListOption>(false);

    this.groupForm = this.formBuilder.group({
      name: ['']
    });

    this.getFunctionalGroups();
  }

  getFunctionalGroups() {
    this.functionalGroupService.getFilteredGroups("", this.data.groupId, this.data.tenantId).subscribe((response) => {
      if(response)
        this.functionalGroups = response.data;
    })
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }

  onSelect() {
    let selectedOption = null;
    if (this.selectionList.selectedOptions.selected.length > 0)
      selectedOption = this.selectionList.selectedOptions.selected[0].value;
    this.dialogRef.close(selectedOption);
  }

  onSearch() {
    this.functionalGroupService.getFilteredGroups(this.groupForm.get('name').value, this.data.groupId, this.data.tenantId).subscribe((response) => {

      if (response) {
        this.functionalGroups = response.data;
      }
    });
  }

  submit($event) {
    $event.preventDefault();
    this.onSearch();
  }

}
