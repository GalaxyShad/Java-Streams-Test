package ledstrips;

import utils.Delayer;

import java.util.*;
import java.util.stream.Collectors;

public class Measuraments {
    public static long measureIterative(LedStrip[] stripList) {
        var start = System.nanoTime();

        Map<Integer, ArrayList<LedStrip>> groupedByDistanceBetweenLeds = new HashMap<>();

        for (LedStrip strip : stripList) {
            int distance = strip.getDistanceBetweenLed();
            groupedByDistanceBetweenLeds.computeIfAbsent(distance, _ -> new ArrayList<>()).add(strip);
        }

        var time = System.nanoTime() - start;


        return time;
    }

    public static long measureStream(LedStrip[] stripList) {
        var start = System.nanoTime();

        var groupedByDistanceBetweenLeds = Arrays
                .stream(stripList)
                .collect(Collectors.groupingBy(LedStrip::getDistanceBetweenLed));

        var time = System.nanoTime() - start;

        return time;
    }

    public static long measureStream(LedStrip[] stripList, boolean useParallel, int delayNanos) {
        var start = System.nanoTime();

        var stream = Arrays.stream(stripList);

        if (useParallel) {
            stream = stream.parallel();
        }

        var groupedByDistanceBetweenLeds = Arrays
                .stream(stripList)
                .collect(Collectors.groupingBy(x -> Delayer.delayAndExecute(delayNanos, x::getDistanceBetweenLed, 0)));

        var time = System.nanoTime() - start;

        return time;
    }

    public static long measureOwnCollector(LedStrip[] stripList) {
        var start = System.nanoTime();

        var groupedByDistanceBetweenLeds = Arrays.stream(stripList)
                .collect(new LedStripCollector());

        var time = System.nanoTime() - start;

        return time;
    }
}
