import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { IEntityHistory } from './entityHistory';
import { EntityHistoryService } from './entity-history.service';
import { MatTableDataSource, MatDialog, MatDialogRef } from "@angular/material";

import { of as observableOf, Observable } from 'rxjs';

import { ErrorService, PickerDialogService, ISearchField, operatorType, Globals } from 'projects/fast-code-core/src/public_api';
import { FormBuilder, FormGroup } from '@angular/forms';

import { UserService, IUser } from '../user/index';

enum listProcessingType {
  Replace = "Replace",
  Append = "Append"
}

@Component({
  selector: 'app-entity-history',
  templateUrl: './entity-history.component.html',
  styleUrls: ['./entity-history.component.scss']
})

export class EntityHistoryComponent implements OnInit {
  title: string = "Entity History";
  entityHistory: IEntityHistory[] = [];
  itemsObservable: Observable<IEntityHistory[]>;
  errorMessage: '';
  displayedColumns: string[] = ['entity', 'cdoId', 'changeType', 'author', 'commitDate', 'propertyName', 'previousValue', 'currentValue'];

  public dataSource;
  userList: IUser[] = [];
  pickerDialogRef: MatDialogRef<any>;
  IsCreatePermission: boolean = true;
  isMediumDeviceOrLess: boolean;
  dialogRef: MatDialogRef<any>;
  mediumDeviceOrLessDialogSize: string = "100%";
  largerDeviceDialogWidthSize: string = "75%";
  largerDeviceDialogHeightSize: string = "75%";

  states: string[] = [
    'Alabama',
  ];
  state: string = "All";

  filterFields = [];
  basicFilterForm: FormGroup;

