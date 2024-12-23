package ledstrips.grouper;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ledstrips.LedStripListPerformanceTests;
import ledstrips.domain.LedStrip;
import ledstrips.domain.LedStripGenerator;
import utils.NanoTimeMeasurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PerformanceComparison {

    public static long measureRxJavaWithDelay(LedStrip[] ledStrips, int nanosDelay) {
        return NanoTimeMeasurer.executeAndGetTime(() -> {
            Map<Integer, List<LedStrip>> statistics = Observable.fromArray(ledStrips)
                    .subscribeOn(Schedulers.computation())
                    .delay(item -> Observable.timer(nanosDelay, TimeUnit.NANOSECONDS))
                    .groupBy(LedStrip::getDistanceBetweenLed)
                    .flatMap(groupedObservable -> groupedObservable.toList().toObservable())
                    .toMap(list -> list.get(0).getDistanceBetweenLed(), list -> list)
                    .blockingGet();
            return statistics;
        });
    }

    public static void main(String[] args) {
        LedStripGenerator generator = new LedStripGenerator();
        int[] counts = {500, 2000};
        int nanosDelay = 2;

        for (int count : counts) {
            LedStrip[] ledStrips = generator.generate(count);

            // Parallel streams with delay
            long parDelayed = LedStripListPerformanceTests.measureStream(ledStrips, true, nanosDelay);
            System.out.println("Parallel Streams with delay (" + count + " elements): " + parDelayed + " ns");

            // RxJava with delay
            long rxJavaDelayed = measureRxJavaWithDelay(ledStrips, nanosDelay);
            System.out.println("RxJava with delay (" + count + " elements): " + rxJavaDelayed + " ns");
        }
    }
}
