package recipe.demo.Recipe.recipe;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table
public class Keyword {
    @Id
    @SequenceGenerator(
            name = "keyword_sequence",
            sequenceName = "keyword_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "keyword_sequence"
    )
    private Long id;
    private String name;

    @ManyToMany
    private List<Recipe> RecipeList;

    public Keyword(String name) {
        this.name = name;
    }


}
