package recipe.demo.Recipe.recipe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RecipeConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, KeywordRepository keywordRepository) {
        return args -> {

            // Check or Create Default Ingredients
            List<Ingredient> ingredientsList = new ArrayList<>();
            List.of("Boeuf", "Navet", "Carotte", "Crème", "Veau").forEach(ing -> {
                List<Ingredient> r = ingredientRepository.findByName(ing);
                if (r.size() > 0) ingredientsList.add(r.get(0));
                else {
                    Ingredient newIngredient = new Ingredient(ing);
                    ingredientRepository.save(newIngredient);
                    ingredientsList.add(newIngredient);
                }
            });


            List<Recipe> r = new ArrayList<>();

            // Create Recipe 1
            r = recipeRepository.findByName("Blanquette de veau");
            if (r.size() == 0) {
                Recipe blanquette = new Recipe(
                        "Blanquette de veau",
                        "blahoihaoia djaijdaz ojdaiozjd "
                );
                blanquette.addIngredient(ingredientsList.get(4), "700 g");
                blanquette.addIngredient(ingredientsList.get(3), "50 cl");
                blanquette.addIngredient(ingredientsList.get(2), "500 g");
                recipeRepository.save(blanquette);
            }

            // Create Recipe 1
            r = recipeRepository.findByName("Pot au feu");
            if (r.size() == 0) {
                Recipe pot = new Recipe(
                        "Pot au feu",
                        "djaijdaz dsqd ojdaiozjd "
                );
                pot.addIngredient(ingredientsList.get(0), "1kg");
                pot.addIngredient(ingredientsList.get(1), "250g");
                pot.addIngredient(ingredientsList.get(2), "500g");
                recipeRepository.save(pot);
            }


            Keyword plat = new Keyword("Plat en Sauce");


        };
    }
}
