import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Measurament {
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

    public static long measureOwnCollector(LedStrip[] stripList) {
        var start = System.nanoTime();

        var groupedByDistanceBetweenLeds = Arrays.stream(stripList)
                .collect(new LedStripCollector());

        var time = System.nanoTime() - start;

        return time;
    }
}
