package com.recipe.app.dao.recipe;

import org.springframework.stereotype.Repository;

import com.recipe.config.spring.dao.AbstractJpaDao;
import com.recipe.app.bo.recipe.Recipe;
import com.recipe.app.bean.recipe.RecipeBean;

@Repository("recipeDao")
public final class RecipeDaoImpl extends AbstractJpaDao<RecipeBean, Recipe, Long> implements RecipeDao {}
