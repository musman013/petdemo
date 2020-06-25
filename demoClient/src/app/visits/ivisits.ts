export interface IVisits {
	visitNotes: string;
	description?: string;
	id: number;
	visitDate?: Date;
	status: VisitStatus;
	petsDescriptiveField?: string;
	petId: number;
	vetsDescriptiveField?: string;
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
