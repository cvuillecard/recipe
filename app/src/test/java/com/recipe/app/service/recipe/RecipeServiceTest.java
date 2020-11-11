package com.recipe.app.service.recipe;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.service.ingredient.IngredientService;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class RecipeServiceTest extends AbstractTest {
    @Autowired private IngredientService ingredientService;
    @Autowired private RecipeService recipeService;

    @Test
    public void should_create_blueLagoon_coktail_create() {
        final Ingredient lemon = new Ingredient("Lemon", IngredientType.FRUIT);
        final Ingredient lemonJuice = new Ingredient("Lemon juice", IngredientType.LIQUID);
        final Ingredient curacao = new Ingredient("Blue curacao", IngredientType.LIQUID);
        final Ingredient vodka = new Ingredient("Vodka", IngredientType.LIQUID);
        final Ingredient ice = new Ingredient("Ice", IngredientType.ORGANIC);
        final List<Ingredient> ingredients = (List<Ingredient>)ingredientService.findAll(Arrays.asList(lemon, lemonJuice, curacao, vodka, ice), "id", "recipes");

        // state : Take a glass, with crushed ice, your hard drinks and check you found all the ingredients necessary..
        Assert.assertEquals(5, ingredients.size());
    }
}
