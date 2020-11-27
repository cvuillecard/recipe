package com.recipe.app.service.ingredient;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public final class IngredientServiceTest extends AbstractTest {
    @Autowired private IngredientService ingredientService;

    @Test
    public void should_create_ingredient_create() {
        Ingredient ingredient = new Ingredient("Kaki", IngredientType.FRUIT);

        // state : bean initialized without id
        Assert.assertNull(ingredient.getId());

        // when : persist
        ingredientService.create(ingredient);

        // then : id has been generated
        Assert.assertNotNull(ingredient.getId());

        // when : delete
        ingredientService.delete(ingredient);

        // then : ingredient doesn't exist in database
        Assert.assertEquals(Optional.empty(), ingredientService.findById(ingredient.getId()));
    }
}
