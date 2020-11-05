package revisions.algo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class FizzBuzzTest {
    public final Logger LOG = LoggerFactory.getLogger(FizzBuzzTest.class);

    private static class Katas {
        public static Map<Integer, String> numbers = null;

        public static String numberToString(final Integer n) {
            return String.valueOf(n);
        }

        public static String multipleOfNumbersToString(final Integer n) {
            String ret = "";
            for (Map.Entry<Integer, String> entry : numbers.entrySet())
                if (n % entry.getKey() == 0)
                    ret += entry.getValue();

            return ret;
        }

        public static String transformNumberToString(final Integer n, final String str) {
            return (n / 10 > 0) ? transformNumberToString(n / 10, str + pickDigitFromNumber(n % 10)) : str + pickDigitFromNumber(n);
        }

        private static String pickDigitFromNumber(Integer n) {
            return numbers.get(n) != null ? numbers.get(n) : "";
        }

        public static String numberWithMultipleToString(final Integer n) {
            String ret = transformNumberToString(n, multipleOfNumbersToString(n));
            return ret.isEmpty() ? numberToString(n) : ret;
        }
    }

    @Before
    public void initKatas() {
        if (Katas.numbers == null) {
            Katas.numbers = new LinkedHashMap<>();
            Katas.numbers.put(7, "Sept");
            Katas.numbers.put(5, "Cinq");
            Katas.numbers.put(3, "Trois");
        }
    }

    @Test
    public void testNumberWithMultipleToString() {
        Assert.assertEquals(Katas.numberWithMultipleToString(3), "TroisTrois");
        Assert.assertEquals(Katas.numberWithMultipleToString(8), "8");
        Assert.assertEquals(Katas.numberWithMultipleToString(9), "Trois");
        Assert.assertEquals(Katas.numberWithMultipleToString(21), "SeptTrois");
        Assert.assertEquals(Katas.numberWithMultipleToString(35), "SeptCinqCinqTrois");
        Assert.assertEquals(Katas.numberWithMultipleToString(42), "SeptTrois");
        Assert.assertEquals(Katas.numberWithMultipleToString(51), "TroisCinq");
        Assert.assertEquals(Katas.numberWithMultipleToString(54), "TroisCinq");
        Assert.assertEquals(Katas.numberWithMultipleToString(70), "SeptCinqSept");
        Assert.assertEquals(Katas.numberWithMultipleToString(105), "SeptCinqTroisCinq");
    }
}
