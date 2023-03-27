import { IngredientQuantity } from './ingredientquantity';
import { Comment } from './comment';

export interface Recipe {
    id: number;
    name: string;
    description: string;
    imageUrl: string;
    instructions: string;
    ingredients: any;
    comments: Comment[];
}
