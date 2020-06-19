# IP Email Template Builder

An **Angular 6** [Email Template Builder](https://ngb.wlocalhost.org?utm_source=npm) with [MJML](https://mjml.io) integration.

## Attention

In order to use this module, you need a valid **API Key**, get one for free from [here](https://ionprodan.typeform.com/to/AuNvPa).

## Installation

```bash
npm install ip-email-builder
```

And inject `IpEmailBuilderModule` in your root module:

```js
import { IpEmailBuilderModule } from 'ip-email-builder';

@NgModule({
  declarations: [AppComponent],
  imports: [
    IpEmailBuilderModule.forRoot({ apiKey: '...' }),
  ],
  bootstrap: [AppComponent]
})
```

## Example

See it in action on our [demo website](https://ngb.wlocalhost.org?utm_source=npm).

Let's say that this is `app.component.ts`:

```js
import { Component, OnInit } from '@angular/core';
import {
  IpEmailBuilderService,
  IPDefaultEmail,
  Structure,
  TextBlock,
  ImageBlock
} from 'ip-email-builder';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  isLoading: BehaviorSubject<boolean>;

  constructor(private _ngb: IpEmailBuilderService) {
    this.isLoading = _ngb.isLoading;
  }

  sendTestMail() {
    const to = prompt('Where to send?');
    if (!to) {
      return;
    }
    this._ngb.sendTestEmail({ to });
  }

  ngOnInit() {
    this._ngb.Email = new IPDefaultEmail({
      structures: [
        new Structure('cols_1', [
          [
            new TextBlock(
              `<p class="ql-align-center">This text and Image are injected from <strong>Root Module</strong>.
              It's an example of dynamic Email, which allow you to create many awesome things, like Newsletter!</p>`,
              {
                lineHeight: {
                  value: 22,
                  unit: 'px'
                }
              }
            ),
            new TextBlock(
              `<h2 class="ql-align-center">It looks like this!</h2>`
            ),
            new ImageBlock(
              'https://image.ibb.co/iXV3S9/Screen_Shot_2018_09_14_at_17_15_38.png'
            )
          ]
        ])
      ]
    });
  }
}
```

and this is `app.component.html`

```html
<ip-email-builder style="...">
  <!-- Include custom html near top buttons -->
  <div class="top-actions">
    <button mat-button (click)="sendTestMail()" [disabled]="isLoading | async" color="primary">Send a test email</button>
  </div>
  <!-- Include custom html after content blocks -->
  <div class="after-content-blocks"></div>
  <!-- Include custom html after structure blocks -->
  <div class="after-structure-blocks"></div>
</ip-email-builder>
```

## Docs

Get more info from online [documentation](https://docs.wlocalhost.org/angular-6-email-builder/).

## History

First release
