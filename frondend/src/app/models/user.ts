export enum Role {
    USER = 'USER',
    CHEF = 'CHEF',
    ADMIN = 'ADMIN',
}

export interface User {
    id: number;
    firstname: string;
    lastname: string;
    username?: string;
    email: string;
    role: Role;
}
