package recipe.demo.Recipe.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipe.demo.Recipe.security.user.User;
import recipe.demo.Recipe.security.user.UserDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@Entity
@Table
public class Recipe {
    @Id
    @SequenceGenerator(
            name = "recipe_sequence",
            sequenceName = "recipe_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_sequence"
    )
    private Long id;
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 5000)
    private String instructions;

    @JsonIgnoreProperties("recipe")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipesIngredients;

    @JsonIgnoreProperties("recipes")
    @ManyToOne(fetch = FetchType.EAGER)
    private User creator;

    @JsonIgnoreProperties("recipes")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Keyword> keywordList;

    @JsonIgnoreProperties("recipe")
    @OneToMany(orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Comment> commentList;

    public Recipe() {
        this.recipesIngredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient, String quantity) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, quantity);
        this.recipesIngredients.add(recipeIngredient);
        ingredient.getRecipesIngredients().add(recipeIngredient);
    }


    public void removeIngredient(Ingredient ingredient) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, null);
        this.recipesIngredients.remove(recipeIngredient);
    }


    public void emptyIngredients() {
        this.recipesIngredients.clear();
    }

    public void removeComment(Long commentId) {
        this.getCommentList().removeIf(comment -> comment.getId().equals(commentId));
    }

    public UserDTO getCreator() {
        return creator.toDTO();
    }


    public String toString() {
        return getName();
    }
}


