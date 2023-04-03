import { User } from './user';

export interface Comment {
    id?: number;
    title: string;
    content: string;
    creationDate?: Date;
    creator?: User;
}
