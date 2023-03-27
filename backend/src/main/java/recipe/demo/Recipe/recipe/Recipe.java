package recipe.demo.Recipe.recipe;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

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

    @Lob
    private String description;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredientsQuantity;

    @ManyToMany
    private List<Keyword> keywordList;
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    public Recipe() {
        this.name = "";
        this.description = "";
    }

    public Recipe(String name) {
        this.name = name;
        this.description = "";
        this.commentList = new ArrayList<Comment>();
        this.ingredientsQuantity = new ArrayList<RecipeIngredient>();
        this.keywordList = new ArrayList<Keyword>();
    }

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
        this.commentList = new ArrayList<Comment>();
        this.ingredientsQuantity = new ArrayList<RecipeIngredient>();
        this.keywordList = new ArrayList<Keyword>();
    }

    public Recipe(String name, String description, List<Keyword> keywords) {
        this.name = name;
        this.description = description;
        this.commentList = new ArrayList<Comment>();
        this.ingredientsQuantity = new ArrayList<RecipeIngredient>();
        this.keywordList = keywords;
    }

    public void addIngredient(Ingredient ingredient, String quantity) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, quantity);
        ingredientsQuantity.add(recipeIngredient);
        ingredient.getRecipes().add(recipeIngredient);
    }


    public void removeIngredient(Ingredient ingredient) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, null);
        ingredientsQuantity.remove(recipeIngredient);
        ingredient.getRecipes().remove(recipeIngredient);
    }

    public HashMap<String,String> getIngredients() {
        HashMap<String,String> r = new HashMap<>();
        this.ingredientsQuantity.forEach(ingredientQuantity -> {
            r.put(ingredientQuantity.getIngredient().getName(),ingredientQuantity.getQuantity());
        });
        return r;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public HashMap<String,String> getIngredientsQuantity() {
        return this.getIngredients();
    }
    public void setIngredientsQuantity(List<RecipeIngredient> ingredientsQuantity) {
        this.ingredientsQuantity = ingredientsQuantity;
    }

    public List<Keyword> getKeywordList() {
      return keywordList;
  }

  public void setKeywordList(List<Keyword> keywordList) {
      this.keywordList = keywordList;
  }

  public List<Comment> getCommentList() {
     return commentList;
  }

  public void setCommentList(List<Comment> commentList) {
      this.commentList = commentList;
  }

    @Override
    public String toString() {
        return "recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
               ", ingredientList=" + getIngredients().toString() +
               ", keywordList=" + keywordList +
               ", commentList=" + commentList +
                '}';
    }
}

