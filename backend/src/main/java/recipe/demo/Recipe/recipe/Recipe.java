package recipe.demo.Recipe.recipe;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.aspectj.bridge.MessageUtil.print;

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


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipesIngredients;

    @ManyToMany
    private List<Keyword> keywordList;
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    public Recipe() {
        this.name = "";
        this.description = "";
        this.commentList = new ArrayList<Comment>();
        this.recipesIngredients = new ArrayList<RecipeIngredient>();
        this.keywordList = new ArrayList<Keyword>();
    }

    public Recipe(String name) {
        this.name = name;
        this.description = "";
        this.instructions = "";
        this.commentList = new ArrayList<Comment>();
        this.recipesIngredients = new ArrayList<RecipeIngredient>();
        this.keywordList = new ArrayList<Keyword>();
    }

    public Recipe(String name, String description) {
        this.name = name;
        this.description = description;
        this.instructions = "";
        this.commentList = new ArrayList<Comment>();
        this.recipesIngredients = new ArrayList<RecipeIngredient>();
        this.keywordList = new ArrayList<Keyword>();
    }

    public Recipe(String name, String description, List<Keyword> keywords) {
        this.name = name;
        this.description = description;
        this.instructions = "";
        this.commentList = new ArrayList<Comment>();
        this.recipesIngredients = new ArrayList<RecipeIngredient>();
        this.keywordList = keywords;
    }

    public void addIngredient(Ingredient ingredient, String quantity) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, quantity);
        this.recipesIngredients.add(recipeIngredient);
        ingredient.recipesIngredientsList().add(recipeIngredient);
    }


    public void removeIngredient(Ingredient ingredient) {
        RecipeIngredient recipeIngredient = new RecipeIngredient(this, ingredient, null);
        this.recipesIngredients.remove(recipeIngredient);
        //ingredient.getRecipesIngredients().remove(recipeIngredient);
    }

    public void emptyIngredients() {
        Iterator<RecipeIngredient> iterator = this.recipesIngredients.iterator();
        while (iterator.hasNext()) {
            RecipeIngredient r = iterator.next();
        //    r.getIngredient().getRecipesIngredients().remove(r);
            iterator.remove();
        }
    }



    public List<QuantityIngredient> ingredientsList() {
        List<QuantityIngredient> r = new ArrayList<>();
        this.recipesIngredients.forEach(ingredientQuantity -> {
            r.add(new QuantityIngredient(ingredientQuantity.getIngredient(),ingredientQuantity.getQuantity()));
        });
        return r;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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


    public List<QuantityIngredient> getIngredientsQuantity() {
        return this.ingredientsList();
    }
    public void setIngredientsQuantity(List<RecipeIngredient> recipesIngredient) {
        this.recipesIngredients = recipesIngredient;
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
               ", keywordList=" + keywordList +
               ", commentList=" + commentList +
                '}';
    }
}


