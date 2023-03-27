package recipe.demo.Recipe.recipe;

import org.hibernate.annotations.Bag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
public class RecipeConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(RecipeRepository repository, IngredientRepository ingredientRepository, KeywordRepository keywordRepository) {
        return args -> {

            Keyword plat = new Keyword("Plat en Sauce");

            Ingredient boeuf = new Ingredient("Boeuf");
            Ingredient navet = new Ingredient("Navet");
            Ingredient carotte = new Ingredient("Carotte");
            Ingredient creme = new Ingredient("Crème");
            Ingredient veau = new Ingredient("Veau");

            ingredientRepository.saveAll(List.of(boeuf, navet, carotte, creme, veau));


            Recipe blanquette = new Recipe(
                    "Blanquette de veau",
                    "blahoihaoia djaijdaz ojdaiozjd "
            );

            blanquette.addIngredient(veau, "700 g");
            blanquette.addIngredient(creme, "50 cl");
            blanquette.addIngredient(carotte, "500 g");

            Recipe pot = new Recipe(
                    "Pot au feu",
                    "djaijdaz dsqd ojdaiozjd "
            );

            pot.addIngredient(boeuf, "1kg");
            pot.addIngredient(navet, "250g");
            pot.addIngredient(carotte, "500g");


            repository.saveAll(List.of(blanquette, pot));
        };
    }
}