  constructor(
    private global: Globals,
    private entityHistoryService: EntityHistoryService,
    private changeDetectorRefs: ChangeDetectorRef,
    public dialog: MatDialog,
    public errorService: ErrorService,
    public userService: UserService,
    public pickerDialogService: PickerDialogService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit() {
    this.manageScreenResizing();
    this.getEntityHistory();

    this.basicFilterForm = this.formBuilder.group({
      author: [''],
      from: [''],
      to: ['']
    });

    this.basicFilterForm.get('author').valueChanges.subscribe(value => this.onPickerSearch(value));
  }

  getEntityHistory() {
    this.isLoadingResults = true;
    this.initializePageInfo();
    this.itemsObservable = this.entityHistoryService.getAll(this.searchValue, this.currentPage * this.pageSize, this.pageSize);
    this.processListObservable(this.itemsObservable, listProcessingType.Replace);
  }

  manageScreenResizing() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
      if (value)
        this.displayedColumns = ['entity', 'cdoId', 'commitDate', 'author'];
      else
        this.displayedColumns = ['entity', 'cdoId', 'changeType', 'author', 'commitDate', 'propertyName', 'previousValue', 'currentValue']

      if (this.dialogRef)
        this.dialogRef.updateSize(value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogWidthSize,
          value ? this.mediumDeviceOrLessDialogSize : this.largerDeviceDialogHeightSize);
    });
  }

  createSearchString() {
    let searchString: string = "";
    let searchFormValue = this.basicFilterForm.getRawValue();
    if (searchFormValue.author) {
      searchString += "author=" + searchFormValue.author;
    }

    if (searchFormValue.from) {
      if (searchString.length > 0) {
        searchString += ";";
      }
      let from = new Date(searchFormValue.from);
      searchString += "from=" + from.getFullYear() + "-" + (from.getMonth() + 1) + "-" + from.getDate() + " " + from.getHours() + ":" + from.getMinutes() + ":" + from.getSeconds() + "." + from.getMilliseconds();
    }

    if (searchFormValue.to) {
      if (searchString.length > 0) {
        searchString += ";";
      }
      let to = new Date(searchFormValue.to);
      searchString += "to=" + to.getFullYear() + "-" + (to.getMonth() + 1) + "-" + to.getDate() + " " + to.getHours() + ":" + to.getMinutes() + ":" + to.getSeconds() + "." + to.getMilliseconds();
    }

    return searchString;
  }

  applyFilter() {
    this.searchValue = this.createSearchString();
    this.isLoadingResults = true;
    this.initializePageInfo();
    this.itemsObservable = this.entityHistoryService.getAll(
      this.searchValue,
      this.currentPage * this.pageSize,
      this.pageSize,
    )
    this.processListObservable(this.itemsObservable, listProcessingType.Replace);
  }

  isLoadingResults = true;

  currentPage: number;
  pageSize: number;
  lastProcessedOffset: number;
  hasMoreRecords: boolean;
  searchValue: string = "";

  initializePageInfo() {
    this.hasMoreRecords = true;
    this.pageSize = 10;
    this.lastProcessedOffset = -1;
    this.currentPage = 0;
  }

  //manage pages for virtual scrolling
  updatePageInfo(data) {
    if (data.length > 0) {
      this.currentPage++;
      this.lastProcessedOffset += data.length;
    }
    else {
      this.hasMoreRecords = false;
    }
  }

  onTableScroll() {
    if (!this.isLoadingResults && this.hasMoreRecords && this.lastProcessedOffset < this.entityHistory.length) {
      this.isLoadingResults = true;
      this.itemsObservable = this.entityHistoryService.getAll(this.searchValue, this.currentPage * this.pageSize, this.pageSize);
      this.processListObservable(this.itemsObservable, listProcessingType.Append);
    }
  }

  processListObservable(listObservable: Observable<IEntityHistory[]>, type: listProcessingType) {
    listObservable.subscribe(
      entityHistory => {
        this.isLoadingResults = false;
        if (type == listProcessingType.Replace) {
          this.entityHistory = entityHistory;
          this.dataSource = new MatTableDataSource(this.entityHistory);
        }
        else {
          this.entityHistory = this.entityHistory.concat(entityHistory);
          this.dataSource = new MatTableDataSource(this.entityHistory);
        }
        this.updatePageInfo(entityHistory);
      },
      error => {
        this.errorMessage = <any>error
        this.errorService.showError("An error occured while fetching results");
      }
    )
  }


  /**
   * Author list processing
   */

  getUsers() {
    this.userService.getAll(this.searchValuePicker, this.currentPickerPage * this.pickerPageSize, this.pickerPageSize).subscribe(items => {
      console.log('MyItems :', items);
      this.userList = items;
    })
  }

  isLoadingPickerResults = true;

  currentPickerPage: number;
  pickerPageSize: number;
  lastProcessedOffsetPicker: number;
  hasMoreRecordsPicker: boolean;

  searchValuePicker: ISearchField[] = [];
  pickerItemsObservable: Observable<any>;

  /**
   * Initializes/Resets paging information of user data list 
   * showing in autocomplete options.
   */
  initializePickerPageInfo() {
    this.hasMoreRecordsPicker = true;
    this.pickerPageSize = 30;
    this.lastProcessedOffsetPicker = -1;
    this.currentPickerPage = 0;
  }

  /**
   * Manages paging for virtual scrolling for user data list 
   * showing in autocomplete options.
   * @param data Item data from the last service call.
   */
  updatePickerPageInfo(data) {
    if (data.length > 0) {
      this.currentPickerPage++;
      this.lastProcessedOffsetPicker += data.length;
    }
    else {
      this.hasMoreRecordsPicker = false;
    }
  }

  /**
   * Loads more user data when 
   * list is scrolled to the bottom (virtual scrolling).
   */
  onPickerScroll() {
    if (!this.isLoadingPickerResults && this.hasMoreRecordsPicker && this.lastProcessedOffsetPicker < this.userList.length) {
      this.isLoadingPickerResults = true;
      this.userService.getAll(this.basicFilterForm.get('author').value, this.currentPickerPage * this.pickerPageSize, this.pickerPageSize).subscribe(
        items => {
          this.isLoadingPickerResults = false;
          this.userList = this.userList.concat(items);
          this.updatePickerPageInfo(items);
        },
        error => {
          this.errorMessage = <any>error;
          this.errorService.showError("An error occured while fetching more results");
        }
      );
    }
  }

  /**
   * Loads the user data meeting given criteria.
   * @param searchValue Filters to be applied.
   */
  onPickerSearch(searchValue: string) {

    let searchField: ISearchField = {
      fieldName: "userName",
      operator: operatorType.Contains,
      searchValue: searchValue ? searchValue : ""
    }
    this.searchValuePicker = [searchField];
    this.getUsers();
  }

}