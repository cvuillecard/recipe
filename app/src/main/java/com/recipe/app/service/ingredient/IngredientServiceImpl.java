package com.recipe.app.service.ingredient;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.type.ingredient.IngredientType;
import com.recipe.config.spring.service.AbstractService;
import com.recipe.app.repository.ingredient.IngredientRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service("ingredientService")
public class IngredientServiceImpl extends AbstractService<Ingredient, IngredientRepository, Long> implements IngredientService {
    @Autowired public IngredientServiceImpl(final IngredientRepository repository) { super(repository); }

    @Override public Ingredient findByName(final String name) { return repository().findByName(name); }
    @Override public Ingredient findByNameAndType(final String name, final IngredientType type) { return repository().findByNameAndType(name, type); }
    @Override public Iterable<Ingredient> findAllByName(final String name) { return repository().findAllByName(name); }
    @Override public Iterable<Ingredient> findAllByType(final IngredientType type) { return repository().findAllByType(type); }
}
