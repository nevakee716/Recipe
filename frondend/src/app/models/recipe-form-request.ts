import { Recipe } from './recipe';
import { QuantityIngredient } from './quantity-ingredient';


;
export interface RecipeFormRequest {
    recipe: Recipe;
    quantityIngredients: QuantityIngredient[];
}
