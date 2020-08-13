import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SchemaData, SchemaFiles } from '../models/schema.model';
import { MetaData } from '../models/reports.model';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class MainService {
  
  constructor(private http: HttpClient, private router: Router) {}

  getMetaData(){
    return this.http.get<MetaData>('http://localhost:4200/cubejs-api/v1/meta');
  }

  getDbTablesList(){
    return this.http.get<SchemaData>('http://localhost:4200/playground/db-schema');
  }

  getSchemaFilesList(){
    return this.http.get<SchemaFiles>('http://localhost:4200/playground/files');
  }

  generateSchema(tablesList){
    return this.http.post<SchemaFiles>('http://localhost:4200/playground/generate-schema', tablesList);
  }

  updateSchemaFile(updatedContent){
    return this.http.post('http://localhost:4200/saveschema', updatedContent);
  }

  generateAggregatedMeasures(){
    return this.http.get('http://localhost:4200/generateAggregatedMeasures');
  }

}