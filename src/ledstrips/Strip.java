package ledstrips;

public class Strip {
    private final String name;
    private final LedStripProducerInfo producer;
    private final LedStripColor stripColor;

    public Strip(String name, LedStripProducerInfo producer, LedStripColor stripColor) {
        this.name = name;
        this.producer = producer;
        this.stripColor = stripColor;
    }

    public String getName() {
        return name;
    }

    public LedStripProducerInfo getProducer() {
        return producer;
    }

    public LedStripColor getStripColor() {
        return stripColor;
    }
}
