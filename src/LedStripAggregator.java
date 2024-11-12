public class LedStripAggregator {
    private double maxColorTemperature = 0.0;

    public void accumulate(LedStrip strip) {
        double colorTemperature = strip.averageColorTemperature();
        if (colorTemperature > maxColorTemperature) {
            maxColorTemperature = colorTemperature;
        }
    }

    public LedStripAggregator combine(LedStripAggregator other) {
        if (other.maxColorTemperature > maxColorTemperature) {
            maxColorTemperature = other.maxColorTemperature;
        }
        return this;
    }

    public double getMaxColorTemperature() {
        return maxColorTemperature;
    }
}
