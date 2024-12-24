package ledstrips.grouper;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ledstrips.domain.LedStrip;
import utils.Delayer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LedStripGrouperRxJava implements ILedStripGrouper {
    private int delayNanos = 0;
    private boolean isUsingParallel = false;

    public LedStripGrouperRxJava useDelay(int nanos) {
        delayNanos = nanos;
        return this;
    }

    public LedStripGrouperRxJava useParallel() {
        isUsingParallel = true;
        return this;
    }

    @Override
    public Map<Integer, List<LedStrip>> group(LedStrip[] ledStripList) {
        Observable<LedStrip> observable = Observable.fromArray(ledStripList);

        if (isUsingParallel) {
            observable = observable.subscribeOn(Schedulers.io());
        }

        return observable
                .groupBy(LedStrip::getDistanceBetweenLed)
                .flatMapSingle(groupedObservable -> groupedObservable
                        .toList()
                        .map(ledStrips -> Map.entry(Delayer.delayAndExecute(delayNanos, groupedObservable::getKey) , ledStrips))
                )
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        )
                )
                .blockingGet();
    }
}
