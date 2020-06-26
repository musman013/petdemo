import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { RelatedContentService } from '../../../common/services/related-content.service';

@Component({
  selector: 'app-document-preview',
  templateUrl: './document-preview.component.html',
  styleUrls: ['./document-preview.component.scss']
})
export class DocumentPreviewComponent implements OnInit {
  @Input() content: any;
  @Input() task: any;
  @Input() readOnly: any;
  @Output() onContentDeleted: EventEmitter<any> = new EventEmitter();
  constructor( private relatedContentService: RelatedContentService) { }

  ngOnInit() {
    console.log(this.content)
    this.relatedContentService.addUrlToContent(this.content);
  }

  deleteContent() {

    this.relatedContentService.deleteContent(this.content.id).subscribe((response)=> {
      // this.deleted({ content: this.content });
      console.log(response);
      this.onContentDeleted.emit(this.content);
      this.content = null;
    });
  };
}
