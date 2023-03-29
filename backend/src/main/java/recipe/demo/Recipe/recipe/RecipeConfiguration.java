package recipe.demo.Recipe.recipe;

import org.hibernate.annotations.Bag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Configuration
public class RecipeConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, KeywordRepository keywordRepository) {
        return args -> {

            // Check or Create Default Ingredients
            List<Ingredient> ingredientsList = new ArrayList<>();
            List.of("Boeuf","Navet","Carotte","Crème","Veau").forEach(ing -> {
                List<Ingredient> r = ingredientRepository.findByName(ing);
                if(r.size() > 0) ingredientsList.add(r.get(0));
                else {
                    Ingredient newIngredient = new Ingredient(ing);
                    ingredientRepository.save(newIngredient);
                    ingredientsList.add(newIngredient);
                }
            });

            // Create Default Ingredients
            if(recipeRepository.findAll().size() == 0) {
                // Create Recipe 1
                Recipe blanquette = new Recipe(
                        "Blanquette de veau",
                        "blahoihaoia djaijdaz ojdaiozjd "
                );
                blanquette.addIngredient(ingredientsList.get(4), "700 g");
                blanquette.addIngredient(ingredientsList.get(3), "50 cl");
                blanquette.addIngredient(ingredientsList.get(2), "500 g");
                // Create Recipe 2
                Recipe pot = new Recipe(
                        "Pot au feu",
                        "djaijdaz dsqd ojdaiozjd "
                );
                pot.addIngredient(ingredientsList.get(0), "1kg");
                pot.addIngredient(ingredientsList.get(1), "250g");
                pot.addIngredient(ingredientsList.get(2), "500g");
                recipeRepository.saveAll(List.of(blanquette, pot));
            }
            Keyword plat = new Keyword("Plat en Sauce");


        };
    }
}
