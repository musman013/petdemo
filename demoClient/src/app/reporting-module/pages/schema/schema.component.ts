import { Component, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { SchemaFileData } from '../../models/schema.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MainService } from '../../services/main.service';
import { MatAccordion } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-schema',
  templateUrl: './schema.component.html',
  styleUrls: ['./schema.component.scss']
})
export class SchemaComponent implements OnInit {
  @ViewChild(MatAccordion, { static: false }) accordion: MatAccordion;
  allSchemas = [];
  listFlexWidth = 30;
  allData = {};
  allValues = [];
  allFiles: Array<SchemaFileData> = [];
  fileContent: SchemaFileData = {
    content: this.translate.instant('REPORTING.LABELS.REPORT.FILE-CONTENT-TITLE'),
    absPath: '',
    fileName: ''
  };
  postContent: string = '';
  schemaFormGroup: FormGroup;
  selectedSchemas = [];

  constructor(
    private service: MainService,
    private fb: FormBuilder,
    private _snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.schemaFormGroup = this.fb.group({
      schemaList: this.fb.array([])
    });
  }

  get schemas() {
    return this.schemaFormGroup.get('schemaList') as FormArray;
  }

  addSchemas() {
    this.schemas.push(this.fb.control(''));
  }

  ngOnInit() {
    this.getAllDbTables();
    this.getAllSchemaFiles();
  }

  getAllDbTables() {
    this.service.getDbTablesList().subscribe(res => {
      this.allSchemas = Object.keys(res.tablesSchema);
      for (const k of this.allSchemas) {
        this.allData[k] = Object.keys(res.tablesSchema[k]);
        this.allValues.push(...Object.keys(res.tablesSchema[k]));
      }
      for (let val of Object.keys(this.allData)) {
        for (let v of this.allData[val]) {
          this.addSchemas();
        }
      }
    });
  }

  getAllSchemaFiles() {
    this.service.getSchemaFilesList().subscribe(res => {
      for (let v of Object.values(res.files)) {
        this.allFiles[v.fileName] = v;
      }
    });
  }

  checkSelectedList(i, s, t, e) {
    if (e.checked) {
      this.selectedSchemas.push(s + '.' + t);
    } else {
      this.selectedSchemas.splice(this.selectedSchemas.indexOf(s + '.' + t), 1);
    }
  }

  onClickGenerateSchema() {
    const tablesList = {
      tables: this.selectedSchemas
    };
    this.service.generateSchema(tablesList).subscribe(res => {
      this.service.generateAggregatedMeasures().subscribe(res2 => {
      });
      for (let v of Object.values(res.files)) {
        this.allFiles[v.fileName] = v;
      }
      this.showMessage(this.translate.instant('REPORTING.MESSAGES.SCHEMA-GENERATED'));
    });
  }

  showFile(f: SchemaFileData) {
    this.fileContent = f;
    this.postContent = f.content;
  }

  onClickUpdateSchema() {
    this.fileContent.content = this.postContent;
    this.service.updateSchemaFile(this.fileContent).subscribe(res => {
      this.getAllSchemaFiles();
      this.showMessage(this.translate.instant('REPORTING.MESSAGES.SCHEMA-FILE-UPDATED'));
    });
  }

  showMessage(msg): void {
    this._snackBar.open(msg, 'close', {
      duration: 2000,
    });
  }

}
