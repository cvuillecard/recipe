package com.recipe.app.repository.recipe;

import com.recipe.app.entity.recipe.Recipe;
import com.recipe.app.type.recipe.RecipeType;
import com.recipe.config.spring.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository("recipeRepository")
public interface RecipeRepository extends BaseRepository<Recipe, Long> {
    Recipe findByName(final String name);
    Recipe findByType(final RecipeType type);
}
