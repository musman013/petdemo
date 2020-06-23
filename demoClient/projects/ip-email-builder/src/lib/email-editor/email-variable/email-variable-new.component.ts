import { Component, OnInit, Inject } from '@angular/core';
import { EmailVariableService } from './email-variable.service';
import { IEmailVariable } from './iemail-variable';

import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BaseNewComponent, Globals, PickerDialogService, ErrorService, ValidatorsService } from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { IEmailVariableType } from './iemail-variable-type';
import { EmailVariablTypeService } from './email-variable.type.service';
import { PropertyType } from "projects/ip-email-builder/src/lib/email-editor/email-variable/property-type";

import { NativeDateAdapter, DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material';
import { formatDate, DatePipe } from '@angular/common';
import { FastCodeCoreService } from "projects/fast-code-core/src/lib/fast-code-core.service";

export const PICK_FORMATS = {
	parse: { dateInput: { month: 'short', year: 'numeric', day: 'numeric' } },
	display: {
		dateInput: 'input',
		monthYearLabel: { year: 'numeric', month: 'short' },
		dateA11yLabel: { year: 'numeric', month: 'long', day: 'numeric' },
		monthYearA11yLabel: { year: 'numeric', month: 'long' }
	}
}

class PickDateAdapter extends NativeDateAdapter {
	static selectedDropDownValue: any;
	format(date: Date, displayFormat: Object): string {

		if (displayFormat === 'input') {
			return formatDate(date, PickDateAdapter.selectedDropDownValue, this.locale);;
		} else {
			return date.toDateString();
		}
	}
}

@Component({
	selector: 'app-email-variable-new',
	templateUrl: './email-variable-new.component.html',
	styleUrls: ['./email-variable-new.component.scss'],
	providers: [
		{ provide: DateAdapter, useClass: PickDateAdapter },
		{ provide: MAT_DATE_FORMATS, useValue: PICK_FORMATS },
		[DatePipe]
	]
})
export class EmailVariableNewComponent extends BaseNewComponent<IEmailVariable> implements OnInit {

	selectedVariableType: any;
	selectedDropDownValue: any;
	listData: string[] = [];
	fileIds: number[] = [];
	attatchment: {
		myFile?: File;
		url?: any;
	}[] = [];

	showImageDropDown: boolean;
	showListButtonAndText: boolean;
	showImage: boolean;
	showPhone: boolean;
	showDecimalDropDown: boolean;
	showDatePicker: boolean;
	showDropDown: boolean;
	showInput: boolean = true;
	showTextArea: boolean;
	showLink: boolean;
	dropDownLabel: string = '';

	imageSrc: string;
	placeHolderValue = 'Default Value'



	dropdownValues: string[] = [];
	numberTypes: string[] = ['Integer', 'Decimal'];
	listType: string[] = ['Comma Seperated', 'Bullet Verticle List', 'Numbered Vertical List'];
	decimalDropDown: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
	imageDropDown: string[] = ['Horizontal', 'Verticle'];

	title: string = "Add Merge Field";
	entityName: string = 'EmailVariable';
	emailVariableType: IEmailVariableType[];
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<EmailVariableNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public global: Globals,
		public pickerDialogService: PickerDialogService,
		public dataService: EmailVariableService,
		public variableTypedataService: EmailVariablTypeService,
		public errorService: ErrorService,
		public datePipe: DatePipe,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, global, pickerDialogService, dataService, errorService);
	}

	ngOnInit() {
		this.itemForm = this.formBuilder.group({
			propertyName: ['', [Validators.required, ValidatorsService.alphanumericSpecialCharacterValidate]],
			propertyType: ['', Validators.required],
			defaultValue: ['', Validators.required],
			mergeType: ['']
		});
		super.ngOnInit();
		this.checkPassedData();
		this.variableTypedataService.getAll().subscribe(data => {
			this.emailVariableType = data;
		});
	}

	addOrRemoveValidation(fieldName, validations, addOrRemove) {
		if (addOrRemove) {
			this.itemForm.controls[fieldName].setValidators(validations);
		}
		else {
			this.itemForm.controls[fieldName].setValidators(null);
		}
	}
	// convenience getter for easy access to form fields
	getSelectedVariableType(event) {
		this.itemForm.controls.defaultValue.setValue('');
		this.itemForm.controls.mergeType.setValue('');
		this.selectedVariableType = event.value;
		//remove previous validation if any previous
		this.addOrRemoveValidation('defaultValue', [], false);
		this.addOrRemoveValidation('mergeType', [], false);

		switch (this.selectedVariableType) {
			case PropertyType.TEXT:
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [Validators.required, Validators.maxLength(256)], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.PERCENTAGE:
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.percentageValidation], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.EMAIL:
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.emailValidation], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.HYPERLINK:
				this.placeHolderValue = 'Enter Link';
				this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.websiteValidate], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.MULTILINE:
				this.addOrRemoveValidation('defaultValue', [Validators.required, Validators.maxLength(512)], true);
				this.showHideSeperateFields(false, true, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.DATE:
				this.addOrRemoveValidation('defaultValue', [Validators.required], true);
				this.showHideSeperateFields(false, false, true, false, false, false, false, false, false, false);
				this.dropDownLabel = 'Select Date Format';
				this.getDropDownValue();
				break;
			case PropertyType.CURRENCY:
				this.showHideSeperateFields(false, false, true, false, false, false, false, false, false, false);
				this.dropDownLabel = 'Select Currency Type';
				this.addOrRemoveValidation('defaultValue', [ValidatorsService.decimalPrecisionTwo], true);
				this.getDropDownValue();
				this.placeHolderValue = 'Enter Currency';
				break;
			case PropertyType.NUMBER:
				this.showHideSeperateFields(true, false, true, false, false, false, false, false, false, false);
				this.dropDownLabel = 'Select Number Type';
				this.getDropDownValue();
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [ValidatorsService.integer], true);
				break;
			case PropertyType.PHONE:
				this.showHideSeperateFields(false, false, false, false, false, true, false, false, false, false);
				break;
			case PropertyType.IMAGE:
				this.showHideSeperateFields(false, false, false, false, false, false, true, false, false, false)
				break;
			case PropertyType.LIST:
				this.showHideSeperateFields(true, false, true, false, false, false, false, true, false, false);
				this.dropDownLabel = 'Select Storage Type';
				this.getDropDownValue();
				this.addOrRemoveValidation('defaultValue', [Validators.required], true);
				this.placeHolderValue = 'Default value';
				break;
			case PropertyType.CLICKABLE_IMAGE:
				this.placeHolderValue = 'Enter Link';
				this.addOrRemoveValidation('mergeType', [ValidatorsService.websiteValidate], true);
				this.showHideSeperateFields(false, false, false, false, false, false, true, false, true, false);
				break;
			case PropertyType.LIST_OF_IMAGES:
				this.showHideSeperateFields(false, false, true, false, false, false, true, false, false, true)
				this.getDropDownValue();
				this.dropDownLabel = "Enter Position";
				break;
			default:
				console.log("something else sele");

		}
	}

	getDropDownValue() {
		switch (this.selectedVariableType) {
			case PropertyType.DATE:
			case PropertyType.CURRENCY:
				this.variableTypedataService.getAllMasterValue(this.selectedVariableType).subscribe(data => {
					this.dropdownValues = data;
				});
				break;
			case PropertyType.NUMBER:
				this.dropdownValues = this.numberTypes;
				break;
			case PropertyType.LIST:
				this.dropdownValues = this.listType;
				break;
			case PropertyType.LIST_OF_IMAGES:
				this.dropdownValues = this.imageDropDown;
				break;

		}

	}

	dropDownValueChanged(event) {
		this.selectedDropDownValue = event.value;
		this.itemForm.controls.defaultValue.setValue('');
		switch (this.selectedVariableType) {
			case PropertyType.DATE:
				this.showDatePicker = true;
				this.showDecimalDropDown = false;
				PickDateAdapter.selectedDropDownValue = event.value;
				break;
			case PropertyType.CURRENCY:
				this.showInput = true;
				break;
			case PropertyType.NUMBER:
				this.addOrRemoveValidation('defaultValue', [], false);

				if (this.selectedDropDownValue == 'Decimal') {
					//	this.showDecimalDropDown = true;
					this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.decimalPrecision], true);
				}
				else {
					//	this.showDecimalDropDown = false;
					this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.integer], true);
				}
				break;
			case PropertyType.LIST_OF_IMAGES:
				break;


		}

	}



	showHideSeperateFields(showInut, showTextArea, showDropDown, showDecimalDropDown, showDatePicker, showPhone, showImage, showListButtonAndText, showLink, showImageDropDown) {
		this.showInput = showInut;
		this.showTextArea = showTextArea;
		this.showDropDown = showDropDown;
		this.showDecimalDropDown = showDecimalDropDown;
		this.showDatePicker = showDatePicker;
		this.showPhone = showPhone;
		this.showImage = showImage;
		this.showListButtonAndText = showListButtonAndText;
		this.showLink = showLink;
		this.showImageDropDown = showImageDropDown;
	}

	decimalDropDownChange(event) {
		let decimalPoints = event.value;
		ValidatorsService.digit = decimalPoints;
		this.addOrRemoveValidation('defaultValue', [ValidatorsService.decimalPrecision], true);
	}

	readURL(event: any): void {
		if (event.target.files && event.target.files[0]) {

			if (this.selectedVariableType == PropertyType.IMAGE) {
				this.attatchment = [];
				// this.myFiles = [];
				// this.urlArray=[];
			}
			if (event.target.files[0].type === 'image/jpeg' ||
				event.target.files[0].type === 'image/png' ||
				event.target.files[0].type === 'image/jpg') {
				if (event.target.files[0].size < 50000000) {
					let obj = {};
					const file = event.target.files[0];
					obj['myFile'] = event.target.files[0];
					const reader = new FileReader();
					reader.readAsDataURL(file); // read file as data url

					reader.onload = (event) => { // called once readAsDataURL is completed
						obj['url'] = reader.result;
						// this.urlArray.push(reader.result);
					}
					this.attatchment.push(obj);

				}
				else {
					this.itemForm.controls.defaultValue.setValue('');
					alert("File size should be less than 2 MB")
					if (this.selectedVariableType == PropertyType.IMAGE) {
						this.attatchment = [];
					}
				}
			}
			else {
				this.itemForm.controls.defaultValue.setValue('');
				alert("Please enter image");
				if (this.selectedVariableType == PropertyType.IMAGE) {
					this.attatchment = [];
				}
			}

		}
	}



	addNewItem() {
		this.listData.push(this.itemForm.controls.defaultValue.value)
	}

	removeListItem(index) {
		this.listData.splice(index, 1);
	}

	removeImage(index) {
		this.attatchment.splice(index, 1)
	}
}
