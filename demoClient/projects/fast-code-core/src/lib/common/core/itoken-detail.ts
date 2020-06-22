export interface ITokenDetail {
    sub: string;
    exp?: string;
    scopes?: string[];
    role?: ITokenRole;
}

export enum ITokenRole {
    admin = "admin",
    owner = "owner",
    vet = "vet",
}