package com.recipe.app.dao.ingredient;

import com.recipe.app.bean.ingredient.IngredientBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ingredientDao")
public interface IngredientDao extends JpaRepository<IngredientBean, Long> {}
