package recipe.demo.Recipe.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;
    private final KeywordRepository keywordRepository;
    private final IngredientRepository ingredientRepository;
    @Autowired
    public RecipeService(KeywordRepository keywordRepository,RecipeRepository recipeRepository,CommentRepository commentRepository,IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
        this.ingredientRepository = ingredientRepository;
        this.keywordRepository = keywordRepository;
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }
    public List<Recipe> getRecipe(Long recipeId) {
        return recipeRepository.findAllById(List.of(recipeId));
    }

    public List<Keyword> getKeywords() {
        return keywordRepository.findAll();
    }


    public Recipe createRecipe(Recipe recipe, User creator) {
        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipe.getName());
        newRecipe.setDescription(recipe.getDescription());
        newRecipe.setInstructions(recipe.getInstructions());
        newRecipe.setCreator(creator);
        processKeywords(newRecipe, recipe);
        processIngredient(newRecipe, recipe);
        recipeRepository.save(newRecipe);
        return newRecipe;
    }
    public Recipe updateRecipe(Long recipeId, Recipe recipe, User editor) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalStateException("Recipe not found"));
        if(!editor.getId().equals(existingRecipe.getCreator().getId()) && editor.getRole() == Role.CHEF) {
            throw new IllegalStateException("Unauthorized to edit this recipe");
        } else {
            existingRecipe.setName(recipe.getName());
            existingRecipe.setDescription(recipe.getDescription());
            existingRecipe.setInstructions(recipe.getInstructions());
            processKeywords(existingRecipe, recipe);
            existingRecipe.emptyIngredients();
            processIngredient(existingRecipe, recipe);
            recipeRepository.save(existingRecipe);
            return existingRecipe;
        }
    }
    private void processKeywords(Recipe existingRecipe,Recipe recipe) {
        List<Keyword> keywordList = new ArrayList<>();
        if(recipe.getKeywordList() != null) {
            recipe.getKeywordList().forEach(keyword -> {
                // new ingredient
                if (keyword.getId() == 0) {
                    Keyword newKeyword = new Keyword();
                    newKeyword.setName(keyword.getName());
                    keywordRepository.save(newKeyword);
                    keywordList.add(newKeyword);
                } else {
                    Long id = keyword.getId();
                    Keyword existingKeyword = keywordRepository.getReferenceById(id);
                    keywordList.add(existingKeyword);
                }
            });
        }
        existingRecipe.setKeywordList(keywordList);
    }
    private void processIngredient(Recipe existingRecipe,Recipe recipe) {
        if(recipe.getRecipesIngredients() != null) {
            recipe.getRecipesIngredients().forEach(quantityIngredient -> {
                // new ingredient
                if (quantityIngredient.getIngredient().getId() == 0) {
                    Ingredient newIngredient = new Ingredient(quantityIngredient.getIngredient().getName());
                    ingredientRepository.save(newIngredient);
                    existingRecipe.addIngredient(newIngredient, quantityIngredient.getQuantity());
                } else {
                    Long id = quantityIngredient.getIngredient().getId();
                    Ingredient existingIngredient = ingredientRepository.findAllById(List.of(id)).get(0); // for some reason getId didn't work
                    existingRecipe.addIngredient(existingIngredient, quantityIngredient.getQuantity());
                }
            });
        }
    }
    public void deleteRecipe(Long recipeId, User user) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalStateException("Recipe not found"));
        if(!user.getId().equals(existingRecipe.getCreator().getId()) && user.getRole() == Role.CHEF) {
            throw new IllegalStateException("Unauthorized to edit this recipe");
        }
        recipeRepository.deleteById(recipeId);
    }

    public void deleteComment(Long commentId, User user, Recipe recipe) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("Comment not found"));
        if(!user.getId().equals(existingComment.getCreator().getId()) && user.getRole() != Role.ADMIN) {
            throw new IllegalStateException("Unauthorized to edit this recipe");
        }
        Recipe existingRecipe = recipeRepository.findById(recipe.getId())
                .orElseThrow(() -> new IllegalStateException("Comment not found"));
        existingRecipe.removeComment(commentId);
        recipeRepository.save(existingRecipe);
        commentRepository.deleteById(commentId);
    }

    public Comment addCommentToRecipe(Long recipeId, Comment comment) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalStateException("Recipe not found"));
        comment.setCreationDate(new Date());
        comment = commentRepository.save(comment);
        existingRecipe.getCommentList().add(comment);
        recipeRepository.save(existingRecipe);
        return comment;
    }
}
