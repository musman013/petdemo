export interface IReport {
    id?: number,
    userId?: number,
    title: string;
    description?: string;
    reportType: string;
    ctype: string;
    query: any;
    reportWidth?: string;
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
  }