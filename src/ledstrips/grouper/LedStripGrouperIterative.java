package ledstrips.grouper;

import ledstrips.domain.LedStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LedStripGrouperIterative implements ILedStripGrouper {

    @Override
    public Map<Integer, List<LedStrip>> group(LedStrip[] ledStripList) {
        Map<Integer, List<LedStrip>> groupedByDistanceBetweenLeds = new HashMap<>();

        for (LedStrip strip : ledStripList) {
            int distance = strip.getDistanceBetweenLed();
            groupedByDistanceBetweenLeds.computeIfAbsent(distance, _ -> new ArrayList<>()).add(strip);
        }

        return groupedByDistanceBetweenLeds;
    }
}

