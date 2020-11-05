package com.recipe.app.service.recipe;

import com.recipe.config.spring.service.AbstractService;
import com.recipe.app.bo.recipe.Recipe;
import com.recipe.app.dao.recipe.RecipeDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service("recipeService")
public class RecipeServiceImpl extends AbstractService<Recipe, Long> implements RecipeService {
    @Autowired public RecipeServiceImpl(final RecipeDao dao) { super(dao); }
}
