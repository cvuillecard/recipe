package com.recipe.app.bean.recipe.ingredient;

import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.app.bo.recipe.Recipe;
import com.recipe.app.bo.recipe.ingredient.RecipeIngredient;

public final class RecipeIngredientBean implements RecipeIngredient {
    private Recipe recipe;
    private Ingredient ingredient;
    private Integer quantity;

    // getters
    @Override public Recipe getRecipe() { return recipe; }
    @Override public Ingredient getIngredient() { return ingredient; }
    @Override public Integer getQuantity() { return quantity; }

    // setters
    @Override public RecipeIngredient setRecipe(final Recipe recipe) { this.recipe = recipe; return this; }
    @Override public RecipeIngredient setIngredient(final Ingredient ingredient) { this.ingredient = ingredient; return this; }
    @Override public RecipeIngredient setQuantity(final Integer quantity) { this.quantity = quantity; return this; }
}
