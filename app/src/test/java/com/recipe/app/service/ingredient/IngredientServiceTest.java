package com.recipe.app.service.ingredient;

import com.recipe.app.bean.ingredient.IngredientBean;
import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.app.type.ingredient.IngredientType;
import com.recipe.config.spring.JpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JpaConfig.class)
public final class IngredientServiceTest {
    @Autowired private IngredientService ingredientService;

    @Test
    public void should_create_ingredient_create() {
        Ingredient ingredient = new IngredientBean("Kaki", IngredientType.FRUIT);

        // state : bean initialized without id
        Assert.isNull(ingredient.getId(), "Ingredient id must be null for the test");

        // when : persist
        ingredientService.create(ingredient);

        // then : id has been generated
        Assert.notNull(ingredient.getId(), "Ingredient not persisted [id = null]");

        // when : delete
        ingredientService.delete(ingredient);

        // then : ingredient doesn't exist in database
        Assert.isNull(ingredientService.findById(ingredient.getId()), "Ingredient not deleted : exists in database");
    }
}
