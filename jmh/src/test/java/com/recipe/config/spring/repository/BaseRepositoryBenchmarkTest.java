package com.recipe.config.spring.repository;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.service.ingredient.IngredientService;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractBenchmark;
import org.junit.BeforeClass;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
/**
 * Object : test performances of custom BaseRepository implementation
 *
 * Note : compare existing methods provided by SimpleJpaRepository with BaseRepository custom methods when possible
 */
public class BaseRepositoryBenchmarkTest extends AbstractBenchmark {
    private static IngredientService ingredientService;
    private Ingredient single;
    private final List<Ingredient> ingredientList = new ArrayList<>();

    @BeforeClass
    public static void initBenchmark() {
        MEASUREMENT_ITERATIONS = 20;
        WARMUP_ITERATIONS = 20;
    }

    @Autowired
    public void setIngredientService(final IngredientService ingredientService) {
        BaseRepositoryBenchmarkTest.ingredientService = ingredientService;
    }

    @Setup(Level.Iteration)
    public void setup() {
        ingredientList.add(single = new Ingredient("Lemon", IngredientType.FRUIT));
        ingredientList.add(new Ingredient("Mango", IngredientType.FRUIT));
        ingredientList.add(new Ingredient("Orange", IngredientType.FRUIT));
        ingredientList.add(new Ingredient("Tabasco", IngredientType.CONDIMENT));
        ingredientList.add(new Ingredient("Milk", IngredientType.LIQUID));
        ingredientList.add(new Ingredient("Beef", IngredientType.ORGANIC));
    }

    // with single bean
    @Benchmark
    public void find_all_with_single_bean_SimpleJpaRepository_findAll() {
        ingredientService.findAll(Example.of(single));
    }

    @Benchmark
    public void find_all_with_single_bean_BaseRepository_findAll() {
        ingredientService.findAll(Collections.singletonList(single));
    }

    // with ingredientList
    @Benchmark
    public void find_all_with_list_SimpleJpaRepository_findAll() {
        for (Ingredient e : ingredientList)
            ingredientService.findAll(Example.of(e));
    }

    @Benchmark
    public void find_all_with_list_BaseRepository_findAll() {
        ingredientService.findAll(ingredientList);
    }
}
