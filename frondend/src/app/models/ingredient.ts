import { IngredientQuantity } from './ingredientquantity';

export interface Ingredient {
    id: number;
    name: string;
    recipe: IngredientQuantity[];
}
