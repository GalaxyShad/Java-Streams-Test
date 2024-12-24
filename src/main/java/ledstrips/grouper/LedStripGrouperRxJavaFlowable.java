package ledstrips.grouper;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ledstrips.domain.LedStrip;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LedStripGrouperRxJavaFlowable implements ILedStripGrouper {
    private int delayNanos = 0;
    private boolean isUsingParallel = false;

    public LedStripGrouperRxJavaFlowable useDelay(int nanos) {
        delayNanos = nanos;
        return this;
    }

    public LedStripGrouperRxJavaFlowable useParallel() {
        isUsingParallel = true;
        return this;
    }

    @Override
    public Map<Integer, List<LedStrip>> group(LedStrip[] ledStripList) {
        Flowable<LedStrip> flowable = Flowable.fromArray(ledStripList);

        if (isUsingParallel) {
            flowable = flowable.subscribeOn(Schedulers.computation());
        }

        LedStripSubscriber subscriber = new LedStripSubscriber(delayNanos);
        flowable.onBackpressureBuffer()
                .blockingSubscribe(subscriber);

        return subscriber.getGroupedMap();
    }

    public static class LedStripSubscriber implements Subscriber<LedStrip> {
        private final int delayNanos;
        private Subscription subscription;
        private Map<Integer, List<LedStrip>> groupedMap = new HashMap<>();

        public LedStripSubscriber(int delayNanos) {
            this.delayNanos = delayNanos;
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.subscription = s;
            s.request(1); // Initial request for one item
        }

        @Override
        public void onNext(LedStrip ledStrip) {
            int distance = ledStrip.getDistanceBetweenLed();
            groupedMap.computeIfAbsent(distance, k -> new ArrayList<>()).add(ledStrip);

            try {
                TimeUnit.NANOSECONDS.sleep(delayNanos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            subscription.request(1); // Request one more item
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {

        }

        public Map<Integer, List<LedStrip>> getGroupedMap() {
            return groupedMap;
        }
    }
}
