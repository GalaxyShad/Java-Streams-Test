package ledstrips.grouper;

import ledstrips.domain.LedStrip;
import utils.Delayer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class LedStripGrouperStream implements ILedStripGrouper {
    private int delayNanos = 0;
    private boolean isUsingParallel = false;

    public LedStripGrouperStream useDelay(int nanos) {
        delayNanos = nanos;
        return this;
    }

    public LedStripGrouperStream useParallel() {
        isUsingParallel = true;
        return this;
    }

    @Override
    public Map<Integer, List<LedStrip>> group(LedStrip[] ledStripList) {
        var stream = Arrays.stream(ledStripList);

        if (isUsingParallel) {
            stream = stream.parallel();
        }

        return stream.collect(
                Collectors.groupingByConcurrent(
                        x -> Delayer.delayAndExecute(delayNanos, x::getDistanceBetweenLed, 0)
                )
        );
    }
}



