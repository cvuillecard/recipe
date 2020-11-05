package com.recipe.app.repository.recipe;

import com.recipe.app.bean.recipe.RecipeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("recipeRepository")
public interface RecipeRepository extends JpaRepository<RecipeBean, Long> {}
