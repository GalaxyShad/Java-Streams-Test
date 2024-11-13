import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        var gen = new LedStripGenerator();
        var countList = new int[]{5_000, 50_000, 250_000};

        lab1(gen, countList);
    }

    private static void lab1(LedStripGenerator gen, int[] countList) {
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
}
