package com.recipe.app.dao.recipe;

import com.recipe.app.entity.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("recipeDao")
public interface RecipeDao extends JpaRepository<Recipe, Long> {}
