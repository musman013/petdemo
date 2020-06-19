import { Component, OnInit } from '@angular/core';
import { EmailTemplateService } from './email-template.service';
import { MailTemplateService } from './mail-template.service';
import {EmailVariableService} from './email-variable/email-variable.service';

import {
    IPDefaultEmail,  Structure,  TextBlock,  ImageBlock, IpBlock, IBlocks,DividerBlock
} from  '../classes';
import {  IpEmailBuilderService} from  '../ip-email-builder.service';
//'ip-email-builder';
import { BehaviorSubject } from 'rxjs';
import { ActivatedRoute,Router} from "@angular/router";
import { IEmailTemplate } from './iemail-template';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { GlobalPermissionService } from 'src/app/core/global-permission.service';
import { TranslateService } from '@ngx-translate/core';
//import { DividerBlock, IBlockState } from 'ip-email-builder/public_api';
@Component({
  selector: 'email-template-editor',
  templateUrl: './template-editor.component.html',
  styleUrls: ['./template-editor.component.scss']
})
export class TemplateEditorComponent implements OnInit {
  title = "Create Email Template";
  isLoading: BehaviorSubject<boolean>;
  emailTemplate?:IEmailTemplate;
  errorMessage = '';
  formGroup: FormGroup;
  editorView:boolean=false;

  entityName:string =  'Email';
  IsReadPermission:Boolean=false;
  IsCreatePermission:Boolean=false;
  IsUpdatePermission:Boolean=false;
  IsDeletePermission:Boolean=false;
  constructor(
    private _ngb: IpEmailBuilderService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private emailtemplateService:EmailTemplateService,
    private mailtemplateService:MailTemplateService,
    private emailVariableService: EmailVariableService,
    private formBuilder: FormBuilder,
    public globalPermissionService: GlobalPermissionService,
    private translate: TranslateService
  ) {
    this.isLoading = _ngb.isLoading;
    _ngb.MergeTags = new Set(["tag22"]) ; //new Set(['{{firstName}}', '{{lastName}}']);
  }
  ngOnInit() {
    const param = this.route.snapshot.paramMap.get('id');
    this.setPermissions();
    this.emailVariableService.getAll(null,0,20).subscribe(
      items => {
        let tags = items.map(item=> item.propertyName);
        this._ngb.MergeTags=new Set(tags);
      },
      error => this.errorMessage = <any>error
    );
    this.formGroup = this.formBuilder.group({
      id:[''],
      templateName: ['', Validators.required],
      category: ['',Validators.required],
      subject: ['', Validators.required],
      to: ['', Validators.required],
      attachmentpath:[''],
      active:[true, Validators.required]    
        
     });
      if(param) {
        const id = +param;
        this.getEmailTemplate(id);
        this.editorView = true;
      }
    else {
    // const id = +param;
    //this.getEmailTemplate(id);
    this.emailTemplate= {} as IEmailTemplate;
   //<p class="ql-align-center">
        this._ngb.Email = new IPDefaultEmail({
          structures: [
            new Structure('cols_1', 
            [
             
              [
                new ImageBlock(
                  'https://secureservercdn.net/198.71.190.232/rjq.996.myftpupload.com/wp-content/uploads/2018/07/Nav-Logo.png',
                  {
                    width: { value:200, unit:'px'},
                    height: { value:50, unit:'px'},
                   
                  }
                ),
             
               new DividerBlock( null,{
                disabled: false,
                message: ''
              }),
                new TextBlock(
                  `${this.translate.instant('EMAIL-EDITOR.MESSAGES.SAMPLE-TEMPLATE')}`,
                  // `<p>Dear Email Editor,</p><p >  With this email template editor, you can edit your text, and add buttons,images and dividers.
                  //  The above logo image and dividers are for demo purpose, you can remove or replace them.
                  //  You can view the list of supported elements by clicking the Drag & Drop Content section of the page</p>
                  // <p>Thanks,</p>
                  // <p>John</p>`,
                  {
                    lineHeight: {
                      value: 22,
                      unit: 'px'
                    },
                    padding:{ bottom:10,left:25, right:25, top:10}
                  }
                ),
             /*   new TextBlock(
                  `<h2 class="ql-align-center">${this.translate.instant('EMAIL-EDITOR.MESSAGES.SAMPLE-TEMPLATE2')}</h2>`
                )*/
               
               
                
              ]
            ])
          ]
        });
      }
 
  }
  setPermissions= ()=> {
    // this.globalService.getUserPermissions().subscribe(permissions=> { 
    //   let perms = permissions;
    
      if(this.globalPermissionService) {
            let entityName = this.entityName.startsWith("I") ? this.entityName.substr(1) : this.entityName;
            this.IsCreatePermission = this.globalPermissionService.hasPermissionOnEntity(entityName, "CREATE");
            if(this.IsCreatePermission) {
              this.IsReadPermission = true;
              this.IsDeletePermission = true;
              this.IsUpdatePermission = true;
            } else {
              this.IsDeletePermission = this.globalPermissionService.hasPermissionOnEntity(entityName, "DELETE");
              this.IsUpdatePermission = this.globalPermissionService.hasPermissionOnEntity(entityName, "UPDATE");
              this.IsReadPermission = (this.IsDeletePermission || this.IsUpdatePermission)? true: this.globalPermissionService.hasPermissionOnEntity(entityName, "READ");
            }
        }
      //});
  }
  sendTestMail() {
    const to = prompt(this.translate.instant('EMAIL-EDITOR.MESSAGES.SELECT-RECEIVER-PROMPT'));
    if (!to) {
      return;
    }
   // this._ngb.sendTestEmail({ to });

    this.loadEmailTemplate();
    this.emailTemplate.to = to;
    //this.emailTemplate.contentJson = template;
    this.mailtemplateService.create(this.emailTemplate)
    .subscribe(
        data => {                  
        //  x = data;
        //   y = JSON.parse(x.contentJson);
           //this.emailTemplate = {...this.emailTemplate,...data};
           var snackBarRef = this.snackBar.open(this.translate.instant('EMAIL-EDITOR.MESSAGES.EMAIL-SENT-SUCCESS'), this.translate.instant('EMAIL-GENERAL.ACTIONS.OK') , { duration:1000, panelClass:['snackbar-background'] });
         //  snackBarRef.dismiss();
           // this._ngb.sendRequest();
            //this.onBack();
        },
        error => {
           
           
        });
  }
 looadEmail (structures:any){
  this._ngb.Email = new IPDefaultEmail(structures);
 }

