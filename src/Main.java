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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        var gen = new LedStripGenerator();
        var countList = new int[]{5_000, 50_000, 250_000};

//        lab1Measure(gen, countList);
        lab2Measure(gen, countList);
    }

    public static void lab2Measure(LedStripGenerator gen, int[] countList) {
        var millisDelay = 2;
        var millisDelayString = millisDelay + "millis delay";

        for (int i = 0; i < 3; i++) {
            appendToFile(countList[i] + ".csv",
                    "№ итерации;sequential;parallel" +
                    ";sequentual " + millisDelayString +
                    ";parallel " + millisDelayString);
        }

        for (int i = 0; i < 15; i++) {
            for (int count : countList) {
                var ledStrips = gen.generate(count);

                System.out.println("\nLedStrip Count: " + count);

                var seq = measureStream(ledStrips, false, 0);
                var par = measureStream(ledStrips, true, 0);

                var seqDelayed = measureStream(ledStrips, false, millisDelay);
                var parDelayed = measureStream(ledStrips, true, millisDelay);

                appendToFile(count + ".csv", (i + 1) + ";" + seq + ";" + par + ";" + seqDelayed + ";" + parDelayed);
            }
        }

        System.out.println("Приступаем к поиску количества элементов где параллельный и последовательный способы были бы одинаковыми по времени");
        lab2findWhenSeqAndParTimeDurationEquals(gen);
    }

    public static void lab2findWhenSeqAndParTimeDurationEquals(LedStripGenerator gen) {
        for (int i = 1; i < 50_000; i++) {
            var ledStrips = gen.generate(i);

            var seq = measureStream(ledStrips, false, 0);
            var par = measureStream(ledStrips, true, 0);

            if (seq != par)
                continue;

            System.out.println("\nКол-во элементов при котором параллельная реализаци : " + i);
        }
    }

    public static void lab1Measure(LedStripGenerator gen, int[] countList) {
        for (int i = 0; i < 3; i++) {
            var labelsString = "№ итерации;цикл;стрим;коллектор";

            appendToFile(countList[i] + ".csv",  labelsString);
            System.out.println(labelsString);
        }


        for (int i = 0; i < 50; i++) {
            for (int count : countList) {
                var ledStrips = gen.generate(count);

                var loop = Measurament.measureIterative(ledStrips);
                var stream = Measurament.measureStream(ledStrips);
                var collector = Measurament.measureOwnCollector(ledStrips);

                var dataString = (i+1) + ";\t\t\t" + loop + ";\t\t" + stream + ";\t\t" + collector;

                System.out.println("LedStrip Count: " + count + " data: " + dataString);
                appendToFile(count + ".csv", dataString);
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

    private static long measureStreamSequential(LedStrip[] stripList) {
        return measureStream(stripList, false, 0);
    }

    private static long measureStream(LedStrip[] stripList, boolean useParallel, int delayMillis) {
        var start = System.nanoTime();

        var stream = Arrays.stream(stripList);

        if (useParallel) {
            stream = stream.parallel();
        }

        var max = stream.max(Comparator.comparingDouble((x) -> Delayer.delayAndExecute(delayMillis, x::averageColorTemperature)));

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
