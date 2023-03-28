package recipe.demo.Recipe.recipe;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table
public class Ingredient {

    @Id
    @SequenceGenerator(
            name = "ingredient_sequence",
            sequenceName = "ingredient_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ingredient_sequence"
    )
    private Long id;
    private String name;

    @OneToMany(mappedBy = "ingredient",  orphanRemoval = true)
    private List<RecipeIngredient> recipesIngredients;

    public Ingredient() {
        this.name = "";
        this.recipesIngredients = new ArrayList<RecipeIngredient>();
    }
    public Ingredient(String name) {
        this.name = name;
        this.recipesIngredients = new ArrayList<RecipeIngredient>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeIngredient> recipesIngredientsList() {
        return this.recipesIngredients;
    }


    public HashMap<String,String> getRecipesIngredients() {
        HashMap<String,String> r = new HashMap<>();
//        this.recipesIngredients.forEach(recipesIngredient -> {
//            r.put( recipesIngredient.getRecipe().getName(),recipesIngredient.getQuantity());
//        });
        return r;
    }



    public void setRecipesIngredients(List<RecipeIngredient> recipesIngredients) {
        this.recipesIngredients = recipesIngredients;
    }
}
