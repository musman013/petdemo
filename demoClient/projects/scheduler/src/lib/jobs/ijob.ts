import { ExecutionHistory } from '../execution-history/executionHistory';
import { Trigger } from '../triggers/trigger';

export interface IJob {
  id?: number;
  jobName?: string;
  jobGroup?: string;
  jobDescription?: string;
  jobClass?: string;
  jobStatus?: string;
  isDurable?: boolean;
  lastExecutionTime?: Date,
  nextExecutionTime?: Date,
  jobMapData?: Map<string, string>;
  triggers?: Array<Trigger>;
  executionHistory?: Array<ExecutionHistory>;

}

export class Job implements IJob {
  constructor(public id?: number, public jobName?: string, public jobGroup?: string,
    public jobDescription?: string, public jobClass?: string, public isDurable?: boolean,
    public lastExecutionTime?: Date, public nextExecutionTime?: Date, public jobMapData?: Map<string,string>,
    public triggers?: Array<Trigger>, public executionHistory?: Array<ExecutionHistory>) { }
}