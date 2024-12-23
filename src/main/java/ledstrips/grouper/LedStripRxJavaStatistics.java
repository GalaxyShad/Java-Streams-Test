import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ledstrips.domain.LedStrip;
import ledstrips.domain.LedStripGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LedStripRxJavaStatistics {

    public static Map<Integer, List<LedStrip>> calculateStatistics(LedStrip[] ledStrips) {
        return Observable.fromArray(ledStrips)
                .subscribeOn(Schedulers.computation())
                .groupBy(LedStrip::getDistanceBetweenLed)
                .flatMapSingle(groupedObservable -> groupedObservable.toList())
                .toMap(list -> list.get(0).getDistanceBetweenLed(), list -> list)
                .blockingGet();
    }

    public static void main(String[] args) {
        LedStripGenerator generator = new LedStripGenerator();
        LedStrip[] ledStrips = generator.generate(500);

        long startTime = System.nanoTime();
        Map<Integer, List<LedStrip>> statistics = calculateStatistics(ledStrips);
        long endTime = System.nanoTime();

        System.out.println("Time taken: " + (endTime - startTime) + " ns");
        System.out.println("Statistics: " + statistics);
    }
}
