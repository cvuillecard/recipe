package com.recipe.config.spring.repository;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.service.ingredient.IngredientService;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractBenchmark;
import org.junit.BeforeClass;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BaseRepositoryBenchmarkTest extends AbstractBenchmark {
    private static IngredientService ingredientService;
    private Ingredient lemon;

    @BeforeClass
    public static void initBenchmark() {
        MEASUREMENT_ITERATIONS = 3;
        WARMUP_ITERATIONS = 3;
    }

    @Autowired
    public void setIngredientService(final IngredientService ingredientService) {
        BaseRepositoryBenchmarkTest.ingredientService = ingredientService;
    }

    @Setup(Level.Invocation)
    public void setup() {
        lemon = new Ingredient("Lemon", IngredientType.FRUIT);
    }

    @Benchmark
    public void findAllWithSingleExampleSpringDataImplementation() {
        ingredientService.findAll(Example.of(lemon));
    }

    @Benchmark
    public void findAllWithIterableCustomImplementation() {
        ingredientService.findAll(Collections.singletonList(lemon));
    }
}
