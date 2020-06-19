import { Observable } from "rxjs";

 export interface IGlobalPermissionService {
    
  //  getUserPermissions(): Observable<any> ;
    hasPermissionOnEntity(entity:string, crudType:string):Boolean;
  }