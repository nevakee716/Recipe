import { Ingredient } from './ingredient';
import { Recipe } from './recipe';


export interface IngredientQuantity {
  id: number;
  ingredient: Ingredient;
  recipe: Recipe;
  quantity: number;
}
