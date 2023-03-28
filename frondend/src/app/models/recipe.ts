import { QuantityIngredient } from './quantity-ingredient';
import { Comment } from './comment';

export interface Recipe {
    id: number;
    name: string;
    description: string;
    imageUrl: string;
    instructions: string;
    ingredientsQuantity: QuantityIngredient[];
    comments: Comment[];
}
