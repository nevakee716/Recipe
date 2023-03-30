package recipe.demo.Recipe.recipe;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import recipe.demo.Recipe.security.user.User;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping(path = "api/v1/recipe")
public class RecipeController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    @Autowired
    public RecipeController(RecipeService recipeService,IngredientService ingredientService){
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }


    @GetMapping
    public List<Recipe> getRecipes(){
        return recipeService.getRecipes();
    }

    @PostMapping("/create")
    public Recipe createRecipe(@RequestBody RecipeFormRequest recipeFormRequest) {
        return recipeService.createRecipe(recipeFormRequest.getRecipe(),recipeFormRequest.getQuantityIngredients());
    }

    @GetMapping(value = "/{recipeId}")
    public List<Recipe> getRecipes(@PathVariable Long recipeId){
        return recipeService.getRecipe(recipeId);
    }

    @GetMapping(value = "/ingredients")
    public List<Ingredient> getIngredients(){
        return ingredientService.getIngredients();
    }

    @PutMapping("/{recipeId}")
    public Recipe updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeFormRequest recipeFormRequest) {
        return recipeService.updateRecipe(recipeId, recipeFormRequest.getRecipe(),recipeFormRequest.getQuantityIngredients());
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
