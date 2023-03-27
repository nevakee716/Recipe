package recipe.demo.Recipe.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long ingredientId, Ingredient ingredient) {
        Ingredient existingIngredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalStateException("Ingredient not found"));
        existingIngredient.setName(ingredient.getName());
        return ingredientRepository.save(existingIngredient);
    }

    public void deleteIngredient(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }


}
