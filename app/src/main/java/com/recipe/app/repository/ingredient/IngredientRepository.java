package com.recipe.app.repository.ingredient;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.type.ingredient.IngredientType;
import com.recipe.config.spring.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository("ingredientRepository")
public interface IngredientRepository extends BaseRepository<Ingredient, Long> {
    Ingredient findByName(final String name);
    Ingredient findByNameAndType(final String name, final IngredientType type);
    Iterable<Ingredient> findAllByName(final String name);
    Iterable<Ingredient> findAllByType(final IngredientType type);
}
