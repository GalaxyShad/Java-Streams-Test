import java.util.*;

public class LedStripGenerator {
    private static final String[] COMPANY_NAMES = {"AMD", "Oracle", "SafeLight", "Duralight", "DearLight"};
    private static final Random RANDOM = new Random();

    private static final LedStripProducerInfo[] PRODUCERS = Arrays.stream(COMPANY_NAMES).map(x -> new LedStripProducerInfo(x.toLowerCase() + ".com", x)).toArray(LedStripProducerInfo[]::new);

    // Массив объектов, соответствующих каждой компании
    private static final Map<String, String[]> COMPANY_OBJECTS = new HashMap<String, String[]>() {{
        put("AMD", new String[]{"AMD ULTRA MK", "AMD PRO MK", "AMD ELITE MK"});
        put("Oracle", new String[]{"Oracle LUX MK", "Oracle PREMIUM MK", "Oracle DELUXE MK"});
        put("SafeLight", new String[]{"SafeLight SECURE MK", "SafeLight PROTECT MK", "SafeLight GUARD MK"});
        put("Duralight", new String[]{"Duralight DURA MK", "Duralight TOUGH MK", "Duralight STRONG MK"});
        put("DearLight", new String[]{"DearLight COZY MK", "DearLight WARM MK", "DearLight COMFY MK"});
    }};

    public LedStrip[] generate(long count) {
        var ledStrips = new ArrayList<LedStrip>();

        for (int i = 0; i < count; i++) {
            var producer = PRODUCERS[RANDOM.nextInt(PRODUCERS.length)];
            var companyName = producer.name();
            var objectNames = COMPANY_OBJECTS.get(companyName);
            var name = objectNames[RANDOM.nextInt(objectNames.length)] + " " + i * 1000;
            var stripColor = getRandomColor();

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
            double colorTemperature = RANDOM.nextDouble() * (6000 - 2700) + 2700; // Генерация цветовой температуры в диапазоне от 2700K до 6000K
            Size size = new Size(RANDOM.nextInt(10) + 1, RANDOM.nextInt(10) + 1);
            leds.add(new Led(colorTemperature, size));
        }

        return leds;
    }
}
