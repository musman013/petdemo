// @import '~@angular/material/theming';
@import '~quill/dist/quill.bubble';

//getachew
.email-body {
  min-width: 400px !important;
  max-width: 800px !important;
}

.ql-container {
  font-family: inherit;
  font-size: inherit;
}
.ql-container .ql-editor {
  padding: 0 !important;
  line-height: inherit;
  font-size: inherit;
  overflow: visible;
  // line-height: inherit !important;
}

.ql-placeholder .ql-picker-label::before {
  content: 'Variables';
  min-width: 100px;
}
.ql-placeholder.ql-expanded .ql-picker-item::before {
  content: attr(data-value);
  width: min-content;
}

.ql-tooltip {
  z-index: 10;
  line-height: normal;
}

.ql-toolbar {
  width: 600px;
  width: max-content;
}

$main-padding: 1rem;

.ip {
  &-email-builder {
    height: 100%;

    &-container {
      position: relative;

      mat-progress-bar {
        position: absolute;
        // top: 0;
      }
      .preview {
        padding: $main-padding;
        border-bottom: 1px solid rgba(0, 0, 0, 0.12);
        z-index: 1;
      }
    }
    &-options {
      border-right: 1px solid rgba(0, 0, 0, 0.12);
      // @include mat-elevation(1);
      position: relative;

      .overflow {
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background-color: white;
        opacity: 0.5;
        display: none;
        z-index: 3;
      }

      &.disabled {
        .overflow {
          display: block;
        }
      }

      .mat-tab-label {
        width: calc(100% / 3);
        min-width: auto;
        height: 68px;
        font-size: initial;
        text-transform: uppercase;
      }

      .mat-tab-body-wrapper .mat-tab-body {
        // padding: $main-padding;

        .elements-lists {
          padding: $main-padding;

          .smooth-dnd-container {
            display: grid;
            grid-template: repeat(2, 1fr) / repeat(2, 1fr);
            grid-gap: $main-padding;
            // grid-template-rows: repeat(3, 1fr);
            .drag-element {
              text-align: center;
              border: 1px solid #cccccc;
              border-radius: 3px;
              box-sizing: border-box;
              background: white;
              cursor: move;

              &.disabled {
                // pointer-events: none;
                cursor: not-allowed;
                opacity: 0.6;
              }

              &.dragging {
                transition: transform 0.18s ease;
                transform: rotateZ(5deg);
                z-index: 11;
              }

              &.dropped {
                transition: transform 0.18s ease-in-out;
                transform: rotateZ(0deg);
              }

              .mat-icon {
                height: 100%;
                width: 100%;
                font-size: 40px;
                padding: $main-padding;
              }

              &-title {
                font-size: 0.9rem;
                text-transform: uppercase;
                margin-bottom: $main-padding;
              }

              // TODO: Make each structure image in css!
              &.structure-element {
                height: 107px;
                position: relative;
                padding: $main-padding/2;
                display: grid;
                grid-template: 1fr / 1fr;
                grid-gap: $main-padding/2;
                border-radius: 3px;
                // border: none;
                // @include mat-elevation(1);

                & > div {
                  background: #cccccc;
                  border-radius: 3px;
                }

                &.cols_2 {
                  grid-template-columns: repeat(2, 1fr);
                }

                &.cols_3 {
                  grid-template-columns: repeat(3, 1fr);
                }

                &.cols_4 {
                  grid-template-columns: repeat(4, 1fr);
                }

                &.cols_12 {
                  grid-template-columns: 2fr 1fr;
                }

                &.cols_21 {
                  grid-template-columns: 1fr 2fr;
                }
              }
            }
          }
        }

        .editing-element-header {
          padding-top: $main-padding;
          text-align: center;
          // border-bottom: 1px solid rgba(0, 0, 0, 0.12);
        }

        .editing-element-options {
          padding: $main-padding;

          h3.divider {
            text-align: center;
            display: block;
            overflow: hidden;
            margin: 0;
            margin-bottom: $main-padding;

            span {
              font-weight: 100;
              position: relative;
              display: inline-block;

              &::before,
              &::after {
                content: '';
                position: absolute;
                top: 50%;
                height: 1px;
                background: #cccccc;
                width: 99999px;
              }

              &::before {
                left: 100%;
                margin-left: 10px;
              }
              &::after {
                right: 100%;
                margin-right: 10px;
              }
            }
          }
        }
      }
    }
  }
}
