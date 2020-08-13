import { IReport } from 'src/app/reporting-module/pages/myreports/ireport';
export interface IDashboard {
    id?: number,
    userId?: number,
    title?: string;
    description?: string;
    reportDetails?: Array<IReport>;
    userDescriptiveField?: string;
    editable?: boolean;
    isAssignedByRole?: boolean;
    isPublished?: boolean;
    isRefreshed?: boolean;
    isResetted?: boolean;
    ownerId?: number;
    ownerSharingStatus?: boolean;
    recipientSharingStatus?: boolean;
    sharedWithMe?: boolean;
    sharedWithOthers?: boolean;
    isShareable?: boolean;
  }