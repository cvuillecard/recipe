package com.recipe.app.service.ingredient;

import com.recipe.config.spring.service.AbstractService;
import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.app.dao.ingredient.IngredientDao;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service("ingredientService")
public class IngredientServiceImpl extends AbstractService<Ingredient, Long> implements IngredientService {
    @Autowired public IngredientServiceImpl(final IngredientDao dao) { super(dao); }
}
