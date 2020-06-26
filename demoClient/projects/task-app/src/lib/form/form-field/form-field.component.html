<div [formGroup]="parentForm" *ngIf="field != null && field != undefined" [ngSwitch]="field.type">

  <div *ngSwitchCase="'text'" class="form-group">
    <mat-form-field>
      <input formControlName="{{field.id}}" matInput placeholder="{{field.placeholder}}">
    </mat-form-field>
  </div>

  <div *ngSwitchCase="'password'" class="form-group">
    <mat-form-field>
      <input type="password" formControlName="{{field.id}}" matInput placeholder="{{field.placeholder}}">
    </mat-form-field>
  </div>

  <div *ngSwitchCase="'multi-line-text'" class="form-group">
    <mat-form-field>
      <textarea formControlName="{{field.id}}" matInput placeholder="{{field.name}}"></textarea>
    </mat-form-field>
  </div>

  <div *ngSwitchCase="'integer'" class="form-group">
    <mat-form-field>
      <input type="number" formControlName="{{field.id}}" matInput placeholder="{{field.name}}">
    </mat-form-field>
  </div>

  <div *ngSwitchCase="'decimal'" class="form-group">
    <mat-form-field>
      <input type="number" formControlName="{{field.id}}" matInput placeholder="{{field.name}}">
    </mat-form-field>
  </div>

  <div *ngSwitchCase="'radio-buttons'" class="form-group">
    <div class="button-row">
      <label>{{field.name}}</label>
      <mat-radio-group formControlName="{{field.id}}">
        <div *ngFor="let option of field.options">
          <mat-radio-button [value]="option.id ? option.id : option.name">{{option.name}}</mat-radio-button>
        </div>
      </mat-radio-group>
    </div>
  </div>

  <div *ngSwitchCase="'boolean'" class="form-group">
    <span>
      <mat-checkbox formControlName="{{field.id}}">{{field.name}}</mat-checkbox>
    </span>
  </div>

  <div *ngSwitchCase="'dropdown'" class="form-group">
    <mat-form-field class="full-width">
      <mat-label>{{field.name}}</mat-label>
      <mat-select formControlName="{{field.id}}">
        <mat-option *ngFor="let option of field.options" [value]="option">
          {{option.name}}
        </mat-option>
      </mat-select>
    </mat-form-field>
  </div>

  <div *ngSwitchCase="'date'" class="form-group">
    <mat-form-field class="full-width">
      <input matInput [matDatepicker]="datePicker" formControlName="{{field.id}}" placeholder="{{field.placeholder}}">
      <mat-datepicker-toggle matSuffix [for]="datePicker"></mat-datepicker-toggle>
      <mat-datepicker #datePicker></mat-datepicker>
    </mat-form-field>
  </div>

  <div *ngSwitchCase="'hyperlink'" class="form-group">
    <label class="control-label">{{field.name}}</label>
    <div>
      <a [href]="field.value" target="_blank">{{field.value}}</a>
    </div>
  </div>

  <div *ngSwitchCase="'people'" class="form-group">
    <label class="control-label">{{field.name}}</label>
    <ul class="generic-list">
      <li class="action">
        <div (click)="selectPeople()">
          <div *ngIf="field.value && field.value.id">
            <app-user-picture [userId]="field.value.id"></app-user-picture>
            <app-user-name [user]="field.value"></app-user-name>
          </div>
          <span *ngIf="!field.value && !field.placeholder" translate="FORM.MESSAGE.SELECT-PERSON"></span>
          <span *ngIf="!field.value && field.placeholder" translate="{{field.placeholder}}"></span>
          <div *ngIf="field.value && field.value.id" user-link="field.value"></div>
          <span *ngIf="field.value && !field.value.id">{{field.value}}</span>
        </div>
        <div class="actions" *ngIf="field.value">
          <a><i class="icon icon-remove" (click)="fieldPersonRemoved()"></i></a>
        </div>
        <input matInput formControlName="{{field.id}}" [hidden]="true" />
      </li>
    </ul>
  </div>

  <div *ngSwitchCase="'functional-group'" class="form-group">
    <label class="control-label">{{field.name}}</label>
    <ul class="generic-list">
      <li class="action">
        <div (click)="selectGroup()">
          <span *ngIf="!field.value && !field.placeholder" translate="FORM.MESSAGE.SELECT-GROUP"></span>
          <span *ngIf="!field.value && field.placeholder" translate="{{field.placeholder}}"></span>
          <div *ngIf="field.value && field.value.id && field.value.name">{{field.value.name}}</div>
        </div>
        <div class="actions" *ngIf="field.value && field.value.id && field.value.name">
          <a (click)="fieldGroupRemoved()"><i class="icon icon-remove"></i></a>
        </div>
        <input matInput formControlName="{{field.id}}" [hidden]="true" />
      </li>
    </ul>
  </div>

  <div *ngSwitchCase="'upload'" class="form-group">
    <label class="control-label">{{field.name}}</label>
    <input *ngIf="!uploads || uploads.length == 0" type="file" (change)="fileChange($event)" #fileInput />

    <ul class="generic-list">
      <li *ngFor="let content of uploads">
        <i class="icon icon-{{content.simpleType}}"></i>
        {{content.name}}
        <div class="actions">
          <a (click)="removeContent(content)"><i class="icon icon-remove"></i></a>
        </div>
      </li>
    </ul>

  </div>

  <div *ngSwitchCase="'spacer'" class="form-group">
    <br />
  </div>

  <div *ngSwitchCase="'horizontal-line'" class="form-group">
    <hr>
  </div>

  <div *ngSwitchCase="'headline'" class="form-group">
    <h4><strong>{{field.name}}</strong></h4>
  </div>

  <div *ngSwitchCase="'headline-with-line'" class="form-group">
    <h4><strong>{{field.name}}</strong></h4>
    <hr>
  </div>

  <div *ngSwitchCase="'expression'" class="form-group">
    <div class="well well-sm" ng-style="{'font-size': field.params.size + 'em'}">
      <div *ngIf="!field.value && !field.expression">
        {{'FORM.MESSAGE.EMPTY' | translate}}
      </div>
      <div *ngIf="!field.value">
        {{field.expression}}
      </div>
      <div *ngIf="field.value">
        {{field.value}}
      </div>
    </div>
  </div>
</div>