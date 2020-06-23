import {Injectable, Inject} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';

import {IEmailTemplate} from './iemail-template';
import {IP_CONFIG} from '../tokens';

// import {GenericApiService, ILibraryRootConfg, ISearchField, ServiceUtils} from 'projects/fast-code-core/src/public_api';
import {GenericApiService, ILibraryRootConfg} from 'projects/fast-code-core/src/public_api';
//removed due to circular dependency
@Injectable({
  providedIn: 'root'
})
export class EmailFileService extends GenericApiService<IEmailTemplate> {
  urlPath;

  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG)  config: ILibraryRootConfg) {
    super(httpclient, {apiUrl: config.apiPath}, 'email');
    this.urlPath = config.apiPath;

  }

  //removed these method to base so that other component can also use this
  // createFileMetadata(fileMetadata) {
  //    return this.httpclient.post<any>(this.urlPath + '/files', fileMetadata);

  // }

  // uploadFile(id, file: File) {
  //   if (file && file.name) {
  //     const fileData = new FormData();
  //     fileData.append('file', file);
  //     this.httpclient.put<any>(this.urlPath + '/files/' + id, fileData);
  //   }
  // }

  createFileMetadata(fileMetadata) {
    return this.httpclient.post<any>(this.apiUrl + '/files', fileMetadata);
  }

  uploadFile(id, file: File) {
    if (file && file.name) {
      const fileData = new FormData();
      fileData.append('file', file);
      return this.httpclient.put<any>(this.apiUrl + '/files/' + id, fileData);
    }
  }


  uploadBlock(id, block) {
    if (block && block.file && block.file.name) {
      const fileData = new FormData();
      fileData.append('file', block.file);
      this.httpclient.put(this.urlPath + '/files/' + id, fileData)
        .subscribe(res => {
          if (block.src) {
            block.src = id;
          }
        });
    }
  }


}
