package com.recipe.config.spring.repository;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.service.ingredient.IngredientService;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ExtensibleRepositoryTest extends AbstractTest {
    @Autowired IngredientService ingredientService;

    /**
     *  findAll(final Iterable<T> iterable)
     *  findAll(final Iterable<T> iterable, final Sort sort)
     */
    @Test
    public void should_throw_exception_if_null_or_empty_param_iterable_findAll() {
        // when : null parameters, java.lang.IllegalArgumentException
        Assert.assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> ingredientService.findAll((Iterable<Ingredient>) Collections.EMPTY_LIST));
        Assert.assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> ingredientService.findAll((Iterable<Ingredient>) null));
        Assert.assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> ingredientService.findAll((Iterable<Ingredient>) null, Sort.by("name").ascending()));
        Assert.assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> ingredientService.findAll((Iterable<Ingredient>) null, null));
    }

    @Test
    public void should_find_all_ingredients_iterable_findAll() {
        // state : 3 ingredients existing in database
        final Ingredient chicken = new Ingredient("Chicken", IngredientType.ORGANIC);
        final Ingredient creamFresh = new Ingredient("Cream fresh", IngredientType.CONDIMENT);
        final Ingredient curry = new Ingredient("Curry", IngredientType.SPICE);

        // when : trying to find all ingredients stated
        final List<Ingredient> ingredients = (List<Ingredient>) ingredientService.findAll(Arrays.asList(chicken, creamFresh, curry));

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
        final List<Ingredient> after = ingredientService.findAll(Arrays.asList(unknownFruit));

        // then : empty list
        Assert.assertTrue(after.isEmpty());
    }

    @Test
    public void should_findAll_by_example_using_custom_impl_and_spring_impl_findAll() {
        // state : 2 fruits existing in database
        final Ingredient lemon = new Ingredient("Lemon", IngredientType.FRUIT);
        final Ingredient mango = new Ingredient("Mango", IngredientType.FRUIT);

        // when : findAll using spring data single parameter and custom ExtensibleRepository impl
        final List<Ingredient> defaultImpl = ingredientService.findAll(Example.of(lemon));
        final List<Ingredient> customImpl = ingredientService.findAll(Arrays.asList(lemon, mango));

        // then : records found
        Assert.assertTrue(!defaultImpl.isEmpty());
        Assert.assertTrue(!customImpl.isEmpty());

        // then : check sizes
        Assert.assertEquals(1, defaultImpl.size());
        Assert.assertEquals(2, customImpl.size());
    }

    @Test
    public void should_findAll_sorting_by_name_ascending_findAll() {
        // state : 2 fruits existing in database
        final Ingredient lemon = new Ingredient("Lemon", IngredientType.FRUIT);
        final Ingredient mango = new Ingredient("Mango", IngredientType.FRUIT);

        // when : trying to find the 2 fruits sorting by name ASC
        final List<Ingredient> sortedResults = ingredientService.findAll(Arrays.asList(lemon, mango), Sort.by("name").ascending());

        // then : not empty list with 2 records
        Assert.assertTrue(!sortedResults.isEmpty());
        Assert.assertEquals(2, sortedResults.size());

        // then : check results are ordered by name ascending
        Assert.assertEquals("Lemon", sortedResults.get(0).getName());
        Assert.assertEquals("Mango", sortedResults.get(1).getName());
    }

    @Test
    public void should_findAll_sorting_by_type_ascending_findAll() {
        // state : 3 different ingredients existing in database
        final Ingredient mustard = new Ingredient("Mustard", IngredientType.SPICE);
        final Ingredient stillWater = new Ingredient("Still Water", IngredientType.LIQUID);
        final Ingredient orange = new Ingredient("Orange", IngredientType.FRUIT);

        // when : trying to find previous list of ingredients
        final List<Ingredient> sortedResults = ingredientService.findAll(Arrays.asList(orange, mustard, stillWater), Sort.by("type").ascending());

        // then : not empty list with 3 records
        Assert.assertTrue(!sortedResults.isEmpty());
        Assert.assertEquals(3, sortedResults.size());

        // then : check results are ordered by type ascending
        Assert.assertEquals(IngredientType.FRUIT, sortedResults.get(0).getType());
        Assert.assertEquals(IngredientType.LIQUID, sortedResults.get(1).getType());
        Assert.assertEquals(IngredientType.SPICE, sortedResults.get(2).getType());

        Assert.assertEquals("Orange", sortedResults.get(0).getName());
        Assert.assertEquals("Still Water", sortedResults.get(1).getName());
        Assert.assertEquals("Mustard", sortedResults.get(2).getName());
    }
}
