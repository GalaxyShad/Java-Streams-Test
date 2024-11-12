import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void appendToFile(String fileName, String content) {
        try (var writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine(); // Добавляем новую строку после записи
            System.out.println("Data successfully appended to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        var gen = new LedStripGenerator();

        var countList = new int[]{5_000, 50_000, 250_000};

        for (int i = 0; i < 3; i++) {
            appendToFile(countList[i] + ".csv",  "№ итерации;цикл;стрим;коллектор");
        }


        for (int i = 0; i < 50; i++) {
            for (int count : countList) {
                var ledStrips = gen.generate(count);

                System.out.println("\nLedStrip Count: " + count);

                var loop = measureLoop(ledStrips);
                var stream = measureStream(ledStrips);
                var collector = measureOwnCollector(ledStrips);

                appendToFile(count + ".csv", (i+1) + ";" + loop + ";" + stream + ";" + collector);
            }
        }
    }

    private static long measureLoop(LedStrip[] stripList) {
        var start = System.nanoTime();
        var max = 0.0;

        for (LedStrip strip : stripList) {
            var colorTemperature = Delayer.delayAndExecute(2, strip::averageColorTemperature);

            if (colorTemperature > max) {
                max = colorTemperature;
            }
        }
        var time = System.nanoTime() - start;

        System.out.println("Loop (ns): " + time);

        return time;
    }

    private static long measureStream(LedStrip[] stripList) {
        var start = System.nanoTime();
        var max = Arrays
                .stream(stripList)
                .max(Comparator.comparingDouble(LedStrip::averageColorTemperature));
        var time = System.nanoTime() - start;

        System.out.println("Stream API (ns): " + time);

        return time;
    }

    private static long measureOwnCollector(LedStrip[] stripList) {
        var start = System.nanoTime();
        var maxColorTemperature = Arrays.stream(stripList)
                .collect(new LedStripAggregatorCollector());
        var time = System.nanoTime() - start;

        System.out.println("Custom Aggregator (ns): " + time);

        return time;
    }
}
