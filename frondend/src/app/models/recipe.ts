import { QuantityIngredient } from './quantity-ingredient';
import { Comment } from './comment';
import { User } from './user';
import { Keyword } from './keyword';
export interface Recipe {
    id: number;
    name: string;
    description: string;
    imageUrl: string;
    instructions: string;
    ingredientsQuantity?: QuantityIngredient[];
    commentList: Comment[];
    keywordList: Keyword[];
    creator: User;
}
