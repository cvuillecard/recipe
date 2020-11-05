package com.recipe.app.dao.recipe;

import com.recipe.app.bean.recipe.RecipeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("recipeDao")
public interface RecipeDao extends JpaRepository<RecipeBean, Long> {}
