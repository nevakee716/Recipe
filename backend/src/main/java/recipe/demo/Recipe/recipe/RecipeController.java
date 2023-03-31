package recipe.demo.Recipe.recipe;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.User;
import recipe.demo.Recipe.security.user.UserDTO;
import recipe.demo.Recipe.security.user.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "api/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final UserService userService;
    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    @Autowired
    public RecipeController(UserService userService, RecipeService recipeService,IngredientService ingredientService){
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.userService =  userService;
    }


    // all user authenticated can read recipe and ingredient
    @GetMapping
    public ResponseEntity<?> getRecipes(){
        return ResponseEntity.ok(recipeService.getRecipes());
    }
    @GetMapping(value = "/{recipeId}")
    public ResponseEntity<?>  getRecipes(@PathVariable Long recipeId){
        return ResponseEntity.ok(recipeService.getRecipe(recipeId));
    }
    @GetMapping(value = "/ingredients")
    public ResponseEntity<?> getIngredients(){
        return ResponseEntity.ok(ingredientService.getIngredients());
    }
    @PostMapping("/{recipeId}/comment")
    public ResponseEntity<?> addCommentToRecipe(Principal principal, @PathVariable Long recipeId, @RequestBody Comment comment) {
        return ResponseEntity.ok(recipeService.addCommentToRecipe(recipeId, comment));
    }


    // only chef or admin can add recipe
    @PostMapping("/create")
    public  ResponseEntity<?>  createRecipe(Principal principal,@RequestBody RecipeFormRequest recipeFormRequest) {
        User user = userService.getUserFromPrincipal(principal);
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.CHEF) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Chef or Admin");
        }
        return ResponseEntity.ok(recipeService.createRecipe(recipeFormRequest.getRecipe(),recipeFormRequest.getQuantityIngredients()));
    }

    @DeleteMapping("/{recipeId}")
    public void deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
    }

    @PutMapping("/{recipeId}")
    public Recipe updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeFormRequest recipeFormRequest) {
        return recipeService.updateRecipe(recipeId, recipeFormRequest.getRecipe(),recipeFormRequest.getQuantityIngredients());
    }




}
