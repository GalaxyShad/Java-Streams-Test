import java.util.Arrays;
import java.util.Comparator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        var gen = new LedStripGenerator();

        var countList = new int[]{5_000, 50_000, 250_000};

        for (int count : countList) {
            var ledStrips = gen.generate(count);

            System.out.println("\nLedStrip Count: " + count);

            measureLoop(ledStrips);
            measureStream(ledStrips);
            measureOwnCollector(ledStrips);
        }
    }

    private static void measureLoop(LedStrip[] stripList) {
        var start = System.nanoTime();
        var max = 0.0;

        // TODO find another way and parameter to measure statistic
        for (LedStrip strip : stripList) {
            var bright = strip.averageBrightness();

            if (bright > max) {
                max = bright;
            }
        }
        var time = System.nanoTime() - start;
        System.out.println("Loop (ns): " + time);
    }

    private static void measureStream(LedStrip[] stripList) {
        var start = System.nanoTime();
        var max = Arrays
                .stream(stripList)
                .max(Comparator.comparingDouble(LedStrip::averageBrightness));
        var time = System.nanoTime() - start;
        System.out.println("Stream API (ns): " + time);
    }

    private static void measureOwnCollector(LedStrip[] stripList) {
        var start = System.nanoTime();
        var maxBrightness = Arrays.stream(stripList)
                .collect(new LedStripAggregatorCollector());
        var time = System.nanoTime() - start;
        System.out.println("Custom Aggregator (ns): " + time);
    }
}