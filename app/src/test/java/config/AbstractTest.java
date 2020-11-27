package config;

import com.recipe.config.spring.JpaConfig;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = JpaConfig.class)
@ActiveProfiles("test")
@Transactional(propagation= Propagation.REQUIRES_NEW)
public abstract class AbstractTest {
        protected final static Logger LOG = LoggerFactory.getLogger(AbstractTest.class);
        protected static long time = 0;
        protected static TimeUnit TIME_UNIT = TimeUnit.NANOSECONDS;

        private static void logInfo(final String desc, final String status, long nanos) {
            LOG.info(String.format(">>> Test '%s' %s, spent %d microseconds", desc, status, TIME_UNIT.toMicros(nanos)));
        }

        @Rule
        public Stopwatch stopwatch = new Stopwatch() {
            private String formatDesc(final Description desc) {
                return desc.getMethodName();
            }

            @Override
            protected void succeeded(final long nanos, final Description desc) {
                logInfo(formatDesc(desc), "SUCCESSED", nanos);
            }

            @Override
            protected void failed(final long nanos, final Throwable e, final Description desc) {
                logInfo(formatDesc(desc), "FAILED", nanos);
            }

            @Override
            protected void skipped(final long nanos, final AssumptionViolatedException e, final Description desc) {
                logInfo(formatDesc(desc), "SKIPPED", nanos);
            }

            @Override
            protected void finished(final long nanos, final Description desc) {
                logInfo(formatDesc(desc), "FINISHED", nanos);
            }
        };

    /**
     * pick time using TIME_UNIT at this point
     * @return time in the TIME_UNIT format
     */
    protected long watch() { return time = stopwatch.runtime(TIME_UNIT); }

    /**
     * pick the time at this point and returns the delta with time. Then returns the delta with time snapshoted before
     * Note : to be called after a first call of watch() method
     *
     * @see AbstractTest#watch()
     * @see AbstractTest#stopWatch(String)
     * @return the delta difference between time (snapshoted time setted by watch() method) and the current time
     */
    protected long stopWatch() { return stopwatch.runtime(TIME_UNIT) - time; }

    /**
     * pick the time at this point and print a message with info level. Then returns the delta with time snapshoted before
     * Note : to be called after a first call of watch() method
     *
     * @see AbstractTest#watch()
     * @see AbstractTest#stopWatch()
     * @return the delta difference between time (snapshoted time setted by watch() method) and the current time
     */
    protected long stopWatch(final String logMessage) {
        final long stop = stopwatch.runtime(TIME_UNIT) - time;
        LOG.info(logMessage + " : " + stop);
        return stop;
    }

    protected long delta(final long st, final long et) { return st - et; }
    protected long factor(final long st, final long et) { return  st / et; }
}
