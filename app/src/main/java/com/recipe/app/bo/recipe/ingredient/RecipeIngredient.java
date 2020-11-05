package com.recipe.app.bo.recipe.ingredient;

import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.app.bo.recipe.Recipe;
import com.recipe.app.type.ingredient.IngredientType;
import com.recipe.config.spring.bo.Entity;

import java.io.Serializable;

public interface RecipeIngredient extends Serializable {
    Recipe getRecipe();
    Ingredient getIngredient();
    Integer getQuantity();

    RecipeIngredient setRecipe(final Recipe recipe);
    RecipeIngredient setIngredient(final Ingredient ingredient);
    RecipeIngredient setQuantity(final Integer quantity);
}
