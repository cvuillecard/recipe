package revisions.algo;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class EulerTest {
    private static final Logger LOG = LoggerFactory.getLogger(EulerTest.class);
    private final Integer VALID_SUM = 233168;

    public int factorielle(int n) {
        if (n <= 1)
            return 1;
        return n * factorielle(n - 1);
    }

    public int fibonacci(int n) {
        if (n < 2)
            return n;
        return fibonacci(n - 2) + fibonacci(n - 1);
    }

    public int fibonacciTerminal(int n, int a, int b) {
        return n >= 0 ? fibonacciTerminal(n - 1, b, a + b): a;
    }

    @Test
    public void testFactorielle() {
        Assert.assertEquals(factorielle(4), 24);
    }

    @Test
    public void testFibonacci() {
        LOG.info(String.valueOf(fibonacci(5)));
    }

    @Test
    public void testFibonacciTerminal() {
        Assert.assertEquals(fibonacciTerminal(5, 0, 1), 8);
    }


    @Test
    public void testMultipleOf5Or3() {
        Integer sum = IntStream.range(0, 1000).filter(n -> n % 3 == 0 || n % 5 == 0).sum();
        Assert.assertEquals(sum, VALID_SUM);
        LOG.info(String.valueOf(sum));
//        IntStream.range(0, 1000).filter(n -> n % 3 == 0 || n % 5 == 0).forEach(n -> { sum.updateAndGet(v -> v + n); LOG.info(String.valueOf(n)); });
    }

    @Test
    public void testCollectMultipleOf5Or3() {
        final List<Integer> list = IntStream.range(0, 1000).filter(n -> n % 3 == 0 || n % 5 == 0).boxed().collect(Collectors.toList());
        final Integer sum = list.stream().reduce(0, Integer::sum);
        Assert.assertEquals(sum, VALID_SUM);
    }
}
