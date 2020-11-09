package com.recipe.app.entity.recipe.ingredient;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.entity.recipe.Recipe;

public final class RecipeIngredient {
    private Recipe recipe;
    private Ingredient ingredient;
    private Integer quantity;

    // getters
    public Recipe getRecipe() { return recipe; }
    public Ingredient getIngredient() { return ingredient; }
    public Integer getQuantity() { return quantity; }

    // setters
    public RecipeIngredient setRecipe(final Recipe recipe) { this.recipe = recipe; return this; }
    public RecipeIngredient setIngredient(final Ingredient ingredient) { this.ingredient = ingredient; return this; }
    public RecipeIngredient setQuantity(final Integer quantity) { this.quantity = quantity; return this; }
}
