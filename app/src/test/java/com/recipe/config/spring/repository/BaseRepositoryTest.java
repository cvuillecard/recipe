package com.recipe.config.spring.repository;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.service.ingredient.IngredientService;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BaseRepositoryTest extends AbstractTest {
    @Autowired IngredientService ingredientService;

    /**
     *  findAll(final Iterable<T> iterable, String... excludeFields)
     *
     *  Note : is null clause in query built is not tested because of MCD null constraints preventing null values on all columns of schema
     */
    @Test
    public void should_return_null_if_null_or_empty_iterable_findAll() {
        // when : null parameters
        final List<Ingredient> emptyIterable = (List<Ingredient>) ingredientService.findAll((Iterable<Ingredient>) Collections.EMPTY_LIST, null);
        final List<Ingredient> nullParams = (List<Ingredient>) ingredientService.findAll((Iterable<Ingredient>) null, null);

        // then : returns null
        Assert.assertNull(nullParams);
        Assert.assertNull(emptyIterable);
    }

    @Test
    public void should_find_all_ingredients_iterable_findAll() {
        // state : 3 ingredients existing in database
        final Ingredient chicken = new Ingredient("Chicken", IngredientType.ORGANIC);
        final Ingredient creamFresh = new Ingredient("Cream fresh", IngredientType.CONDIMENT);
        final Ingredient curry = new Ingredient("Curry", IngredientType.SPICE);

        // when : trying to find all ingredients stated
        final List<Ingredient> ingredients = (List<Ingredient>) ingredientService.findAll(Arrays.asList(chicken, creamFresh, curry), "id", "recipes");

        // then : ingredients found
        Assert.assertNotNull(ingredients);
        Assert.assertEquals(3, ingredients.size());

        // then : curry exists
        Assert.assertEquals("Curry", ingredients.get(0).getName());
        Assert.assertEquals(IngredientType.SPICE, ingredients.get(0).getType());

        // then : Cream fresh exists
        Assert.assertEquals("Cream fresh", ingredients.get(1).getName());
        Assert.assertEquals(IngredientType.CONDIMENT, ingredients.get(1).getType());

        // Then : Chicken exists
        Assert.assertEquals("Chicken", ingredients.get(2).getName());
        Assert.assertEquals(IngredientType.ORGANIC, ingredients.get(2).getType());
    }

    @Test
    public void should_return_empty_list_if_no_results_found_findAll() {
        // state : one ingredient not existing in database
        final Ingredient unknownFruit = new Ingredient("Unknown", IngredientType.FRUIT);

        // when : trying to find the unknownFruit
        final List<Ingredient> after = (List<Ingredient>) ingredientService.findAll(Arrays.asList(unknownFruit), "id", "recipes");

        // then : empty list
        Assert.assertTrue(after.isEmpty());
    }

    @Test
    public void should_not_find_existing_record_if_id_is_null_in_query_findAll() {
        // state : one ingredient existing in database
        final Ingredient lemon = new Ingredient("Lemon", IngredientType.FRUIT);

        Assert.assertNull(lemon.getId());

        // when : trying to find the lemon fruit with 'id' field not excluded, so id is null and null clause on id column will be used in where clause of findAll query
        final List<Ingredient> ingredientsWithNullId = (List<Ingredient>) ingredientService.findAll(Collections.singletonList(lemon), "recipes");

        // then : empty list because of id not excluded in findAll query
        Assert.assertEquals(0, ingredientsWithNullId.size());

        // when : trying to find the lemon fruit with 'id' excluded from where clause
        final List<Ingredient> ingredientsNotNullId = (List<Ingredient>) ingredientService.findAll(Collections.singletonList(lemon), "id", "recipes");

        // then : lemon found
        Assert.assertEquals(1, ingredientsNotNullId.size());

        Assert.assertNull(lemon.getId());
        Assert.assertNotNull(ingredientsNotNullId.get(0).getId());
        Assert.assertEquals(lemon.getName(), ingredientsNotNullId.get(0).getName());
        Assert.assertEquals(lemon.getType(), ingredientsNotNullId.get(0).getType());
    }
}
