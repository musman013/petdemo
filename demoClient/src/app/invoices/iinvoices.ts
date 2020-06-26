export interface IInvoices {  
	id: number;
	amount: number;
	status: string;
	visitsDescriptiveField?: string;
	visitId: number;
	ownersDescriptiveField?: string;
	ownerId: number;
	vetDescriptiveField?: string;
	vetId: number;
}
