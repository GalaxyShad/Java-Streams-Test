public class Led {
    private final double brightness;
    private final Size size;

    public Led(double brightness, Size size) {
        this.brightness = brightness;
        this.size = size;
    }

    public double getBrightness() {
        return brightness;
    }

    public Size getSize() {
        return size;
    }
}
