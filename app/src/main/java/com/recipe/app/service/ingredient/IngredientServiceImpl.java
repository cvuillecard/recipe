package com.recipe.app.service.ingredient;

import com.recipe.app.bean.ingredient.IngredientBean;
import com.recipe.config.spring.service.AbstractService;
import com.recipe.app.repository.ingredient.IngredientRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service("ingredientService")
public class IngredientServiceImpl extends AbstractService<IngredientBean, Long> implements IngredientService {
    @Autowired public IngredientServiceImpl(final IngredientRepository dao) { super(dao); }
}
