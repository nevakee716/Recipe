package recipe.demo.Recipe.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
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

    public Recipe updateRecipe(Long recipeId, Recipe recipe) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalStateException("Recipe not found"));
        existingRecipe.setName(recipe.getName());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setKeywordList(recipe.getKeywordList());
     //   existingRecipe.setIngredientsQuantity(recipe.getIngredientsQuantity());
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
