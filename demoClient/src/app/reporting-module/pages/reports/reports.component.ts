import { Component, OnInit, OnDestroy, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { QueryParam, Measures, Dimensions, MetaContent } from '../../models/reports.model';
import * as _ from 'lodash';
import { Dashboard } from '../../models/dashboard.model';
import { Router, ActivatedRoute } from '@angular/router';
import { MatDialog, MatSnackBar } from '@angular/material';
import { AddReportsToDashboardComponent } from '../../modalDialogs/addReportsToDashboard/addReportsToDashboard.component';
import { AddExReportsToDashboardComponent } from "../../modalDialogs/addExReportsToDashboard/addExReportsToDashboard.component";
import { MainService } from '../../services/main.service';
import { SaveReportsComponent } from '../../modalDialogs/saveReports/saveReports.component';
import { ReportService } from './report.service';
import { DashboardService } from '../dashboard/dashboard.service';
import { IReport } from './ireport';
import { IDashboard } from '../dashboard/Idashboard';
import { CubejsClient } from '@cubejs-client/ngx';
import * as CodeMirror from 'codemirror';
import { WindowRef } from './WindowRef';
import sqlFormatter from "sql-formatter";
import { Globals } from 'projects/fast-code-core/src/public_api';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit, OnDestroy {
  dashboard: Dashboard = {
    title: '',
    description: '',
    reportDetails: [{
      title: '',
      reportType: '',
      ctype: '',
      query: {},
      reportWidth: ''
    }]
  };
  allDashboardsList = [];
  allDashboardsData: IDashboard[] = [];

  title = 'Untitle chart';
  metadata: Array<MetaContent>;
  reOrderMeta = {};
  measures: Array<Measures> = [];
  dimensions: Array<Dimensions> = [];
  segments: Array<any>;
  tableColumns = [];
  measuresChipArray = [];
  selectedTableColumn: any;
  order: any;
  dataTime: Array<Dimensions> = [];
  filters = [];
  selectedFilters = [];
  chartType = 'line';
  query: QueryParam;
  ctype = 'line';
  timeFilter = '';
  timeFilterFor = '';
  timeFilterBy = '';
  filItemVal = '';
  selected = '';
  selectedChart = 'Select chart';
  addToDashStatus = false;
  report_id = -1;
  report: IReport;
  jsonQuery = "";
  sql = "";
  devEnvironment = true;
  aggregations = ['count','countDistinct','countDistinctApprox','sum','avg','min','max'];
  timeFilterForList = ['All time', 'Today', 'Yesterday', 'This week', 'This month', 'This quarter', 'This year', 'Last 7 days',
    'Last 30 days', 'Last week', 'Last month', 'Last quarter', 'Last year'];
  timeFilterByList = ['w/o grouping', 'Hour', 'Day', 'Week', 'Month', 'Year'];
  filtersNonStrings = {
    equals: 'equals', notEquals: 'does not equal', set: 'is set',
    notSet: 'is not set', gt: '>', gte: '>=', lt: '<', lte: '<='
  };
  filtersStrings = {
    contains: 'contains', notContains: 'does not contain', equals: 'equals',
    notEquals: 'does not equal', set: 'is set', notSet: 'is not set'
  };

  queryParam = {
    measures: [],
    dimensions: [],
    timeDimensions: [],
    filters: [],
    order:{}
  };
  generalAggregations = ['count','countDistinct','countDistinctApprox','sum','avg','min','max'];
  timeAggregations = ['count','countDistinct','countDistinctApprox','min','max'];
  stringAggregations = ['count','countDistinct','countDistinctApprox'];
  sqlDoc;
  sqlViewer;
  @ViewChild('sqlViewer', {static: false}) set content(content: ElementRef) {
    if(content) { // initially setter gets called with undefined
      if(!this.sqlViewer){
        this.sqlViewer = content;
        this.setCodeMirror();
      }
    }
 };
  constructor(
    private service: MainService,
    private reportService: ReportService,
    private dashboardService: DashboardService,
    public dialog: MatDialog,
    private _snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private cubejs: CubejsClient,
    private winRef: WindowRef,
    private global: Globals
    ) { }

  ngOnInit() {
    this.manageScreenResizing();
    this.report_id = +this.route.snapshot.paramMap.get('id');
    this.route.params.subscribe(params => {
      this.report_id = params['id'];
      console.log(`subscribed param : ${this.report_id}`);
    })
    this.getMetaData();
    this.dashboardService.getAll([], 0, 1000).subscribe(res => {
      this.allDashboardsData = res;
    });
  }

  measureCriteriaFieldWidth = 30;
  criteriaFieldWidth = 20;
  aggregationFieldWidth = 10;
  timeValueFieldWidth = 15;
  filterFieldWidth = 33;
  filterRowWidth = 60;
  manageScreenResizing() {
    this.global.isSmallDevice$.subscribe(value => {
      if(value){
        this.filterFieldWidth = 100;
        this.aggregationFieldWidth = 100;
        this.timeValueFieldWidth = 100;
        this.measureCriteriaFieldWidth = 100;
        this.criteriaFieldWidth = 100;
        this.filterRowWidth = 100;
      }
    });
    this.global.isMediumDevice$.subscribe(value => {
      if(value){
        this.filterFieldWidth = 33;
        this.aggregationFieldWidth = 50;
        this.timeValueFieldWidth = 50;
        this.measureCriteriaFieldWidth = 50;
        this.criteriaFieldWidth = 50;
        this.filterRowWidth = 100;
      }
    });
    this.global.isLargeDevice$.subscribe(value => {
      if(value){
        this.filterFieldWidth = 33;
        this.aggregationFieldWidth = 10;
        this.timeValueFieldWidth = 15;
        this.measureCriteriaFieldWidth = 30;
        this.criteriaFieldWidth = 20;
        this.filterRowWidth = 60;
      }
    });
  }

  getMetaData(){
    this.service.getMetaData().subscribe(
      (res) => {
        this.metadata = res.cubes;
        for (const m of res.cubes) {
          this.reOrderMeta[m.name] = m;
          if (m.hasOwnProperty('measures')) {
            this.measures.push(...JSON.parse(JSON.stringify(m.measures)));
          }
          if (m.hasOwnProperty('dimensions')) {
            this.dimensions.push(...JSON.parse(JSON.stringify(m.dimensions)));
          }
          // if(m.hasOwnProperty('segments')) {
          //   this.segments.push(...JSON.parse(JSON.stringify(m.segments)));
          // }
        }
        for(var d of this.dimensions){
          this.tableColumns.push({name: d.title,type: d.type});
        }
        console.log(this.dimensions);
        console.log(this.measures);
        console.log(this.tableColumns);
        this.dataTime = this.dimensions.filter(x => x.type === 'time');
        console.log(this.dataTime)
        this.filters.push(...JSON.parse(JSON.stringify(this.measures)));
        this.filters.push(...JSON.parse(JSON.stringify(this.dimensions)));
        if (this.report_id >= 0) {
          this.getReport();
        }
        else {
          this.initializeReport();
        }
      },
      (err) => {
        console.error('Observer got an error: ' + err);
      }
    );
  }

  getReport() {
    console.log(this.report_id);
    this.reportService.getById(this.report_id).subscribe(report => {
      this.report = report;
      this.updateReportInfo();
    });
  }

  updateReportInfo() {
    this.title = this.report.title;
    this.query = this.report.query;
    this.chartType = this.report.reportType;
    this.selectedChart = this.report.reportType.charAt(0).toUpperCase() + this.report.reportType.slice(1);
    this.queryParam.measures = this.report.query.measures;
    this.queryParam.dimensions = this.report.query.dimensions;
    this.queryParam.timeDimensions = this.report.query.timeDimensions;
    this.dashboard.description = this.report.description;
    this.measuresChipArray = [];
    if (this.report.query.timeDimensions.length > 0) {
      this.timeFilter = this.report.query.timeDimensions[0].dimension;
      this.timeFilterFor = "All time";
      this.timeFilterBy = this.timeFilterByList[this.timeFilterByList.findIndex(x => x.toLowerCase() == this.report.query.timeDimensions[0].granularity)];
    }
    for(var i = 0; i < this.queryParam.measures.length; i++){
      var meta = this.queryParam.measures[i].split(".")[0];
      var aggregatedString = this.queryParam.measures[i].substr(this.queryParam.measures[i].indexOf(".")+1);
      var aggregate = aggregatedString.split("_")[0];
      var title = aggregatedString.substr(aggregatedString.indexOf("_")+1);
      title = title.replace(/_/g," ");
      title = title.toLowerCase().split(" ").map((s)=> s.charAt(0).toUpperCase()+s.substr(1)).join(" ");
      console.log(title);
      console.log(aggregate);
      var dimension = this.tableColumns[this.tableColumns.findIndex(s=>s.name === (`${meta} ${title}`))];
      console.log(dimension);
      if(dimension.type == "string"){
        this.aggregations = this.stringAggregations
      }else if(dimension.type == "time"){
        this.aggregations = this.timeAggregations;
      }else{
        this.aggregations = this.generalAggregations;
      }
      var chipTitle = `${meta}.${title.replace(/ /g,"_")}`;
      console.log(chipTitle,aggregate.charAt(0).toUpperCase() + aggregate.slice(1));
      this.measuresChipArray.push({name:chipTitle,aggregation:this.aggregations,selectedAggregation:aggregate});
    }
    console.log(this.measuresChipArray);
  }

  initializeReport() {
    this.dashboard = {
      title: '',
      description: '',
      reportDetails: [{
        title: '',
        reportType: '',
        ctype: '',
        query: {},
        reportWidth: ''
      }]
    };
    this.chartType = 'line';
    this.measuresChipArray = [];
    this.selectedTableColumn = undefined;
    this.query = undefined;
    this.title = 'Untitle chart';
    this.timeFilter = '';
    this.timeFilterFor = '';
    this.timeFilterBy = '';
    this.selectedChart = 'Select chart';
    this.queryParam = {
      measures: [],
      dimensions: [],
      timeDimensions: [],
      filters: [],
      order:{}
    };
  }

  ngOnDestroy() {
  }

  // showChart(chartType) {
  //   if(chartType === 'area'){
  //     this.chartType = 'line';
  //     this.ctype = chartType;
  //   } else{
  //     this.chartType = chartType;
  //     this.ctype = chartType;
  //   }
  //   this.resetDimentions('Line');
  // }

  showLineChart() {
    this.chartType = 'line';
    this.ctype = 'line';
    this.resetDimentions('Line');
  }
  showAreaChart() {
    this.chartType = 'line';
    this.ctype = 'area';
    this.resetDimentions('Area');
  }
  showPieChart() {
    this.chartType = 'pie';
    this.resetDimentions('Pie');
  }
  showDoughnutChart() {
    this.chartType = 'doughnut';
    this.resetDimentions('Doughnut');
  }
  showPolarAreaChart() {
    this.chartType = 'polarArea';
    this.resetDimentions('Polar Area');
  }
  showBarChart() {
    this.chartType = 'bar';
    this.resetDimentions('Bar');
  }
  showStackedBarChart() {
    this.chartType = 'stackedBar';
    this.resetDimentions('Stacked Bar');
  }
  showSingleValue() {
    this.chartType = 'singleValue';
    this.resetDimentions('Number');
  }

  showTable() {
    this.chartType = 'table';
    this.resetDimentions('Table');
  }

  resetDimentions(chart) {
    this.selectedChart = chart;
    if (this.queryParam.timeDimensions.length > 0) {
      if (this.chartType === 'singleValue') {
        this.queryParam.timeDimensions = [{ dimension: this.queryParam.timeDimensions[0].dimension }];
        this.timeFilterBy = 'Week';
      } else {
        let gran = 'week';
        if (this.queryParam.timeDimensions[0].granularity) {
          gran = this.queryParam.timeDimensions[0].granularity;
        }
        this.queryParam.timeDimensions = [{ dimension: this.queryParam.timeDimensions[0].dimension, granularity: gran }];
      }
    }
    this.buildQuery();
  }

  setMeasure(m){
    this.selectedTableColumn = m;
    console.log(this.selectedTableColumn);
    if(this.selectedTableColumn.type == 'string'){
      this.aggregations = this.stringAggregations;
    }else if(this.selectedTableColumn.type == 'time'){
      this.aggregations = this.timeAggregations;
    }else{
      this.aggregations = this.generalAggregations;
    }
    const metakey = this.selectedTableColumn.name.split(" ")[0];
    var chipMeasure = `${metakey}.${this.selectedTableColumn.name.substr(this.selectedTableColumn.name.indexOf(" ")+1).replace(/ /g,"_")}`;
    if(this.measuresChipArray.includes(chipMeasure) === false){
      console.log(chipMeasure);
      this.measuresChipArray.push({name:chipMeasure,aggregation:this.aggregations,selectedAggregation:''});
    }
  }

  getMeasure(m,index) {
    console.log(m,index);
    var measure = ''
    var queryMeasure = '';
    const metakey = this.measuresChipArray[index].name.split(".")[0];
    var column = this.measuresChipArray[index].name.substr(this.measuresChipArray[index].name.indexOf(".")+1).toLowerCase();
    measure = `${m}_${column}`;
    queryMeasure = `${this.measuresChipArray[index].name.split(".")[0]}.${measure}`;
    
    console.log(measure);
    console.log(queryMeasure);
    console.log(metakey);
    if(this.queryParam.measures[index]){
      this.queryParam.measures[index] = queryMeasure;
      this.measuresChipArray[index].selectedAggregation = m
    }
    else if (this.queryParam.measures.includes(this.selectedTableColumn.name) === false) {
      this.queryParam.measures.push(queryMeasure);
      this.measuresChipArray[index].selectedAggregation = m
      const defaultTimeDimension = this.reOrderMeta[metakey].dimensions.filter(x => x.type === 'time').map(y => y.name);
      if (defaultTimeDimension.length > 0) {
        this.queryParam.timeDimensions = [{
          dimension: defaultTimeDimension[0],
          granularity: 'week'
        }];
        this.timeFilter = defaultTimeDimension[0];
        this.timeFilterFor = 'All time';
        this.timeFilterBy = 'Week';
      } else {
        this.showSingleValue();
      }
      this.queryParam.order = {
        
      }
    }
    
    this.buildQuery();
    console.log(this.measuresChipArray);
    console.log(this.queryParam);
  }

  removeMeasure(i) {
    console.log(i);
    this.measuresChipArray.splice(i,1);
    console.log(this.queryParam.measures);
    this.queryParam.measures.splice(i, 1);
    console.log(this.queryParam.measures);
    if (this.queryParam.measures.length > 0) {
      this.buildQuery();
    } else {
      this.queryParam.timeDimensions = [];
      this.timeFilterFor = '';
      this.timeFilterBy = '';
      this.query = {};
    }
  }

  getDimension(d) {
    if (this.queryParam.dimensions.includes(d.name) === false) {
      this.queryParam.dimensions.push(d.name);
      this.buildQuery();
    }
  }
  removeDimension(i) {
    this.queryParam.dimensions.splice(i, 1);
    this.buildQuery();
  }

  getTimeFilter(e) {
    if (!e) {
      this.removeTimeFilter(0);
      return
    }
    this.queryParam.timeDimensions = [{
      dimension: e,
      granularity: 'week'
    }];
    this.timeFilterFor = 'All time';
    this.timeFilterBy = 'Week';
    this.buildQuery();
  }

  removeTimeFilter(i) {
    this.queryParam.timeDimensions.splice(i, 1);
    this.buildQuery();
  }

  getTimeFilterFor(ff) {

    this.timeFilterFor = ff.value;
    if (ff.value !== 'All time') {
      this.queryParam.timeDimensions[0].dateRange = ff.value;
    } else {
      delete this.queryParam.timeDimensions[0].dateRange;
    }
    this.buildQuery();
  }

  getTimeFilterBy(fb) {
    this.timeFilterBy = fb.value;
    if (fb.value !== 'w/o grouping') {
      this.queryParam.timeDimensions[0].granularity = fb.value.toLowerCase();
    } else {
      delete this.queryParam.timeDimensions[0].granularity;
    }
    this.buildQuery();
  }

  selectFilter(f) {
    console.log(f);
    this.selectedFilters.push({
      dimension: f.name,
      type: f.type
    });
    this.queryParam.filters.push({});
  }
  selectOperator(i, e, dim) {
    this.queryParam.filters[i].dimension = dim;
    this.queryParam.filters[i].operator = e.source.value;
    this.queryParam.filters[i].values = [];
    console.log(this.queryParam);
    // this.buildQuery();
  }
  removeFilter(i) {
    this.selectedFilters.splice(i, 1);
    this.queryParam.filters.splice(i, 1);
    this.buildQuery();
  }

  addFilterItem(i, v, type) {
    console.log(v.target.value);
    if (type === 'm') {
      this.queryParam.filters[i].values.push(v.target.value);
      this.filItemVal = null;
    } else {
      this.queryParam.filters[i].values = [v.target.value];
    }
    console.log(this.queryParam);
    this.buildQuery();
  }

  removeFilterVal(index1, index2) {
    this.queryParam.filters[index1].values.splice(index2, 1);
    this.buildQuery();
  }

  buildQuery() {
    this.query = _.clone(this.queryParam);
    console.log(this.query);
  }

  selectedView: string = 'report';
  sqlQuery: string;

  showReport(){
    this.selectedView = 'report';
  }

  showJsonQuery() {
    this.selectedView = 'json';
    this.jsonQuery = JSON.stringify(this.query, undefined, 4).trim();
  }

  showSqlQuery() {
    this.selectedView = 'sql';
    this.cubejs.sql(this.query).subscribe(res => {
      // this.sqlQuery = res.sql();
      this.sqlQuery = sqlFormatter.format(res.sql());
      // this.setCodeMirror();
      // this.sqlDoc.setValue(this.sqlQuery);
    });
  }

  setCodeMirror(){
    const mime = 'text/x-mariadb';
    const currentWindow = this.winRef.nativeWindow;
    // get mime type
    // if (currentWindow.location.href.indexOf('mime=') > -1) {
    //   mime = currentWindow.location.href.substr(currentWindow.location.href.indexOf('mime=') + 5);
    // }
    this.sqlDoc = CodeMirror.fromTextArea(this.sqlViewer.nativeElement, {
      mode: mime,
      indentWithTabs: true,
      smartIndent: true,
      lineNumbers: false,
      // readOnly: true,
      // cursorBlinkRate: -1,
      // matchBrackets: true,
      autofocus: true,
      extraKeys: { 'Ctrl-Space': 'autocomplete' },
    });
  }

  asIsOrder(a, b) {
    return 1;
  }

  // addToDashBoard() {
  //   const graphInfo = {
  //     chartType: this.chartType,
  //     ctype: this.ctype,
  //     query: this.query,
  //     chartWidth: 'smallchart'
  //   };
  //   this.service.saveToDashboard(graphInfo);
  // }

  refreshChart(){
    this.buildQuery();
  }

  saveReportDialog(): void {
    const dialogRef = this.dialog.open(SaveReportsComponent, {
      panelClass: "fc-modal-dialog",
      data: {
        title: this.title,
        description: this.dashboard.description
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (!this.report) {
          this.report = {
            title: result.reportTitle,
            description: result.reportdescription,
            reportType: this.chartType,
            ctype: this.ctype,
            query: this.query
          }
        }
        else {
          this.report.title = result.reportTitle;
          this.report.description = result.reportdescription;
          this.report.reportType = this.chartType;
          this.report.ctype = this.ctype;
          this.report.query = this.query;
          this.report.id = this.report_id;
        }
        if (this.report_id > -1) {
          this.reportService.update(this.report, this.report_id).subscribe(res => {
            this.report = res;
            this.updateReportInfo();
            this.showMessage(`Report updated as ${res.title}`);
          });
        }
        else {
          this.reportService.create(this.report).subscribe(res => {
            if(res){
              this.showMessage('Report saved as ' + res.title);
              this.report = res;
              this.updateReportInfo();
            }
          });
        }
      }
    });
  }

  addReporttoDashboardDialog(): void {
    this.allDashboardsList = this.allDashboardsData.map(v => {
      return {
        id: v.id,
        title: v.title
      };
    });
    if (this.report_id > 0) {
      console.log('report function edit', this.report_id);
      const dialogRef = this.dialog.open(AddExReportsToDashboardComponent, {
        panelClass: "fc-modal-dialog",
        data: this.allDashboardsList
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          if (result.type === "new") {
            const dashboardDetails = {
              userId: this.report.userId,
              title: result.title,
              description: result.description,
              reportDetails: [
                {
                  id: this.report.id,
                  reportWidth: result.chartSize
                }
              ]
            };
            this.dashboardService
              .addExistingReportToNewDashboard(dashboardDetails)
              .subscribe(res => {
                this.showMessage("Added report to " + res.title);
              });
          } else {
            const dashboardDetails = {
              id: result.id,
              userId: this.report.userId,
              reportDetails: [
                {
                  id: this.report.id,
                  reportWidth: result.chartSize
                }
              ]
            };
            this.dashboardService
              .addExistingReportToExistingDashboard(dashboardDetails)
              .subscribe(res => {
                this.showMessage("Added report to " + res.title);
              });
          }
        }
      });
    } else {
      console.log('report function add', this.report_id);
      const dialogRef = this.dialog.open(AddReportsToDashboardComponent, {
        panelClass: "fc-modal-dialog",
        data: this.allDashboardsList
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          if (result.type === 'new') {
            this.dashboard = {
              userId: 1,
              title: result.title,
              description: result.description,
              reportDetails: [{
                title: result.chartTitle,
                description: result.reportdescription,
                reportType: this.chartType,
                ctype: this.ctype,
                query: this.query,
                reportWidth: result.chartSize
              }]
            };
            this.dashboardService.addNewReporttoNewDashboard(this.dashboard).subscribe(res => {
              this.allDashboardsData.push(res);
              this.showMessage('Added report to ' + res.title);
            });
          } else {
            const chartDetails: Dashboard = {
              id: result.title,
              reportDetails: [{
                title: result.chartTitle,
                description: result.reportdescription,
                reportType: this.chartType,
                ctype: this.ctype,
                query: this.query,
                reportWidth: result.chartSize
              }]
            }
            this.dashboardService.addNewReporttoExistingDashboard(chartDetails).subscribe(res => {
              this.showMessage('Added report to ' + res.title);
            });
          }
        }
      });
    }
    // const dialogRef = this.dialog.open(AddReportsToDashboardComponent, {
    //   panelClass: "fc-modal-dialog",
    //   data: this.allDashboardsList
    // });

    // dialogRef.afterClosed().subscribe(result => {
    //   if (result) {
    //     if (result.type === 'new') {
    //       this.dashboard = {
    //         userId: 1,
    //         title: result.title,
    //         description: result.description,
    //         reportDetails: [{
    //           userId: 1,
    //           title: result.chartTitle,
    //           description: result.reportdescription,
    //           reportType: this.chartType,
    //           ctype: this.ctype,
    //           query: this.query,
    //           reportWidth: result.chartSize
    //         }]
    //       };
    //       this.dashboardService.addNewReporttoNewDashboard(this.dashboard).subscribe(res => {
    //         this.allDashboardsData.push(res);
    //         this.showMessage('Added report to ' + res.title);
    //       });
    //     } else {
    //       const chartDetails: Dashboard = {
    //         id: result.title,
    //         userId: 1,
    //         reportDetails: [{
    //           userId: 1,
    //           title: result.chartTitle,
    //           description: result.reportdescription,
    //           reportType: this.chartType,
    //           ctype: this.ctype,
    //           query: this.query,
    //           reportWidth: result.chartSize
    //         }]
    //       }
    //       this.dashboardService.addNewReporttoExistingDashboard(chartDetails).subscribe(res => {
    //         this.showMessage('Added report to ' + res.title);
    //       });
    //     }
    //   }
    // });
  }

  showMessage(msg): void {
    this._snackBar.open(msg, 'close', {
      duration: 2000,
    });
  }

}
