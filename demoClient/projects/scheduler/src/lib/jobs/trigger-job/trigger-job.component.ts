import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { Globals } from '../../globals';
import { MatDialogRef } from '@angular/material/dialog';
import { JobService } from '../job.service';
import { ActivatedRoute, Router } from "@angular/router";
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-trigger-job',
  templateUrl: './trigger-job.component.html',
  styleUrls: ['./trigger-job.component.scss']
})
export class TriggerJobComponent implements OnInit {

  constructor(private formBuilder: FormBuilder, private router: Router,
    private global: Globals, private jobService: JobService,
    public dialogRef: MatDialogRef<TriggerJobComponent>
  ) { }

  ngOnInit() {

  }
}
