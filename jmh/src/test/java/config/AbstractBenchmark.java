package config;

import com.recipe.config.spring.JpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JpaConfig.class)
@ActiveProfiles("test")
public abstract class AbstractBenchmark {
    protected static Integer MEASUREMENT_ITERATIONS = 1;
    protected static Integer WARMUP_ITERATIONS = 1;
    protected static Integer FORK = 0;
    protected static Integer THREADS = 1;

    @Test
    public void executeBenchmarkTest() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS)
                // do not use forking or the benchmark methods will not see references stored within its class
                .forks(FORK)
                // do not use multiple threads
                .threads(THREADS)
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.TEXT)
                .result(this.getClass().getSimpleName() + ".results")
                .shouldFailOnError(true)
                .jvmArgs("-server -Xms256m -Xmx2048m")
                .build();

        new Runner(opt).run();
    }
}
