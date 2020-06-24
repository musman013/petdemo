export interface IVisits {  

	description?: string;
	id: number;
	visitDate?: Date;
	petsDescriptiveField?: string;
	petId: number;
	vetsDescriptiveField?: number;
	vetId: number;
}

export interface IChangeStatusObj{
	status: VisitStatus;
	visitNotes?: string;
	invoiceAmount?: Number;
}

export enum VisitStatus{
	Created = "CREATED",
	Confirmed = "CONFIRMED",
	Cancelled = "CANCELLED",
	Completed = "COMPLETED",
}
