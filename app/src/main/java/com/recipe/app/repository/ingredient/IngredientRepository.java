package com.recipe.app.repository.ingredient;

import com.recipe.app.bean.ingredient.IngredientBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ingredientRepository")
public interface IngredientRepository extends JpaRepository<IngredientBean, Long> {}
