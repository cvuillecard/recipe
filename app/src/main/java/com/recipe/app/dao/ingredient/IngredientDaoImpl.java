package com.recipe.app.dao.ingredient;

import com.recipe.app.bean.ingredient.IngredientBean;
import com.recipe.app.bo.ingredient.Ingredient;
import com.recipe.config.spring.dao.AbstractJpaDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("ingredientDao")
public class IngredientDaoImpl extends AbstractJpaDao<IngredientBean, Ingredient, Long> implements IngredientDao {}
