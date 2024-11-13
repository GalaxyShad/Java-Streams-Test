import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        var gen = new LedStripGenerator();

        var countList = new int[]{5_000, 50_000, 250_000};

        for (int i = 0; i < 3; i++) {
            appendToFile(countList[i] + ".csv",  "№ итерации;цикл;стрим;коллектор");
        }


        for (int i = 0; i < 50; i++) {
            for (int count : countList) {
                var ledStrips = gen.generate(count);

                System.out.println("\nLedStrip Count: " + count);

                var loop = measureIterative(ledStrips);
                var stream = measureStream(ledStrips);
                var collector = measureOwnCollector(ledStrips);

                appendToFile(count + ".csv", (i+1) + ";" + loop + ";" + stream + ";" + collector);
            }
        }
    }

    private static long measureIterative(LedStrip[] stripList) {
        var start = System.nanoTime();

        Map<Integer, ArrayList<LedStrip>> groupedByDistanceBetweenLeds = new HashMap<>();

        for (LedStrip strip : stripList) {
            int distance = strip.getDistanceBetweenLed();
            groupedByDistanceBetweenLeds.computeIfAbsent(distance, _ -> new ArrayList<>()).add(strip);
        }

        var time = System.nanoTime() - start;

        System.out.println("Iterative (ns): " + time);

        return time;
    }

    private static long measureStream(LedStrip[] stripList) {
        var start = System.nanoTime();

        var groupedByDistanceBetweenLeds = Arrays
                .stream(stripList)
                .collect(Collectors.groupingBy(LedStrip::getDistanceBetweenLed));

        var time = System.nanoTime() - start;

        System.out.println("Stream API (ns): " + time);

        return time;
    }

    private static long measureOwnCollector(LedStrip[] stripList) {
        var start = System.nanoTime();

        var groupedByDistanceBetweenLeds = Arrays.stream(stripList)
                .collect(new LedStripCollector());

        var time = System.nanoTime() - start;

        System.out.println("Custom Aggregator (ns): " + time);

        return time;
    }
}
