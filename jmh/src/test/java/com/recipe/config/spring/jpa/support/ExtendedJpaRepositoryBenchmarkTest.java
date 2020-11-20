package com.recipe.config.spring.jpa.support;

import com.recipe.app.entity.ingredient.Ingredient;
import com.recipe.app.service.ingredient.IngredientService;
import com.recipe.app.type.ingredient.IngredientType;
import config.AbstractBenchmark;
import org.junit.BeforeClass;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * test of re-written methods of SimpleJpaRepository forked to ExtendedJpaRepository
 *
 * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository
 * @see com.recipe.config.spring.jpa.support.ExtendedJpaRepositoryImpl
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ExtendedJpaRepositoryBenchmarkTest extends AbstractBenchmark {
    private static IngredientService ingredientService;
    private final List<Ingredient> beanList = new ArrayList<>();

    @BeforeClass
    public static void initBenchmark() {
        MEASUREMENT_ITERATIONS = 3;
        WARMUP_ITERATIONS = 3;
    }

    @Autowired
    public void setIngredientService(final IngredientService ingredientService) {
        ExtendedJpaRepositoryBenchmarkTest.ingredientService = ingredientService;
    }
//
//    @State(Scope.Thread)
//    public static class DeleteAllContext {
//        public static IngredientService ingredientService;
//        public static final List<Ingredient> beanList = new ArrayList<>();
//
//        static {
//            DeleteAllContext.beanList.add(new Ingredient("Lemon", IngredientType.FRUIT));
//            DeleteAllContext.beanList.add(new Ingredient("Mango", IngredientType.FRUIT));
//            DeleteAllContext.beanList.add(new Ingredient("Orange", IngredientType.FRUIT));
//            DeleteAllContext.beanList.add(new Ingredient("Milk", IngredientType.LIQUID));
//            DeleteAllContext.beanList.add(new Ingredient("Beef", IngredientType.ORGANIC));
//
//            ingredientService.saveAll(beanList);
//        }
//    }

    @Setup(Level.Invocation)
    public void setup() {
        beanList.add(new Ingredient("Grenada", IngredientType.FRUIT));
        beanList.add(new Ingredient("Papaya", IngredientType.FRUIT));
//        beanList.add(new Ingredient("Orange", IngredientType.FRUIT));
//        beanList.add(new Ingredient("Milk", IngredientType.LIQUID));
//        beanList.add(new Ingredient("Beef", IngredientType.ORGANIC));
    }

//    @Benchmark
//    public void old_version_delete_all_bean_list_deleteAllSimpleJpaRepository() {
//        ingredientService.deleteAllSimpleJpaRepository(beanList);
//        ingredientService.saveAll(beanList);
//        ingredientService.deleteAllSimpleJpaRepository(beanList);
//    }

    @Benchmark
    public void re_written_version_delete_all_bean_list_deleteAll() {
        ingredientService.deleteAll(beanList);
        ingredientService.insertAll(beanList, false);
        ingredientService.deleteAll(beanList);
    }
}
