import java.util.*;

public class LedStripGenerator {
    private static final String[] COMPANY_NAMES = {"AMD", "Oracle", "SafeLight", "Duralight", "DearLight"};
    private static final Random RANDOM = new Random();

    private static final LedStripProducerInfo[] PRODUCERS = Arrays.stream(COMPANY_NAMES).map(x -> new LedStripProducerInfo(x.toLowerCase() + ".com", x)).toArray(LedStripProducerInfo[]::new);

    public LedStrip[] generate(long count) {
        var ledStrips = new ArrayList<LedStrip>();

        var baseList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            var producer = PRODUCERS[RANDOM.nextInt(PRODUCERS.length)];
            var name = producer.name() + " ULTRA MK" + i * 1000;
            var stripColor = getRandomColor();

            // TODO first generate strips and put references to each here (maybe move it to separate method)
            var stripBase = new Strip(name, producer, stripColor);

            Date creationDate = new Date();
            int distanceBetweenLed = RANDOM.nextInt(10) * 10 + 30;
            var ledList = generateLeds(RANDOM.nextInt(50) + 1);

            LedStrip ledStrip = new LedStrip(stripBase, creationDate, distanceBetweenLed, ledList);
            ledStrips.add(ledStrip);
        }

        return ledStrips.toArray(new LedStrip[0]);
    }

    private LedStripColor getRandomColor() {
        LedStripColor[] colors = LedStripColor.values();
        return colors[RANDOM.nextInt(colors.length)];
    }

    private ArrayList<Led> generateLeds(int count) {
        var leds = new ArrayList<Led>();

        for (int i = 0; i < count; i++) {
            double brightness = RANDOM.nextDouble();
            Size size = new Size(RANDOM.nextInt(10) + 1, RANDOM.nextInt(10) + 1);
            leds.add(new Led(brightness, size));
        }

        return leds;
    }
}
