import { Component, Input, OnInit } from '@angular/core';
import { TextBlock } from '../../classes/Elements';
import { createPadding, createFont, createLineHeight } from '../../utils';
import { IpEmailBuilderService } from '../../ip-email-builder.service';

@Component({
  selector: 'ip-text-element',
  templateUrl: './text-element.component.html',
  styleUrls: ['./text-element.component.css']
})
export class TextElementComponent implements OnInit {
  @Input()
  block: TextBlock = new TextBlock('Text from inside a component');
  constructor(private ngjs: IpEmailBuilderService) {}

  getTextStyles() {
    const { color, font, lineHeight, padding } = this.block.options;

    return {
      color,
      ...createLineHeight(lineHeight),
      ...createFont(font),
      ...createPadding(padding)
    };
  }

  getQuillConfig() {
    const container: Array<Array<object | string>> = [
      ['bold', 'italic', 'underline', 'strike'],
      [{ header: 1 }, { header: 2 }],
      [{ size: ['small', false, 'large', 'huge'] }, { align: [] }],
      [{ color: [] }, { background: [] }],
      [{ direction: 'rtl' }, 'link']
    ];

    const placeholder = Array.from(this.ngjs.MergeTags);

    if (placeholder.length) {
      container.push([{ placeholder }]);
    }

    return {
      toolbar: {
        container,
        handlers: {
          placeholder(selector: string) {
            //getachew
           var selectorTxt = selector? "{{" + selector + "}}" : selector;
            const range = this.quill.getSelection();
            const format = this.quill.getFormat();
            const text = this.quill.getText(range.index, range.length);
            this.quill.deleteText(range.index, text.length);
            this.quill.insertText(range.index, selectorTxt, format);
            this.quill.setSelection(range.index, selector.length);
          }
        }
      }
    };
  }

  ngOnInit() {}
}
