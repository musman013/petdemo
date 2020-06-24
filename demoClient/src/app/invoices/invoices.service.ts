
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IInvoices } from './iinvoices';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InvoicesService extends GenericApiService<IInvoices> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "invoices");
  }

  payInvoice(id: number): Observable<IInvoices>{
    return this.httpclient.put<IInvoices>(`${this.url}/${id}/pay`, {});
  }
  
  combineDateAndTime(date: string, time: string): Date {
    let tmpDate = new Date(date)
    let hours: number = parseInt(time.substring(0, 2));
    let minutes = parseInt(time.substring(3, 5));
    let ampm = time.substring(6, 8)? time.substring(6, 8): "am";
    if (ampm.toLocaleLowerCase() == "pm") {
      hours = hours + 12;
    } else if (ampm.toLocaleLowerCase() == "am" && hours === 12) {
      hours = 0;
    }
    tmpDate.setHours(hours? hours : 0);
    tmpDate.setMinutes(minutes? minutes : 0);
    return tmpDate;
  }

  formatDateStringToAMPM(d) {
    console.log(d);
    if (d) {
      var date = new Date(d);
      var hours = date.getHours();
      var minutes = date.getMinutes();
      var ampm = hours >= 12 ? 'pm' : 'am';
      hours = hours % 12;
      hours = hours ? hours : 12; // the hour '0' should be '12'
      var minutes_str = minutes < 10 ? '0' + minutes : minutes;
      var strTime = hours + ':' + minutes_str + ' ' + ampm;
      return strTime;
    }
    return null;
  }
  
}
