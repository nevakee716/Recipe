package recipe.demo.Recipe.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;

    private final IngredientRepository ingredientRepository;
    @Autowired
    public RecipeService(RecipeRepository recipeRepository,CommentRepository commentRepository,IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }
    public List<Recipe> getRecipe(Long recipeId) {
        return recipeRepository.findAllById(List.of(recipeId));
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long recipeId, Recipe recipe,List<QuantityIngredient> quantityIngredients) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalStateException("Recipe not found"));
        existingRecipe.setName(recipe.getName());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setInstructions(recipe.getInstructions());
        existingRecipe.setKeywordList(recipe.getKeywordList());
        existingRecipe.emptyIngredients();

        if(quantityIngredients != null) {
            quantityIngredients.forEach(quantityIngredient -> {
                // new ingredient
                if (quantityIngredient.getIngredient().getId() == 0) {
                    Ingredient newIngredient = new Ingredient(quantityIngredient.getIngredient().getName());
                    ingredientRepository.save(newIngredient);
                    existingRecipe.addIngredient(newIngredient, quantityIngredient.getQuantity());
                } else {
                    Long id = quantityIngredient.getIngredient().getId();
                    Ingredient existingIngredient = ingredientRepository.getReferenceById(id);
                    existingRecipe.addIngredient(existingIngredient, quantityIngredient.getQuantity());
                }
            });
        }

        return recipeRepository.save(existingRecipe);
    }

    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public Comment addCommentToRecipe(Long recipeId, Comment comment) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalStateException("Recipe not found"));
        commentRepository.save(comment);
        recipe.getCommentList().add(comment);
        recipeRepository.save(recipe);
        return comment;
    }
}
