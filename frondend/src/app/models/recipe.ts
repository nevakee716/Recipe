import { QuantityIngredient } from './quantity-ingredient';
import { Comment } from './comment';
import { User } from './user';

export interface Recipe {
    id: number;
    name: string;
    description: string;
    imageUrl: string;
    instructions: string;
    ingredientsQuantity?: QuantityIngredient[];
    commentList: Comment[];
    creator: User;
}
