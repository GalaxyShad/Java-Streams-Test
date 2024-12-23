import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ledstrips.domain.LedStrip;
import ledstrips.domain.LedStripGenerator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LedStripFlowableStatistics {

    public static Map<Integer, List<LedStrip>> calculateStatistics(LedStrip[] ledStrips) {
        final Map<Integer, List<LedStrip>> result = new HashMap<>();

        Flowable.fromArray(ledStrips)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Subscriber<LedStrip>() {
                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.subscription = s;
                        s.request(1); // Request one item initially
                    }

                    @Override
                    public void onNext(LedStrip ledStrip) {
                        result.computeIfAbsent(ledStrip.getDistanceBetweenLed(), k -> new ArrayList<>()).add(ledStrip);
                        subscription.request(1); // Request next item
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Processing complete");
                    }
                });

        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        LedStripGenerator generator = new LedStripGenerator();
        LedStrip[] ledStrips = generator.generate(100000);

        long startTime = System.nanoTime();
        Map<Integer, List<LedStrip>> statistics = calculateStatistics(ledStrips);
        long endTime = System.nanoTime();

        System.out.println("Time taken: " + (endTime - startTime) + " ns");
        System.out.println("Statistics: " + statistics);

        // Wait for the Flowable to complete
        TimeUnit.SECONDS.sleep(5);
    }
}
