import { Component, OnInit, OnDestroy } from '@angular/core';
import { DashboardService } from '../dashboard.service';

@Component({
  selector: 'app-manage-dashboards',
  templateUrl: './manage-dashboards.component.html',
  styleUrls: ['./manage-dashboards.component.scss']
})
export class ManageDashboardsComponent implements OnInit, OnDestroy {
  allDashboardsData = [];
  constructor(private dashboardService: DashboardService) { }

  ngOnInit() {
    console.log("in manage dashboard");
    this.dashboardService.getAll([],0,1000).subscribe(res => {
      this.allDashboardsData = res;
    })
  }

  deleteDashboard(id){
    this.dashboardService.delete(id).subscribe(res => {
      this.allDashboardsData = this.allDashboardsData.filter(v => v.id!==id);
    });
  }

  ngOnDestroy(){
  }
}
