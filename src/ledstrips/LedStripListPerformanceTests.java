package ledstrips;

import ledstrips.domain.LedStrip;
import ledstrips.grouper.LedStripGrouperIterative;
import ledstrips.grouper.LedStripGrouperOwnCollector;
import ledstrips.grouper.LedStripGrouperStream;
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

    public static long measureOwnCollector(LedStrip[] stripList) {
        return NanoTimeMeasurer.executeAndGetTime(() -> new LedStripGrouperOwnCollector().group(stripList));
    }
}
