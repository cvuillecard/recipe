package com.recipe.app.service.ingredient;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.type.ingredient.IngredientType;
import com.recipe.config.spring.service.GenericService;

public interface IngredientService extends GenericService<Ingredient, Long> {
    Ingredient findByName(final String name);
    Ingredient findByNameAndType(final String name, final IngredientType type);
    Iterable<Ingredient> findAllByType(final IngredientType type);
    Iterable<Ingredient> findAllByName(final String name);
}
