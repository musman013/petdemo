import { ExecutionHistory } from '../execution-history/executionHistory';

export interface ITrigger {
  id?: number;
  jobName?: string;
  jobGroup?: string;
  triggerName?: string;
  triggerGroup?: string;
  triggerType?: string;
  triggerState?: string;
  description?: string;
  lastExecutionTime?: Date;
  nextExecutionTime?: Date;
  startTime?: Date;
  endTime?: Date;
  cronExpression?: string;
  repeatInterval?: string;
  repeatIndefinitely?: boolean;
  repeatCount?: number;
  jobMapData?: Map<string, string>;
  executionHistory?: Array<ExecutionHistory>;

}

export class Trigger implements ITrigger {
  constructor(
    public id?: number,
    public jobName?: string,
    public jobGroup?: string,
    public triggerName?: string,
    public triggerGroup?: string,
    public triggerType?: string,
    public triggerState?: string,
    public description?: string,
    public lastExecutionTime?: Date,
    public nextExecutionTime?: Date,
    public startTime?: Date,
    public endTime?: Date,
    public cronExpression?: string,
    public repeatInterval?: string,
    public repeatIndefinitely?: boolean,
    public repeatCount?: number,
    public jobMapData?: Map<string, string>,
    public executionHistory?: Array<ExecutionHistory>,
  ) { }
}