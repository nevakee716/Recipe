package recipe.demo.Recipe.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
@RestController
@RequestMapping(path = "api/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;
    @Autowired
    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }
    @GetMapping
    public List<Recipe> getRecipes(){
        return recipeService.getRecipes();
    }

    @PostMapping("/create")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    @GetMapping(value = "/{recipeId}")
    public List<Recipe> getRecipes(@PathVariable Long recipeId){
        return recipeService.getRecipe(recipeId);
    }


    @PutMapping("/{recipeId}")
    public Recipe updateRecipe(@PathVariable Long recipeId, @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(recipeId, recipe);
    }

    @DeleteMapping("/{recipeId}")
    public void deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
    }

    @PostMapping("/{recipeId}/comment")
    public Comment addCommentToRecipe(@PathVariable Long recipeId, @RequestBody Comment comment) {
        return recipeService.addCommentToRecipe(recipeId, comment);
    }

}
