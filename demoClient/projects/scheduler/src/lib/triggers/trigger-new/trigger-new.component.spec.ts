import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { By } from "@angular/platform-browser";
import { TestingModule, EntryComponents } from '../../../testing/utils';
import { TriggerNewComponent, TriggerService, SelectJobComponent, ITrigger } from '../index';
import { JobService } from '../../jobs/index';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { IP_CONFIG } from '../../tokens';
import { IForRootConf } from '../../IForRootConf';
import { of, throwError } from 'rxjs';
import { HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../environment';
describe('TriggerNewComponent', () => {
  let component: TriggerNewComponent;
  let fixture: ComponentFixture<TriggerNewComponent>;
  let el: HTMLElement;

  let httpTestingController: HttpTestingController;
  let url:string = environment.apiUrl + '/triggers';

  let data: ITrigger = {
    id: 1,
    triggerName: "trigger1",
    triggerGroup: "group1",
    jobName: "job1",
    jobGroup: "group1",
    triggerType: "Simple",
    repeatInterval: "1",
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TriggerNewComponent, SelectJobComponent].concat(EntryComponents),
      imports: [
        TestingModule
      ],
      providers: [
        TriggerService,
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: { close: (dialogResult: any) => { console.log("dialogResult") } } },
        {
          provide: IP_CONFIG,
          useValue: { apiPath: "127.0.0.1:5555" }
        },
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TriggerNewComponent);
    httpTestingController = TestBed.get(HttpTestingController);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  * Unit test cases for isolated testing
  */

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call the onsubmit', async () => {

    component.triggerForm.patchValue(data);
    fixture.detectChanges();
    spyOn(component, "combineDateAndTime").and.returnValue(new Date());
    spyOn(component, "onSubmit").and.callThrough();
    el = fixture.debugElement.query(By.css('button[name=save]')).nativeElement;
    el.click();
    expect(component.onSubmit).toHaveBeenCalledTimes(1);

  });

  it('should not call the onsubmit', async () => {

    fixture.detectChanges();
    el = fixture.debugElement.query(By.css('button[name=save]')).nativeElement;
    el.click();
    spyOn(component, "combineDateAndTime").and.returnValue(new Date());
    spyOn(component, "onSubmit").and.callThrough();
    expect(component.onSubmit).toHaveBeenCalledTimes(0);

  });

  it('should call the create method of service with success', async () => {

    component.triggerForm.patchValue(data);
    fixture.detectChanges();
    const triggerService = fixture.debugElement.injector.get(TriggerService);
    spyOn(component, "getJobMapData").and.returnValue({sampleKey: "sampleValue"});
    spyOn(component, "combineDateAndTime").and.returnValue(new Date());
    spyOn(component.dialogRef, "close").and.callThrough();
    spyOn(triggerService, "create").and.returnValue(of(data));
    component.onSubmit();
    expect(component.dialogRef.close).toHaveBeenCalled();

  });

  it('should call the create method of service with error', async () => {

    component.triggerForm.patchValue(data);
    fixture.detectChanges();
    const triggerService = fixture.debugElement.injector.get(TriggerService);
    spyOn(component, "getJobMapData").and.returnValue({sampleKey: "sampleValue"});
    spyOn(component, "combineDateAndTime").and.returnValue(new Date());
    spyOn(triggerService, "create").and.returnValue(throwError({status: 404}));
    component.onSubmit();
    expect(component.loading).toEqual(false);

  });

  it('should parse the jobMapData', async () => {

    component.ELEMENT_DATA = [{
      "dataKey": "key1",
      "dataValue": "val1"
    }];
    fixture.detectChanges();
    let jobMapData = component.getJobMapData();
    component.onSubmit();
    expect(jobMapData).toEqual({key1: "val1"});

  });

  it('should call selectJob function', async () => {

    spyOn(component, 'selectJob').and.callFake(()=>{});
    const openDialogButton = fixture.debugElement.query(By.css('button[title=select-job]')).nativeElement;
    openDialogButton.click();
    expect(component.selectJob).toHaveBeenCalled();

  });

  it('should open select job dialog', async () => {

    spyOn(component.dialog, 'open').and.callThrough();
    component.selectJob();
    expect(component.dialog.open).toHaveBeenCalled();

  });

  it('should remove job data', async () => {

    component.ELEMENT_DATA = [{
      "dataKey": "key1",
      "dataValue": "val1"
    }];
    fixture.detectChanges();
    component.removeJobData(0);
    expect(component.ELEMENT_DATA.length).toEqual(0);

  });

  it('should add job data', async () => {

    component.ELEMENT_DATA = [];
    fixture.detectChanges();
    component.addJobData();
    expect(component.ELEMENT_DATA.length).toEqual(1);

  });

  it('should call the onCancel', async () => {

    spyOn(component, "onCancel").and.callThrough();
    el = fixture.debugElement.query(By.css('button[name=cancel]')).nativeElement;
    el.click();
    expect(component.onCancel).toHaveBeenCalledTimes(1);

  });

  it('should get the list of groups', async () => {

    const triggerService = fixture.debugElement.injector.get(TriggerService);
    spyOn(triggerService, "getTriggerGroups").and.returnValue(of(['g1', 'g2']));
    spyOn(component, "getTriggerGroups").and.callThrough();
    component.getTriggerGroups();
    expect(component.options.length).toEqual(2);

  });

  it('should create the form', async () => {

    component.createForm();
    expect(component.triggerForm).toBeTruthy();

  });

  it('should call combineDateAndTime method with am date', async () => {

    const amDate = component.combineDateAndTime("10/17/2019", "09:23 AM");
    expect(amDate).toEqual(new Date("2019-10-17T09:23:00"));

  });

  it('should call combineDateAndTime method with pm date', async () => {

    const pmDate = component.combineDateAndTime("10/17/2019", "09:23 PM");
    expect(pmDate).toEqual(new Date("2019-10-17T21:23:00"));

  });

  it('should call combineDateAndTime method with 12 am date', async () => {
    
    const amDate = component.combineDateAndTime("10/17/2019", "12:00 AM");
    expect(amDate).toEqual(new Date("2019-10-17T00:00:00"));

  });

  it('should set field validations for trigger type cron ', async () => {

    component.triggerForm.get('triggerType').setValue("Cron");
    fixture.detectChanges();
    // reading controls are not supported, so checking that if these controls are invalid and valid respectedly
    expect(component.triggerForm.get('cronExpression').valid).toBe(false);
    expect(component.triggerForm.get('repeatInterval').valid).toBe(true);

  });

  it('should set field validations for trigger type simple ', async () => {

    component.triggerForm.get('triggerType').setValue("Simple");
    fixture.detectChanges();
    expect(component.triggerForm.get('cronExpression').valid).toBe(true);
    expect(component.triggerForm.get('repeatInterval').valid).toBe(false);

  });

  it('should set required validation for control repeatCount if trigger type is simple and repeatIndefinite is not set', async () => {

    component.triggerForm.get('triggerType').setValue("Simple");
    component.triggerForm.get('repeatIndefinite').setValue(false);
    fixture.detectChanges();
    expect(component.triggerForm.get('repeatCount').valid).toBe(false);

  });

  it('should remove required validation from control repeatCount if trigger type is cron', async () => {

    component.triggerForm.get('triggerType').setValue("Cron");
    fixture.detectChanges();
    expect(component.triggerForm.get('repeatCount').valid).toBe(true);

  });

  it('should remove required validation from control repeatCount if repeatIndefinite is checked ', async () => {

    component.triggerForm.get('repeatIndefinite').setValue(true);
    fixture.detectChanges();
    expect(component.triggerForm.get('repeatCount').valid).toBe(true);

  });

  /** end of unit tests */

  /**
   * integrtaion test cases
   */

  it('should call the onsubmit int', async () => {

    component.triggerForm.patchValue(data); 
    fixture.detectChanges();
    el = fixture.debugElement.query(By.css('button[name=save]')).nativeElement;
    spyOn(component, "combineDateAndTime").and.returnValue(new Date());
    spyOn(component.dialogRef, "close").and.callThrough();
    el.click();
    const req = httpTestingController.expectOne(req => req.method === 'POST' && req.url === url).flush(data); 
    expect(component.dialogRef.close).toHaveBeenCalledWith(data);
  });

});
