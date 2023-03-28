package recipe.demo.Recipe.recipe;

import java.util.List;

public class RecipeFormRequest {
    private Recipe recipe;
    private List<QuantityIngredient> quantityIngredient;


    public RecipeFormRequest() {
    }

    public RecipeFormRequest(List<QuantityIngredient> quantityIngredient, Recipe recipe) {
        this.recipe = recipe;
        this.quantityIngredient = quantityIngredient;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<QuantityIngredient> getQuantityIngredient() {
        return quantityIngredient;
    }

    public void setQuantityIngredient(List<QuantityIngredient> quantityIngredient) {
        this.quantityIngredient = quantityIngredient;
    }
}
