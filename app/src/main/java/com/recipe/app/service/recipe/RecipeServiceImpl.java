package com.recipe.app.service.recipe;

import com.recipe.app.entity.recipe.Recipe;
import com.recipe.config.spring.service.AbstractService;
import com.recipe.app.repository.recipe.RecipeRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service("recipeService")
public class RecipeServiceImpl extends AbstractService<Recipe, Long> implements RecipeService {
    @Autowired public RecipeServiceImpl(final RecipeRepository dao) { super(dao); }
}
