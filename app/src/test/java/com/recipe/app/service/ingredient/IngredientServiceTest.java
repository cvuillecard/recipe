package com.recipe.app.service.ingredient;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public final class IngredientServiceTest extends AbstractTest {
    @Autowired private IngredientService ingredientService;

    @Test
    public void should_create_ingredient_create() {
        Ingredient ingredient = new Ingredient("Kaki", IngredientType.FRUIT);

        ingredientService.deleteByExample(ingredient, "id", "recipes");

        // state : bean initialized without id
        Assert.assertNull(ingredient.getId());

        // when : persist
        ingredientService.save(ingredient);

        // then : id has been generated
        Assert.assertNotNull(ingredient.getId());

        // when : delete
        ingredientService.delete(ingredient);

        // then : ingredient doesn't exist in database
        Assert.assertEquals(Optional.empty(), ingredientService.findById(ingredient.getId()));
    }

    @Test
    public void should_find_unique_ingredient_by_name_findByName() {
        // when : find unique ingredient with name "Pepper"
        final Ingredient ingredient = ingredientService.findByName("Cumin");

        // then : name equals
        Assert.assertEquals("Cumin", ingredient.getName());
        Assert.assertEquals(IngredientType.SPICE, ingredient.getType());
    }

    @Test
    public void should_find_unique_ingredient_by_name_and_type_findByNameAndType() {
        // when : find unique ingredient with name "Saffron" and type "SPICE"
        final Ingredient ingredient = ingredientService.findByNameAndType("Saffron", IngredientType.SPICE);

        // then : name equals
        Assert.assertEquals("Saffron", ingredient.getName());
        Assert.assertEquals(IngredientType.SPICE, ingredient.getType());
    }

    @Test
    public void should_find_all_ingredients_by_name_findAllByName() {
        // when : find all ingredients with name "Pepper"
        final List<Ingredient> results = (List<Ingredient>) ingredientService.findAllByName("Pepper");

        // then : name equals
        Assert.assertEquals(2, results.size());
        Assert.assertEquals("Pepper", results.get(0).getName());
        Assert.assertEquals("Pepper", results.get(1).getName());
        // then : different type because of unique constraint
        Assert.assertNotEquals(results.get(0).getType(), results.get(1).getType());
    }

    @Test
    public void should_find_all_ingredients_by_type_findAllByType() {
        // when : find all ingredients with name "Pepper"
        final List<Ingredient> results = (List<Ingredient>) ingredientService.findAllByType(IngredientType.FRUIT);

        // then : name equals
        Assert.assertTrue(results.size() > 1);

        IntStream.range(0, results.size()).forEach(i -> Assert.assertEquals(IngredientType.FRUIT, results.get(i).getType()));
    }
}
