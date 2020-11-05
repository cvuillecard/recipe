package revisions.algo;

import org.junit.Assert;
import org.junit.Test;

public class DichotomicSearchTest {
    static boolean dichotomicSearch(int[] ints, int k ) {
        if (ints == null || ints.length == 0) return false;
        int startIdx =  0, len = ints.length - 1, endIdx =  len, idx = -1, middle ;

        do{
            middle =  (startIdx + endIdx) / 2;
            if ( k == ints[middle])  idx = middle;
            else if  ( ints[middle] < k )  startIdx =  middle + 1 ;
            else endIdx =  middle-1 ;
        }
        while ( ( k != ints[middle] ) & ( startIdx <= endIdx ) );

        return idx > -1;
    }

    @Test
    public void firstTest() {
        int[] ints = {-9, 14, 37, 102};

        // true
        Assert.assertTrue(dichotomicSearch(ints, -9));
        Assert.assertTrue(dichotomicSearch(ints, 14));
        Assert.assertTrue(dichotomicSearch(ints, 37));
        Assert.assertTrue(dichotomicSearch(ints, 102));
        // false
        Assert.assertFalse(dichotomicSearch(ints,42));
        Assert.assertFalse(dichotomicSearch(ints,53));
    }
}
