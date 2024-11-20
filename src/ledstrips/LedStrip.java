package ledstrips;

import java.util.ArrayList;
import java.util.Date;

public class LedStrip {
    private final Strip base;
    private final Date creationDate;
    private final int distanceBetweenLed;
    private final ArrayList<Led> ledList;

    public LedStrip(Strip base, Date creationDate, int distanceBetweenLed, ArrayList<Led> ledList) {
        this.base = base;
        this.creationDate = creationDate;
        this.distanceBetweenLed = distanceBetweenLed;
        this.ledList = ledList;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getDistanceBetweenLed() {
        return distanceBetweenLed;
    }

    public ArrayList<Led> getLedList() {
        return ledList;
    }

    public double averageColorTemperature() {
        double avgColorTemperature = 0.0;

        for (Led led : ledList) {
            avgColorTemperature += led.getColorTemperature();
        }

        return avgColorTemperature / ledList.size();
    }

    public Strip getBase() {
        return base;
    }
}
