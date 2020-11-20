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

/**
 * Object : test performances of custom ExtensibleRepository implementation
 *
 * Note : compare existing methods provided by SimpleJpaRepository with ExtensibleRepository custom methods when possible
 *
 * @see com.recipe.config.spring.repository.ExtensibleRepository
 * @see com.recipe.config.spring.jpa.support.ExtendedJpaRepositoryImpl
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ExtensibleRepositoryBenchmarkTest extends AbstractBenchmark {
    private static IngredientService ingredientService;
    private Ingredient single;
    private final List<Ingredient> beanList = new ArrayList<>();

    @BeforeClass
    public static void initBenchmark() {
        MEASUREMENT_ITERATIONS = 20;
        WARMUP_ITERATIONS = 20;
    }

    @Autowired
    public void setIngredientService(final IngredientService ingredientService) {
        ExtensibleRepositoryBenchmarkTest.ingredientService = ingredientService;
    }

    @Setup(Level.Iteration)
    public void setup() {
        beanList.add(single = new Ingredient("Lemon", IngredientType.FRUIT));
        beanList.add(new Ingredient("Mango", IngredientType.FRUIT));
        beanList.add(new Ingredient("Orange", IngredientType.FRUIT));
        beanList.add(new Ingredient("Tabasco", IngredientType.CONDIMENT));
        beanList.add(new Ingredient("Milk", IngredientType.LIQUID));
        beanList.add(new Ingredient("Beef", IngredientType.ORGANIC));
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

    // with beanList
    @Benchmark
    public void find_all_with_list_SimpleJpaRepository_findAll() {
        for (Ingredient e : beanList)
            ingredientService.findAll(Example.of(e));
    }

    @Benchmark
    public void find_all_with_list_BaseRepository_findAll() {
        ingredientService.findAll(beanList);
    }
}
