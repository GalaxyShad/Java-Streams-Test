public class Led {
    private final double colorTemperature;
    private final Size size;

    public Led(double colorTemperature, Size size) {
        this.colorTemperature = colorTemperature;
        this.size = size;
    }

    public double getColorTemperature() {
        return colorTemperature;
    }

    public Size getSize() {
        return size;
    }
}