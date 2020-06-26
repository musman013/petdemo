import { Component, OnInit, Input, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';

import { FormService } from './form.service';
import { UserService } from '../common/services/user.service';
import { FunctionalGroupService } from '../common/services/functional-group.service';
@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit, OnChanges {

  @Input() formData: any;
  @Input() taskId: any;
  @Input() outcomesOnly: boolean;
  @Input() hideButtons: any;
  @Input() disableForm: any;
  @Input() disableFormText: any;
  @Input() disableOutcomes: any;
  
  @Output() onTaskCompletion: EventEmitter<any> = new EventEmitter();


  form: FormGroup
  loading = false;
  uploadInProgress: false;

  processDefinitionId: string;
  caseDefinitionId: string;

  constructor(
    private translate: TranslateService,
    private formBuilder: FormBuilder,
    private formService: FormService,
    private userService: UserService,
    private functionalGroupService: FunctionalGroupService
  ) { }

  ngOnInit() {
    // this.prepareForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      // only run when property "formData" changed
      if (propName === 'formData') {
        //  update formData value when a task is selected 
        this.formData = changes[propName].currentValue;
        this.prepareForm();
      }
    }
  }

  prepareForm() {
    this.form = this.formBuilder.group({
    });

    this.formData.fields.forEach((field, index) => {
      this.preProcessField(field, index);
      let ctrl = new FormControl(
        {
          value: field.value,
          disabled: field.readOnly
        }
      )

      if (field.readOnly && !field.value) {
        ctrl.setValue("(empty)");
      }

      // adding validators to field

      let newValidators = [];
      let existingValidator = ctrl.validator;

      // required
      if (field.required) {
        newValidators.push(Validators.required);
      }

      if (field.params) {
        if (['integer', 'decimal'].includes(field.type)) {
          // minimum value
          if (field.params.minLength) {
            newValidators.push(Validators.min(field.params.minLength));
          }

          // maximum value
          if (field.params.maxLength) {
            newValidators.push(Validators.max(field.params.maxLength));
          }
        }
        else if (['text', 'multi-line-text']) {
          // minimum length
          if (field.params.minLength) {
            newValidators.push(Validators.minLength(field.params.minLength));
          }

          // maximum length
          if (field.params.maxLength) {
            newValidators.push(Validators.maxLength(field.params.maxLength));
          }
        }
      }

      if (newValidators.length > 0) {
        if (existingValidator && existingValidator.length > 0)
          newValidators = newValidators.concat(existingValidator);

        ctrl.setValidators(newValidators)
      }

      this.form.addControl(field.id, ctrl)
    });
  }

  getDefaultCompleteButtonText() {
    if (this.processDefinitionId) {
      return this.translate.instant('FORM.DEFAULT-OUTCOME.START-PROCESS');
    } else if (this.caseDefinitionId) {
      return this.translate.instant('FORM.DEFAULT-OUTCOME.START-CASE');
    } else {
      return this.translate.instant('FORM.DEFAULT-OUTCOME.COMPLETE');
    }
  };

  updateFormDataFieldvalues() {
    let formValues = this.form.value;
    let newFields = [];
    this.formData.fields.forEach(field => {
      if (!field.readOnly) {
        field.value = formValues[field.id]
      }
      newFields.push(field);
    });
    this.formData.fields = newFields;
  };

  saveForm() {
    debugger;
    this.loading = true;
    // Prep data
    this.updateFormDataFieldvalues();
    var postData: any = this.createPostData();
    postData.formId = this.formData.id;
    this.formService.saveTaskForm(this.taskId, postData).subscribe((data) => {
      console.log("Save Form Data :", data, this.taskId);
      this.loading = false;
    });
  };

  completeForm(outcome) {

    this.loading = true;

    // Prep data
    this.updateFormDataFieldvalues();
    var postData: any = this.createPostData();
    postData.formId = this.formData.id;

    if (outcome) {
      postData.outcome = outcome.name;
    }

    this.formService.completeTaskForm(this.taskId, postData).subscribe((data) => {
      this.loading = false;
      this.onTaskCompletion.emit();
    }
      // function (errorResponse) {
      //   this.completeButtonDisabled = false;
      //   this.loading = false;
      //   this.$emit('task-completed-error', {
      //     taskId: this.taskId,
      //     error: errorResponse
      //   });
      // }
    );

  };

  createPostData() {
    var postData = { values: {} };
    if (!this.formData.fields) return postData;

    this.formData.fields.forEach(field => {
      if (field.type === 'boolean' && field.value == null) {
        field.value = false;
      }

      if (field && field.type !== 'expression' && !field.readOnly) {

        if (field.type === 'dropdown' && field.hasEmptyValue !== null && field.hasEmptyValue !== undefined && field.hasEmptyValue === true) {

          // Manually filled dropdown
          if (field.options !== null && field.options !== undefined && field.options.length > 0) {

            var emptyValue = field.options[0];
            if (field.value != null && field.value != undefined && emptyValue.name !== field.value.name) {
              postData.values[field.id] = field.value;
            }
          }

        } else if (field.type === 'date' && field.value) {
          postData.values[field.id] = field.value.getFullYear() + '-' + (field.value.getMonth() + 1) + '-' + field.value.getDate();

        } else {
          postData.values[field.id] = field.value;
        }
      }
    });

    return postData;
  };

  // Pre-process any previous values, if needed
  preProcessField(field, i) {

    // set visibility bool if no condition is present
    if (!field.visibilityCondition) {
      field.isVisible = true;
    }

    if (field.type == 'dropdown' && field.value && field.options && !field.readOnly) {
      for (var j = 0; j < field.options.length; j++) {
        if (field.options[j].name == field.value) {
          field.value = field.options[j];
          break;
        }
      }

    }
    else if (field.type == 'date' && field.value && !field.readOnly) {
      var dateArray = field.value.split('-');
      if (dateArray && dateArray.length == 3) {
        field.value = new Date(dateArray[0], dateArray[1] - 1, dateArray[2]);
      }
    }
    else if (field.type == 'people' && field.value) {
      this.userService.getUserInfo(field.value).subscribe((userInfoFormObject) => {
        field.value = userInfoFormObject;
      });

    }
    else if (field.type == 'functional-group' && field.value) {
      this.functionalGroupService.getGroupInfo(field.value).subscribe(group => {
        field.value = group;
      });

    };
  }
}
