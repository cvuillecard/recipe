package com.recipe.app.service.ingredient;

import com.recipe.app.bean.ingredient.IngredientBean;
import com.recipe.app.type.ingredient.IngredientType;
import com.recipe.config.spring.JpaConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JpaConfig.class)
public final class IngredientServiceTest {
    @Autowired private IngredientService ingredientService;

    @Test
    public void should_create_ingredient_create() {
        IngredientBean ingredient = new IngredientBean("Kaki", IngredientType.FRUIT);

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
