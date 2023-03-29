package recipe.demo.Recipe.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    @Query(value = "SELECT * FROM recipe r WHERE LOWER(r.name) = LOWER(:name)", nativeQuery = true)
    List<Recipe> findByName(String name);
}

