package ledstrips;

import ledstrips.domain.LedStripGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

        lab1Measure(gen, countList);
//        lab2Measure(gen, countList);
    }

    public static void lab2Measure(LedStripGenerator gen, int[] countList) {
        var nanosDelay = 2;
        var iterationsNumber = 1;
        var millisDelayString = nanosDelay + "millis delay";

        for (int i = 0; i < 3; i++) {
            appendToFile(countList[i] + ".csv",
                    "№ итерации;sequential;parallel" +
                    ";sequentual " + millisDelayString +
                    ";parallel " + millisDelayString);
        }

        for (int i = 0; i < iterationsNumber; i++) {
            for (int count : countList) {
                var ledStrips = gen.generate(count);

                System.out.println("\nLedStrip Count: " + count);

                var seq = LedStripListPerformanceTests.measureStream(ledStrips, false, 0);
                System.out.println("Seq: " + seq);

                var par = LedStripListPerformanceTests.measureStream(ledStrips, true, 0);
                System.out.println("Par: " + par);

                var seqDelayed = LedStripListPerformanceTests.measureStream(ledStrips, false, nanosDelay);
                System.out.println("Seq Delayed by " + nanosDelay + "nanos: "  + seqDelayed);

                var parDelayed = LedStripListPerformanceTests.measureStream(ledStrips, true, nanosDelay);
                System.out.println("Par Delayed by " + nanosDelay + "nanos: "  + parDelayed);

                appendToFile(count + ".csv", (i + 1) + ";" + seq + ";" + par + ";" + seqDelayed + ";" + parDelayed);
            }
        }

        System.out.println("Приступаем к поиску количества элементов где параллельный и последовательный способы были бы одинаковыми по времени");

        lab2findWhenSeqAndParTimeDurationEquals(gen);
    }

    public static void lab2findWhenSeqAndParTimeDurationEquals(LedStripGenerator gen) {
        for (int i = 1; i < 50_000; i++) {
            var ledStrips = gen.generate(i);

            var seq = LedStripListPerformanceTests.measureStream(ledStrips, false, 0);
            var par = LedStripListPerformanceTests.measureStream(ledStrips, true, 0);

            if (seq != par)
                continue;

            System.out.println("\nКол-во элементов при котором параллельная реализации : " + i);
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

                var loop = LedStripListPerformanceTests.measureIterative(ledStrips);
                var stream = LedStripListPerformanceTests.measureStream(ledStrips);
                var collector = LedStripListPerformanceTests.measureOwnCollector(ledStrips);

                var dataString = (i+1) + ";\t\t\t" + loop + ";\t\t" + stream + ";\t\t" + collector;

                System.out.println("LedStrip Count: " + count + " data: " + dataString);
                appendToFile(count + ".csv", dataString);
            }
        }
    }
}
