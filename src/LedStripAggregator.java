public class LedStripAggregator {
    private double maxBrightness = 0.0;

    public void accumulate(LedStrip strip) {
        double bright = strip.averageBrightness();
        if (bright > maxBrightness) {
            maxBrightness = bright;
        }
    }

    public LedStripAggregator combine(LedStripAggregator other) {
        if (other.maxBrightness > maxBrightness) {
            maxBrightness = other.maxBrightness;
        }
        return this;
    }

    public double getMaxBrightness() {
        return maxBrightness;
    }
}
