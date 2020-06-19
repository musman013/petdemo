import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material';
import { IEmailTemplate } from './iemail-template';
import { EmailTemplateService } from './email-template.service';

import { Router, ActivatedRoute } from '@angular/router';
import { BaseListComponent, IListColumn, listColumnType, Globals, PickerDialogService, ErrorService } from 'projects/fast-code-core/src/public_api';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-emailtemplate-list',
	templateUrl: './email-template-list.component.html',
	styleUrls: ['./email-template-list.component.scss']
})
export class EmailTemplateListComponent extends BaseListComponent<IEmailTemplate> implements OnInit {

	title: string = "Email Templates";
	entityName: string = 'Email';
	columns: IListColumn[] = [
		{
			column: 'templatename',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.TEMPLATE-NAME'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'subject',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.SUBJECT'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'category',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.CATEGORY'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		/*{
			column: 'cc',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.CC'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'contenthtml',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.CONTENT-HTML'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'contentjson',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.CONTENT-JSON'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},
		{
			column: 'creationtime',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.CREATION-TIME'),
			sort: false,
			filter: false,
			type: listColumnType.Date
		},
		{
			column: 'creatoruserid',
			label: this.translate.instant('EMAIL-EDITOR.EMAIL-TEMPLATE.FIELDS.CREATOR-USER-ID'),
			sort: true,
			filter: true,
			type: listColumnType.String
		},*/

		{
			column: 'actions',
			label: this.translate.instant('EMAIL-GENERAL.ACTIONS.ACTIONS'),
			sort: false,
			filter: false,
			type: listColumnType.String
		}
	];

	selectedColumns = this.columns;
	displayedColumns: string[] = this.columns.map((obj) => { return obj.column });

	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public emailService: EmailTemplateService,
		public globalPermissionService: GlobalPermissionService,
		public errorService: ErrorService,
		private translate: TranslateService
	) {
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, emailService, errorService)
		//this.globalPermissionService = localGlobalPermissionService;
	}

	ngOnInit() {
		this.setAssociation();
		this.primaryKeys = ["id"];
		super.ngOnInit();
	}
	initializePageInfo() {
		this.hasMoreRecords = true;
		this.pageSize = 10;
		this.lastProcessedOffset = -1;
		this.currentPage = 0;
	}
	setAssociation() {
		this.associations = [
		];
	}

	addNew() {
		this.router.navigate(['./emailtemplate'], { relativeTo: this.route.parent });
		return;
	}

}
