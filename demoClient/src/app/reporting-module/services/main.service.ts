import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SchemaData, SchemaFiles } from '../models/schema.model';
import { MetaData } from '../models/reports.model';
import { Report, Dashboard } from '../models/dashboard.model';
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

  login(details){
    this.http.post('/api/login',details).subscribe(res => {
      localStorage.setItem('user',JSON.stringify({userId:1,...res}))
      this.router.navigate(['/']);
    });
  }

  getToken(){
    return JSON.parse(localStorage.getItem('user'));
  }

  logout(){
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  // report calls

  addReport(details){
    return this.http.post<Report>('/api/report',details);
  }
  updateReport(details, id: number){
    return this.http.put<Report>('/api/report/'+id,{details});
  }
  getAllReports(){
    return this.http.get<Array<Report>>('/api/report');
  }
  getReport(id: number){
    return this.http.get<Report>('/api/report/'+id);
  }
  deleteReport(id: number){
    return this.http.delete('/api/report/'+id);
  }
  refreshReport(report_id){
    return this.http.put<Report>(`/api/report/${report_id}/refresh`,{});
  }
  resethReport(report_id){
    return this.http.put<Report>(`/api/report/${report_id}/reset`,{});
  }
  publishReport(report_id){
    return this.http.put<Report>(`/api/report/${report_id}/publish`,{});
  }

  // report dashboard calls

  addNewReporttoNewDashboard(data: Dashboard){
    return this.http.post<Dashboard>('/api/dashboard/addNewReportToNewDashboard',data)
  }
  addNewReporttoExistingDashboard(data: Dashboard){
    return this.http.put<Dashboard>('/api/dashboard/addNewReportToExistingDashboard',data)
  }
  addExistingReportToNewDashboard(data: any){
    return this.http.post<Dashboard>('/api/dashboard/addExistingReportToNewDashboard',data);
  }
  addExistingReportToExistingDashboard(data: any){
    return this.http.put<Dashboard>('/api/dashboard/addExistingReportToExistingDashboard',data);
  }

  // dashboard calls

  addDashboard(data){
    this.http.post('/dashboard',data);
  }
  getAllDashboards(){
    return this.http.get<Array<Dashboard>>('/api/dashboard');
  }
  getDashboard(id: any){
    return this.http.get<Dashboard>('/api/dashboard/'+id);
  }
  updateDashboard(id: any, data){
    return this.http.put('/api/dashboard/'+id,data);
  }
  deleteDashboard(id:number){
    return this.http.delete('/api/dashboard/'+id);
  }

}