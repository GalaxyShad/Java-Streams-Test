package ledstrips.grouper;

import ledstrips.domain.LedStrip;

import java.util.List;
import java.util.Map;

public interface ILedStripGrouper {
    Map<Integer, List<LedStrip>> group(LedStrip[] ledStripList);
}
