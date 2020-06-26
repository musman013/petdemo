import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Globals } from '../../../globals';
import { MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from "@angular/router";

@Component({
  selector: 'app-comment-new',
  templateUrl: './comment-new.component.html',
  styleUrls: ['./comment-new.component.scss']
})
export class CommentNewComponent implements OnInit {

  commentForm: FormGroup;
  loading = false;
  submitted = false;

  isMediumDeviceOrLess: boolean;

  constructor(private formBuilder: FormBuilder, private router: Router,
    private global: Globals,
    public dialogRef: MatDialogRef<CommentNewComponent>
  ) { }

  ngOnInit() {
    this.global.isMediumDeviceOrLess$.subscribe(value => {
      this.isMediumDeviceOrLess = value;
    });

    this.commentForm = this.formBuilder.group({
      comment: ['', [Validators.required,Validators.maxLength(4000)]]
    });

    console.log(this.commentForm.get('comment'))
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }

  onSubmit(){
    this.dialogRef.close(this.commentForm.value['comment']);
  }

}
