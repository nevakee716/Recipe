package recipe.demo.Recipe.recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Ingredient {

    @Id
    @SequenceGenerator(
            name = "ingredient_sequence",
            sequenceName = "ingredient_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ingredient_sequence"
    )
    private Long id;
    private String name;
    @JsonIgnoreProperties("ingredient")
    @OneToMany(mappedBy = "ingredient",  orphanRemoval = true)
    private List<RecipeIngredient> recipesIngredients;


    public Ingredient(String name) {
        this.name = name;
        this.recipesIngredients = new ArrayList<RecipeIngredient>();
    }

}
