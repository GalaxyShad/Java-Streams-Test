package ledstrips.grouper;

import ledstrips.domain.LedStrip;
import ledstrips.LedStripCollector;

import java.util.*;

public class LedStripGrouperOwnCollector implements ILedStripGrouper {

    @Override
    public Map<Integer, List<LedStrip>> group(LedStrip[] ledStripList) {
        return Arrays.stream(ledStripList).collect(new LedStripCollector());
    }
}
