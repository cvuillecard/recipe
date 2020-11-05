package revisions.algo;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementarFunctionsTest {
    public final Logger LOG = LoggerFactory.getLogger(ElementarFunctionsTest.class);

    public int factorielle(int n) {
        return n <= 1 ? n : n * factorielle(n - 1);
    }

    public int fibonnaciTerminal(int n, int a, int b) {
        return n >= 0 ? fibonnaciTerminal(n - 1, b, a + b) : a;
    }

    public int pow(int n, int power) {
        return power <= 1 ? n : n * pow(n, power - 1);
    }

    public String toBinary(int n) {
        return n <= 1 ? String.valueOf(n) : toBinary(n / 2) + String.valueOf(n % 2);
    }

    @Test
    public void testPow() { Assert.assertEquals(pow(4, 2), 16); }

    @Test
    public void testFactorielle() {
        Assert.assertEquals(factorielle(4), 24);
    }

    @Test
    public void testFibonacci() {
        Assert.assertEquals(fibonnaciTerminal(5, 0, 1), 8);
    }

    @Test
    public void testToBinary() {
        Assert.assertEquals(String.format("%06d%s",0, toBinary(3)), "00000011");
        Assert.assertEquals(toBinary(128), "10000000");
    }

    static void main() {

    }
}
