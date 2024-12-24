package ledstrips;

import ledstrips.domain.LedStrip;
import ledstrips.grouper.*;
import utils.NanoTimeMeasurer;

public class LedStripListPerformanceTests {
    public static long measureIterative(LedStrip[] stripList) {
        return NanoTimeMeasurer.executeAndGetTime(() -> new LedStripGrouperIterative().group(stripList));
    }

    public static long measureStream(LedStrip[] stripList) {
        return NanoTimeMeasurer.executeAndGetTime(() -> new LedStripGrouperStream().group(stripList));
    }

    public static long measureStream(LedStrip[] stripList, boolean useParallel, int delayNanos) {
        return NanoTimeMeasurer.executeAndGetTime(() -> {
            var grouper = new LedStripGrouperStream().useDelay(delayNanos);

            if (useParallel) {
                grouper.useParallel();
            }

            return grouper.group(stripList);
        });
    }

    public static long measureForkJoin(LedStrip[] stripList, int delayNanos) {
        return NanoTimeMeasurer.executeAndGetTime(() -> new LedStripGrouperForkJoin(delayNanos).group(stripList));
    }

    public static long measureOwnCollector(LedStrip[] stripList) {
        return NanoTimeMeasurer.executeAndGetTime(() -> new LedStripGrouperOwnCollector().group(stripList));
    }

    public static long measureRxJava(LedStrip[] stripList, boolean useParallel, int delayNanos) {
        return NanoTimeMeasurer.executeAndGetTime(() -> {
            var grouper = new LedStripGrouperRxJava().useDelay(delayNanos);

            if (useParallel) {
                grouper.useParallel();
            }

            return grouper.group(stripList);
        });
    }

    public static long measureRxJavaFlowable(LedStrip[] stripList, boolean useParallel, int delayNanos) {
        return NanoTimeMeasurer.executeAndGetTime(() -> {
            var grouper = new LedStripGrouperRxJavaFlowable().useDelay(delayNanos);

            if (useParallel) {
                grouper.useParallel();
            }

            return grouper.group(stripList);
        });
    }
}
