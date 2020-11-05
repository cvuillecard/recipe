package com.recipe.app.bo.recipe;

import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.app.type.recipe.RecipeType;
import com.recipe.config.spring.bo.Entity;

import java.util.Set;

public interface Recipe extends Entity<Long> {
    String getName();
    RecipeType getType();
    Set<Ingredient> getIngredients();

    Recipe setName(final String name);
    Recipe setType(final RecipeType type);
    Recipe setIngredients(final Set<Ingredient> ingredients);
}
