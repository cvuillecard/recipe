package com.recipe.app.dao.ingredient;

import com.recipe.app.entity.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ingredientDao")
public interface IngredientDao extends JpaRepository<Ingredient, Long> {}
