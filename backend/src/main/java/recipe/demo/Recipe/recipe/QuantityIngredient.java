package recipe.demo.Recipe.recipe;

public class QuantityIngredient {
    private Ingredient ingredient;
    private String quantity;
    public QuantityIngredient() {
    }

    public QuantityIngredient(Ingredient ingredient, String quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