  getEmailTemplate(id: number) {
    this.emailtemplateService.getById(id).subscribe(
      templ => {
        this.emailTemplate = templ;
        this.formGroup.patchValue({
          id:templ.id,
          templateName:templ.templateName,
          subject:templ.subject,
          to:templ.to,
          contentJson:templ.contentJson,
          contentHtml:templ.contentHtml,
          category: templ.category,
          attachmentpath: templ.attachmentpath,
          active: templ.active
        
        });
        this.looadEmail(JSON.parse(templ.contentJson));
       
      },
      error => this.errorMessage = <any>error);
  }
  
  saveEmail = ()=>{
    if (!this._ngb.IsChanged ){
      this._ngb.notify(this.translate.instant('EMAIL-EDITOR.MESSAGES.NO-CHANGES'));
     // return Promise.reject(`There's no changes to be saved`);
    } else {
      // this.generatingTemplate = true;
      //await this._ngb.sendRequest();
      if (this.formGroup.invalid) {
        return;
    }
      if(!this.editorView)
      {
        this.editorView=true;
      }
      else
       this.onEmailTemplateSave(JSON.stringify(this._ngb.Email));
      // this.generatingTemplate = false;
    }
  }
  loadEmailTemplate = ()=>{
    this.emailTemplate.templateName = this.formGroup.value.templateName;
    this.emailTemplate.category = this.formGroup.value.category;
    this.emailTemplate.to = this.formGroup.value.to;
    this.emailTemplate.subject = this.formGroup.value.subject;
    this.emailTemplate.contentHtml="";
    this.emailTemplate.attachmentpath=  this.formGroup.value.attachmentpath;
    this.emailTemplate.active = this.formGroup.value.active;
    this.emailTemplate.contentJson = JSON.stringify(this._ngb.Email);
  }
  onEmailTemplateSave = (template) => {
    let x = template;
    let y = JSON.parse(x);
    this.emailTemplate.templateName = this.formGroup.value.templateName;
    this.emailTemplate.category = this.formGroup.value.category;
    this.emailTemplate.to = this.formGroup.value.to;
    this.emailTemplate.subject = this.formGroup.value.subject;
    this.emailTemplate.contentHtml="";
    this.emailTemplate.attachmentpath=  this.formGroup.value.attachmentpath;
    this.emailTemplate.active = this.formGroup.value.active;
    this.emailTemplate.contentJson = template;
   
    if(!this.emailTemplate.id){
      /*this.emailTemplate.subject="subject";
      this.emailTemplate.category="category";
      this.emailTemplate.contentHtml="";
      this.emailTemplate.templateName="usertemplate";
      this.emailTemplate.to="gzadik@yahoo.com";
      this.emailTemplate.contentJson = template;*/
      this.emailtemplateService.create(this.emailTemplate)
      .subscribe(
          data => {                  
            x = data;
             y = JSON.parse(x.contentJson);
             this.emailTemplate = {...this.emailTemplate,...data};
           /*   this._ngb.sendRequest().then(data=> {
                 var x = data;
              });*/
              this.onBack();
          },
          error => {
             
             
          });
    }
    else {
   
    this.emailtemplateService.update(this.emailTemplate, this.emailTemplate.id)
    .subscribe(
        data => {                  
          x = data;
           y = JSON.parse(x.contentJson);
        /*   this._ngb.sendRequest().then(data=> {
            var x = data;
          });*/
           this.onBack();
        },
        error => {
           
           
        });
      }
}
onBack(): void {
  this.router.navigate(['./emailtemplates'],{ relativeTo: this.route.parent });
}
}
