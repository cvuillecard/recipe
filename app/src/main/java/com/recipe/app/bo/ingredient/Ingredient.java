package com.recipe.app.bo.ingredient;

import com.recipe.app.bo.recipe.Recipe;
import com.recipe.app.type.ingredient.IngredientType;
import com.recipe.config.spring.bo.Entity;

import java.util.Set;

public interface Ingredient extends Entity<Long> {
    String getName();
    IngredientType getType();
    Set<Recipe> getRecipes();

    Ingredient setName(final String name);
    Ingredient setType(final IngredientType type);
    Ingredient setRecipes(final Set<Recipe> recipes);
}
