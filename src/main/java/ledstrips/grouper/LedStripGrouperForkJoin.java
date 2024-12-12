package ledstrips.grouper;

import ledstrips.domain.LedStrip;
import utils.Delayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class LedStripGrouperForkJoin implements ILedStripGrouper {

    private final int delayNanos;

    public LedStripGrouperForkJoin(int delayNanos) {
        this.delayNanos = delayNanos;
    }

    @Override
    public Map<Integer, List<LedStrip>> group(LedStrip[] ledStripList) {
        try (ForkJoinPool pool = new ForkJoinPool(Math.min(Runtime.getRuntime().availableProcessors(), 4))) {
            var task = new LedStripGroupingTask(ledStripList, delayNanos, 1_000);

            return pool.invoke(task);
        }
    }
}

class LedStripGroupingTask extends RecursiveTask<Map<Integer, List<LedStrip>>> {
    private final int threshold;
    private final LedStrip[] ledStripList;
    private final int delayNanos;

    public LedStripGroupingTask(LedStrip[] ledStripList, int delayNanos, int threshold) {
        this.ledStripList = ledStripList;
        this.delayNanos = delayNanos;
        this.threshold = threshold;
    }

    @Override
    protected Map<Integer, List<LedStrip>> compute() {
        if (ledStripList.length <= threshold) {
            return computeDirectly();
        } else {
            int mid = ledStripList.length / 2;

            LedStripGroupingTask leftTask = new LedStripGroupingTask(Arrays.copyOfRange(ledStripList, 0, mid), delayNanos, threshold);
            LedStripGroupingTask rightTask = new LedStripGroupingTask(Arrays.copyOfRange(ledStripList, mid, ledStripList.length), delayNanos, threshold);

            leftTask.fork();

            Map<Integer, List<LedStrip>> rightResult = rightTask.compute();
            Map<Integer, List<LedStrip>> leftResult = leftTask.join();

            return mergeResults(leftResult, rightResult);
        }
    }

    private Map<Integer, List<LedStrip>> computeDirectly() {
        return Arrays.stream(ledStripList)
                .collect(Collectors.groupingByConcurrent(
                        x -> Delayer.delayAndExecute(delayNanos, x::getDistanceBetweenLed, 0)
                ));
    }

    private Map<Integer, List<LedStrip>> mergeResults(Map<Integer, List<LedStrip>> leftResult, Map<Integer, List<LedStrip>> rightResult) {
        var mergedResult = new ConcurrentHashMap<>(leftResult);

        mergedResult.putAll(leftResult);
        mergedResult.putAll(rightResult);

        return mergedResult;
    }
}