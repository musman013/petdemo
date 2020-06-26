import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IForRootConf } from '../../IForRootConf';
import { IP_CONFIG } from '../../tokens';

@Injectable({
  providedIn: 'root'
})
export class FunctionalGroupService {

  url = "";
  resp: any

  appDefinitionKey: any;

  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
    this.url = _config.apiPath;
  }

  //Get group info by id
  getGroupInfo(groupId): Observable<any> {
    return this.httpclient.get(this.url + '/app/rest/workflow-groups/' + groupId);
  };

  // get group list
  getFilteredGroups(filterText, groupId, tenantId): Observable<any> {

    var params: any = {};

    if (filterText !== null && filterText !== undefined) {
      params.filter = filterText;
    }

    if (groupId) {
      params.groupId = groupId;
    }

    return this.httpclient.get(this.url + '/app/rest/workflow-groups', { params: params });

  };

}
