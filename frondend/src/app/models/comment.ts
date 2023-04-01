import { User } from './user';

export interface Comment {
    id: number;
    author: string;
    title: string;
    content: string;
    date: Date;
    creator: User;
}
