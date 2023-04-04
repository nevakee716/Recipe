package recipe.demo.Recipe.recipe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import recipe.demo.Recipe.security.auth.AuthenticationService;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.User;
import recipe.demo.Recipe.security.user.UserRepository;
import recipe.demo.Recipe.security.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class RecipeConfiguration {


    @Bean
    CommandLineRunner commandLineRunner(AuthenticationService authService, UserRepository userRepository, RecipeRepository recipeRepository, IngredientRepository ingredientRepository, KeywordRepository keywordRepository) {

        return args -> {
            Boolean initUser= true;
            Boolean initKeywords= true;
            Boolean initIngredients = true;
            Boolean initRecipe = false;





            List<Keyword> keywordsList = new ArrayList<>();
            if(initKeywords) {
                // Check or Create Default Keyword

                List.of("Plat en sauce", "Chaud", "Viande", "Lactose", "Froid", "Grillade").forEach(keywordName -> {
                    List<Keyword> r = keywordRepository.findByName(keywordName);
                    if (r.size() > 0) keywordsList.add(r.get(0));
                    else {
                        Keyword newKeyword = new Keyword();
                        newKeyword.setName(keywordName);
                        keywordRepository.save(newKeyword);
                        keywordsList.add(newKeyword);
                    }
                });
            }

            List<Ingredient> ingredientsList = new ArrayList<>();
            if(initIngredients) {
                // Check or Create Default Ingredients

                List.of("Boeuf", "Navet", "Carotte", "Crème", "Veau").forEach(ing -> {
                    List<Ingredient> r = ingredientRepository.findByName(ing);
                    if (r.size() > 0) ingredientsList.add(r.get(0));
                    else {
                        Ingredient newIngredient = new Ingredient(ing);
                        ingredientRepository.save(newIngredient);
                        ingredientsList.add(newIngredient);
                    }
                });
            }

            if(initUser) {
                User adminUser;
                UserService userService = new UserService(userRepository);
                // Create Admin
                Optional<User> ru = userRepository.findByEmail("admin");

                if (ru.isEmpty()) {
                    adminUser = new User(1, "admin", "admin", "admin", "admin", Role.ADMIN, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                    authService.register(adminUser);
                } else {
                    adminUser = ru.get();
                }

                if(initKeywords && initIngredients && initRecipe) {
                    List<Recipe> r = new ArrayList<>();

                    // Create Recipe 1
                    r = recipeRepository.findByName("Blanquette de veau");
                    if (r.size() == 0) {
                        Recipe blanquette = new Recipe();
                        blanquette.setName("Blanquette de veau");
                        blanquette.setDescription("La blanquette, ou blanquette de veau ou blanquette de veau à l\'ancienne, est une recette de cuisine traditionnelle de cuisine française, à base de viande de veau cuite dans un bouillon avec carotte, poireau, oignon et bouquet garni, liée en sauce blanche à la crème et au beurre et aux champignons de Paris.");
                        blanquette.addIngredient(ingredientsList.get(4), "700 g");
                        blanquette.addIngredient(ingredientsList.get(3), "50 cl");
                        blanquette.addIngredient(ingredientsList.get(2), "500 g");
                        blanquette.setKeywordList(List.of(keywordsList.get(0), keywordsList.get(1), keywordsList.get(2), keywordsList.get(3)));
                        blanquette.setCreator(adminUser);
                        recipeRepository.save(blanquette);
                    }

                    // Create Recipe 1
                    r = recipeRepository.findByName("Pot au feu");
                    if (r.isEmpty()) {
                        Recipe pot = new Recipe();
                        pot.setName("Pot au feu");
                        pot.setDescription("Le pot-au-feu est une recette de cuisine traditionnelle emblématique historique de la cuisine française, et du repas gastronomique des Français, à base de viande de bœuf cuisant longuement à feu très doux dans un bouillon de légumes.");
                        pot.addIngredient(ingredientsList.get(0), "1kg");
                        pot.addIngredient(ingredientsList.get(1), "250g");
                        pot.addIngredient(ingredientsList.get(2), "500g");
                        pot.setKeywordList(List.of(keywordsList.get(0), keywordsList.get(1), keywordsList.get(2)));
                        pot.setCreator(adminUser);
                        recipeRepository.save(pot);
                    }
                }
            }
        };
    }
}
