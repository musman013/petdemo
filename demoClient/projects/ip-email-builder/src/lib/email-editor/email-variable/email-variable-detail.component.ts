import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { IEmailVariableType } from './iemail-variable-type';
import { EmailVariableService } from './email-variable.service';
import { IEmailVariable } from './iemail-variable';
import { BaseDetailsComponent, Globals, PickerDialogService, ErrorService, ValidatorsService } from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { EmailVariablTypeService } from "projects/ip-email-builder/src/lib/email-editor/email-variable/email-variable.type.service";
import { PropertyType } from "projects/ip-email-builder/src/lib/email-editor/email-variable/property-type";
import { NativeDateAdapter, DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material';
import { formatDate, DatePipe } from '@angular/common';
import { first } from 'rxjs/operators';

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
			return formatDate(date, PickDateAdapter.selectedDropDownValue, this.locale);
		} else {
			return date.toDateString();
		}
	}
}

@Component({
	selector: 'app-role-detail',
	templateUrl: './email-variable-detail.component.html',
	styleUrls: ['./email-variable-detail.component.scss'],
	providers: [
		{ provide: DateAdapter, useClass: PickDateAdapter },
		{ provide: MAT_DATE_FORMATS, useValue: PICK_FORMATS },
		[DatePipe]
	]
})
export class EmailVariableDetailComponent extends BaseDetailsComponent<IEmailVariable> implements OnInit {
	selectedVariableType: any;
	selectedDropDownValue: any;
	listData: string[] = [];
	fileIds: number[] = [];
	attatchment: {
		myFile?: File;
		url?: any;
	}[] = [];

