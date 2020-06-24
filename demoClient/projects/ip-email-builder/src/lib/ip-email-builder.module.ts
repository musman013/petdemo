import { NgModule, ModuleWithProviders } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatIconModule,
  MatFormFieldModule,
  MatInputModule,
  MatTabsModule,
  MatButtonToggleModule,
  MatSelectModule,
  MatListModule,
  MatRadioModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatDividerModule,
  MatTooltipModule,
  MatDialogModule,
  MatSnackBarModule,
  MatProgressBarModule,
  MatExpansionModule,
  MatToolbarModule,
  MatTableModule,
  MatCardModule,
  MatSortModule,
  MatPaginatorModule,
  MatCheckboxModule,
  MatProgressSpinnerModule, MatAutocompleteModule, MatDatepickerModule
} from '@angular/material';

import 'hammerjs';
import 'quill';
import { QuillModule } from 'ngx-quill';


import { IP_CONFIG, GENERAL_OPTIONS } from './tokens';
import { TextElementComponent } from './elements/text-element/text-element.component';
import { StructureComponent } from './components/structure/structure.component';
import { NgxSmoothDnDModule } from 'ngx-smooth-dnd';
import { IpEmailBuilderComponent } from './ip-email-builder.component';
import { DynamicComponentDirective } from './directives/dynamic-component.directive';
import { ImageComponent } from './elements/image/image.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonComponent } from './elements/button/button.component';
import { DividerComponent } from './elements/divider/divider.component';
import { SpacerComponent } from './elements/spacer/spacer.component';
import { SocialComponent } from './elements/social/social.component';
import { BuilderContainerComponent } from './builder-container/builder-container.component';
import { FontStylesComponent } from './groups/font-styles';
import { PaddingComponent } from './groups/padding';
import { LineHeightComponent } from './groups/line-height';
import { WidthHeightComponent } from './groups/width-height';
import { BorderComponent } from './groups/border';
import { ColorComponent } from './groups/color';
import { LinkComponent } from './groups/link';
import { AlignComponent } from './groups/align';
import { DirectionComponent } from './groups/direction';
import { BackRepatComponent } from './groups/back-repeat';
import { ConfirmDialogComponent } from './components/dialog.component';
import { EmptyBlockComponent } from './elements/empty-block.component';
import { HttpClientModule } from '@angular/common/http';
import { PreviewTemplateComponent } from './components/preview.component';
import {Globals} from './globals';
//
//import {EmailListComponent} from './email-editor/email-list.component'
import { EmailTemplateListComponent } from './email-editor/email-template-list.component';
import { TemplateEditorComponent } from './email-editor/template-editor.component';
import {EmailVariableListComponent} from "./email-editor/email-variable/email-variable-list.component";
import {EmailVariableNewComponent} from "./email-editor/email-variable/email-variable-new.component";
import {EmailVariableDetailComponent} from "./email-editor/email-variable/email-variable-detail.component";
import { PickerComponent } from './picker/picker.component';
//import {EmailTemplateListComponent,TemplateEditorComponent, EmailVariableListComponent, EmailVariableNewComponent, EmailVariableDetailComponent} from  './email-editor/index';
import { RouterModule } from '@angular/router';
import { EmailRoutes } from './email-routing.module';
//import { ILibraryRootConfg } from './interfaces';
//import { EmailRoutingModule } from './email-routing.module';
//import {FastCodeCoreModule,EmailRoutes} from 'fastCodeCore';
import { FastCodeCoreModule ,ILibraryRootConfg} from 'projects/fast-code-core/src/public_api';
import { EmailAttachmentComponent } from './email-editor/email-attachment/email-attachment.component';
import { InternationalPhoneNumberModule } from 'ngx-international-phone-number';
//'fastCodeCore';
//import {GenericApiService} from 'fastCodeCore/public_api';
@NgModule({
  imports: [
    // EmailRoutingModule,
    FastCodeCoreModule.forRoot({apiUrl: ''}),
    RouterModule.forChild(EmailRoutes),
    HttpClientModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    NgxSmoothDnDModule,
    QuillModule.forRoot(),
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    MatSliderModule,
    MatSelectModule,
    MatListModule,
    MatRadioModule,
    MatCheckboxModule,
    MatInputModule,
    MatSlideToggleModule,
    MatButtonToggleModule,
    MatDividerModule,
    MatTooltipModule,
    MatDialogModule,
    MatSnackBarModule,
    MatProgressBarModule,
    MatExpansionModule,
    MatToolbarModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    ReactiveFormsModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    InternationalPhoneNumberModule
  ],
  declarations: [
    IpEmailBuilderComponent,
    EmailVariableListComponent,
    EmailVariableNewComponent,
    EmailVariableDetailComponent,
    TextElementComponent,
    StructureComponent,
    DynamicComponentDirective,
    PreviewTemplateComponent,
    ImageComponent,
    ButtonComponent,
    DividerComponent,
    SpacerComponent,
    SocialComponent,
    BuilderContainerComponent,
    FontStylesComponent,
    PaddingComponent,
    LineHeightComponent,
    WidthHeightComponent,
    BorderComponent,
    ColorComponent,
    LinkComponent,
    AlignComponent,
    DirectionComponent,
    BackRepatComponent,
    ConfirmDialogComponent,
    EmptyBlockComponent,

    TemplateEditorComponent,
    EmailTemplateListComponent,TemplateEditorComponent,PickerComponent, EmailAttachmentComponent

  ],
  exports: [IpEmailBuilderComponent,
     EmailTemplateListComponent,TemplateEditorComponent,EmailVariableListComponent],
  entryComponents: [
    TextElementComponent,
    ImageComponent,
    ButtonComponent,
    DividerComponent,
    SpacerComponent,
    SocialComponent,
    ConfirmDialogComponent,
    EmptyBlockComponent,
    PickerComponent
  ]
})
export class IpEmailBuilderModule {
  static forRoot(config: ILibraryRootConfg): ModuleWithProviders {
    return {
      ngModule: IpEmailBuilderModule,
      providers: [
        {
          provide: IP_CONFIG,
          useValue: config
        },
        {
          provide: GENERAL_OPTIONS,
          useValue: { padding: {} }
        },
        Globals
      ]
    };
  }
}
