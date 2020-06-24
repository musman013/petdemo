
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IDashboard } from './Idashboard';
import { ShareApiService } from '../../services/share-api.service';

@Injectable({
	providedIn: 'root'
})
export class DashboardService extends ShareApiService<IDashboard>{
	constructor(protected http: HttpClient) {
		super(http, "dashboard");
	}

	removeReport(id: number, reportId: number): Observable<any> {
		return this.http.delete(`${this.url}/${id}/report/${reportId}`, {}).pipe(catchError(this.handleError));
	}

	// report dashboard calls

	addNewReporttoNewDashboard(data: any) {
		return this.http.post<any>('/api/dashboard/addNewReportToNewDashboard', data)
	}
	addNewReporttoExistingDashboard(data: any) {
		return this.http.put<any>('/api/dashboard/addNewReportToExistingDashboard', data)
	}
	addExistingReportToNewDashboard(data: any) {
		return this.http.post<any>('/api/dashboard/addExistingReportToNewDashboard', data);
	}
	addExistingReportToExistingDashboard(data: any) {
		return this.http.put<any>('/api/dashboard/addExistingReportToExistingDashboard', data);
	}

}