	decimalValue: any;
	showListButtonAndText: boolean;
	title: string = 'Email Merge Fields';
	parentUrl: string = './emailvariables';
	entityName: string = 'EmailVariable';
	emailVariableType: IEmailVariableType[];

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

	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public global: Globals,
		public variableTypedataService: EmailVariablTypeService,
		public pickerDialogService: PickerDialogService,
		public dataService: EmailVariableService,
		public errorService: ErrorService,
		public datePipe: DatePipe

	) {
		super(formBuilder, router, route, dialog, global, pickerDialogService, dataService, errorService);
		var u = this.route.parent.toString();
	}

	ngOnInit() {
		super.ngOnInit();
		this.setForm();
		this.getItem();
		this.variableTypedataService.getAll().subscribe(data => {
			this.emailVariableType = data;
		});
	}

	setForm() {
		this.itemForm = this.formBuilder.group({
			id: [''],
			propertyName: ['', Validators.required],
			propertyType: ['', Validators.required],
			defaultValue: ['', Validators.required],
			mergeType: ['']
		});
	}

	onItemFetched(item: IEmailVariable) {
		this.item = item;
		this.itemForm.patchValue(item);
		let obj = {};
		obj['value'] = item.propertyType;
		this.getSelectedVariableType(obj, false);
		console.log(this.item);
		this.attatchment = [];
		let allimage: any[] = this.item.defaultValue.split(',');
		allimage.forEach(src => {
			let obj = {};
			obj['url'] = 'api/files/' + src;
			this.attatchment.push(obj);
		})
	}


	addOrRemoveValidation(fieldName, validations, addOrRemove) {
		if (addOrRemove) {
			this.itemForm.controls[fieldName].setValidators(validations);
		}
		else {
			this.itemForm.controls[fieldName].setValidators(null);
		}
	}

	getSelectedVariableType(event, onChange) {
		console.log(event);
		if (onChange) {
			this.itemForm.controls.defaultValue.setValue('');
			this.itemForm.controls.mergeType.setValue('');
		}
		this.selectedVariableType = event.value;
		//remove previous validation if any previous
		this.addOrRemoveValidation('defaultValue', [], false);
		this.addOrRemoveValidation('mergeType', [], false);

		switch (this.selectedVariableType) {
			case PropertyType.TEXT:
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [Validators.required, Validators.maxLength(256)], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.PERCENTAGE:
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.percentageValidation], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.EMAIL:
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.emailValidation], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.HYPERLINK:
				this.placeHolderValue = 'Enter Link';
				this.addOrRemoveValidation('defaultValue', [Validators.required, ValidatorsService.websiteValidate], true);
				this.showHideSeperateFields(true, false, false, false, false, false, false, false, false);
				break;
			case PropertyType.MULTILINE:
				this.addOrRemoveValidation('defaultValue', [Validators.required, Validators.maxLength(512)], true);
				this.showHideSeperateFields(false, true, false, false, false, false, false, false, false);
				break;
			case PropertyType.DATE:
				this.addOrRemoveValidation('defaultValue', [Validators.required], true);
				this.showHideSeperateFields(false, false, true, false, false, false, false, false, false);
				this.dropDownLabel = 'Select Date Format';
				this.getDropDownValue();
				let obj = {};
				obj['value'] = this.itemForm.controls.mergeType.value;
				this.dropDownValueChanged(obj, false);
				break;
			case PropertyType.CURRENCY:
				this.showHideSeperateFields(true, false, true, false, false, false, false, false, false);
				this.dropDownLabel = 'Select Currency Type';
				this.addOrRemoveValidation('defaultValue', [ValidatorsService.decimalPrecisionTwo], true);
				this.getDropDownValue();
				this.placeHolderValue = 'Enter Currency';
				break;
			case PropertyType.NUMBER:
				this.showHideSeperateFields(true, false, true, false, false, false, false, false, false);
				this.dropDownLabel = 'Select Number Type';
				this.getDropDownValue();
				this.placeHolderValue = 'Default value';
				this.addOrRemoveValidation('defaultValue', [ValidatorsService.integer], true);
				let obj2 = {};
				obj2['value'] = this.itemForm.controls.mergeType.value;
				this.dropDownValueChanged(obj2, false);
				break;
			case PropertyType.PHONE:
				this.showHideSeperateFields(false, false, false, false, false, true, false, false, false);
				break;
			case PropertyType.IMAGE:
				this.showHideSeperateFields(false, false, false, false, false, false, true, false, false)
				break;
			case PropertyType.LIST:
				this.showHideSeperateFields(true, false, true, false, false, false, false, true, false);
				this.dropDownLabel = 'Select Storage Type';
				this.getDropDownValue();
				this.addOrRemoveValidation('defaultValue', [Validators.required], true);
				this.placeHolderValue = 'Default value';
				break;
			case PropertyType.CLICKABLE_IMAGE:
				this.placeHolderValue = 'Enter Link';
				this.addOrRemoveValidation('mergeType', [ValidatorsService.websiteValidate], true);
				this.showHideSeperateFields(false, false, false, false, false, false, true, false, true);
				break;
			case PropertyType.LIST_OF_IMAGES:
				this.showHideSeperateFields(false, false, true, false, false, false, true, false, false);
				this.getDropDownValue();
				this.dropDownLabel = "Enter Position";
				break;
			default:
				console.log("something else sele");

		}
	}

	getDropDownValue() {
		this.selectedDropDownValue = this.itemForm.controls.mergeType.value;
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
				let value = this.itemForm.controls.defaultValue.value;
				this.listData = value.split(',');
				break;
			case PropertyType.LIST_OF_IMAGES:
				this.dropdownValues = this.imageDropDown;
				break;
		}

	}


	showHideSeperateFields(showInut, showTextArea, showDropDown, showDecimalDropDown, showDatePicker, showPhone, showImage, showListButtonAndText, showLink) {
		this.showInput = showInut;
		this.showTextArea = showTextArea;
		this.showDropDown = showDropDown;
		this.showDecimalDropDown = showDecimalDropDown;
		this.showDatePicker = showDatePicker;
		this.showPhone = showPhone;
		this.showImage = showImage;
		this.showListButtonAndText = showListButtonAndText;
		this.showLink = showLink;
	}


	dropDownValueChanged(event, check) {
		if (check) {
			this.itemForm.controls.defaultValue.setValue('');
		}
		this.selectedDropDownValue = event.value;
		switch (this.selectedVariableType) {
			case PropertyType.DATE:
				this.showDatePicker = true;
				PickDateAdapter.selectedDropDownValue = event.value;
				this.itemForm.controls.defaultValue.setValue(new Date(this.itemForm.controls.defaultValue.value));
				break;
			case PropertyType.NUMBER:
				if (this.selectedDropDownValue != 'Integer') {
					//this.showDecimalDropDown = true;
					this.selectedDropDownValue = 'Decimal';
					this.addOrRemoveValidation('defaultValue', [ValidatorsService.decimalPrecision], true);
					//this.decimalValue=this.itemForm.controls.mergeType.value;					
				}
				else {
					this.showDecimalDropDown = false;

				}
				break;
			case PropertyType.LIST:
				break;

		}

	}

	decimalDropDownChange(event) {
		let decimalPoints = event.value;
		this.addOrRemoveValidation('defaultValue', [ValidatorsService.decimalPrecision], true);
	}


	readURL(event: any): void {
		if (event.target.files && event.target.files[0]) {

			if (this.selectedVariableType == PropertyType.IMAGE) {
				this.attatchment = [];
			}
			if (event.target.files[0].type === 'image/jpeg' ||
				event.target.files[0].type === 'image/png' ||
				event.target.files[0].type === 'image/jpg') {
				if (event.target.files[0].size < 50000000) {
					let obj = {}
					const file = event.target.files[0];
					obj['myFile'] = event.target.files[0];
					const reader = new FileReader();
					reader.readAsDataURL(file); // read file as data url
					reader.onload = (event) => { // called once readAsDataURL is completed
						obj['url'] = reader.result
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

	/**
 * Gets data from item form and calls
 * service method to update the item.
 */
	onSubmit() {
		let check = true;
		if (this.itemForm.invalid) {
			return;
		}

		this.submitted = true;
		this.loading = true;
		//
		switch (this.selectedVariableType) {
			case PropertyType.DATE:
				this.itemForm.controls.defaultValue.setValue(this.datePipe.transform(this.itemForm.controls.defaultValue.value, this.selectedDropDownValue));
				break;
			case PropertyType.LIST:
				if (this.listData && this.listData.length > 0) {
					this.itemForm.controls.defaultValue.setValue(this.listData.join(','));
				}
				break;
			case PropertyType.IMAGE:
			case PropertyType.CLICKABLE_IMAGE:
			case PropertyType.LIST_OF_IMAGES:
				check = false;
				this.saveAttachments();
				//need to check this code
				break;
		}
		if (check) {
			this.dataService.update(this.itemForm.getRawValue(), this.idParam)
				.pipe(first())
				.subscribe(
					data => {
						this.loading = false;
						this.router.navigate([this.parentUrl], { relativeTo: this.route.parent });
					},
					error => {
						this.errorService.showError("Error Occured while updating");
						this.loading = false;
					});
		}
	}

	saveAttachments() {
		if (this.attatchment && this.attatchment.length > 0) {
			this.attatchment.forEach(file => {

				if (file.myFile.name) {
					const fileMetadata = {
						name: file.myFile.name, summary: file.myFile.name
					};
					this.dataService.createFileMetadata(fileMetadata).subscribe(res => {
						console.log("response is", res);
						this.dataService.uploadFile(res.id, file.myFile).subscribe(res2 => {
							this.fileIds.push(res.id);
							this.itemForm.controls.defaultValue.setValue(this.fileIds.join(','));
						});
					});
				}
			});
		}

		setTimeout(() => this.dataService.update(this.itemForm.getRawValue(), this.idParam)
			.pipe(first())
			.subscribe(
				data => {
					this.loading = false;
					this.router.navigate([this.parentUrl], { relativeTo: this.route.parent });
				},
				error => {
					this.errorService.showError("Error Occured while updating");
					this.loading = false;
				}), 3000);

	}

}
