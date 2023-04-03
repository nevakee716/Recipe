import { Recipe } from './recipe';
import { QuantityIngredient } from './quantity-ingredient';
import { Keyword } from './keyword';

export interface RecipeFormRequest {
    recipe: Recipe;
    quantityIngredients: QuantityIngredient[];
    keywords: Keyword[];
}
