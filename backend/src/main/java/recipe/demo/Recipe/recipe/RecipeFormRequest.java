package recipe.demo.Recipe.recipe;

import java.util.List;

public class RecipeFormRequest {
    private Recipe recipe;
    private List<QuantityIngredient> quantityIngredients;


    public RecipeFormRequest() {
    }

    public RecipeFormRequest(List<QuantityIngredient> quantityIngredients, Recipe recipe) {
        this.recipe = recipe;
        this.quantityIngredients = quantityIngredients;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<QuantityIngredient> getQuantityIngredients() {
        return quantityIngredients;
    }

    public void setQuantityIngredient(List<QuantityIngredient> quantityIngredients) {
        this.quantityIngredients = quantityIngredients;
    }
}
