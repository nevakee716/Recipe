package recipe.demo.Recipe.recipe;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.User;
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

    @GetMapping(value = "/keywords")
    public ResponseEntity<?> getKeywords(){
        return ResponseEntity.ok(recipeService.getKeywords());
    }


    @GetMapping(value = "/ingredients")
    public ResponseEntity<?> getIngredients(){
        return ResponseEntity.ok(ingredientService.getIngredients());
    }
    @PostMapping("/{recipeId}/comment")
    public ResponseEntity<?> addCommentToRecipe(@PathVariable Long recipeId, @RequestBody Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        comment.setCreator(user);
        return ResponseEntity.ok(recipeService.addCommentToRecipe(recipeId, comment));
    }
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @RequestBody Recipe recipe ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        recipeService.deleteComment(commentId,user,recipe);
        return ResponseEntity.ok(user.toDTO());
    }



    // only chef or admin can add recipe
    @PostMapping("/create")
    public  ResponseEntity<?> createRecipe(@RequestBody Recipe recipe) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.CHEF) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Chef or Admin");
        }
        Recipe newRecipe = recipeService.createRecipe(recipe,user);
        return ResponseEntity.ok(newRecipe.getId());
    }




    @PutMapping("/{recipeId}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long recipeId, @RequestBody Recipe recipe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.CHEF) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Chef or Admin");
        }
        recipeService.updateRecipe(recipeId,recipe,user);
        return ResponseEntity.ok(recipeId);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long recipeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.CHEF) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Chef or Admin");
        }
        recipeService.deleteRecipe(recipeId,user);
        return ResponseEntity.ok(user.toDTO());
    }


}
