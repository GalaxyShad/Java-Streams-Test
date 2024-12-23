package jmh;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ledstrips.domain.LedStrip;
import ledstrips.domain.LedStripGenerator;
import ledstrips.LedStripListPerformanceTests;
import org.openjdk.jmh.annotations.*;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import utils.NanoTimeMeasurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms4G", "-Xmx4G"})
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 10, time = 1)
public class MeasuramentStreamJmhTest {

    private LedStrip[] ledStripList;

    @Param({"5000", "50000", "250000"})
    private int ledStripCount;

    @Param({"true", "false"})
    private boolean useParallel;

    @Param({"0", "2"})
    private int delayNanos;

    @Setup
    public void setup() {
        var gen = new LedStripGenerator();
        ledStripList = gen.generate(ledStripCount);
    }

    @Benchmark
    public long measureStream() {
        return LedStripListPerformanceTests.measureStream(ledStripList, useParallel, delayNanos);
    }

    @Benchmark
    public long measureForkJoin() {
        return LedStripListPerformanceTests.measureForkJoin(ledStripList, delayNanos);
    }

    @Benchmark
    public long measureRxJavaWithDelay() {
        return NanoTimeMeasurer.executeAndGetTime(() -> {
            Map<Integer, List<LedStrip>> statistics = Observable.fromArray(ledStripList)
                    .subscribeOn(Schedulers.computation())
                    .delay(i -> Observable.timer(delayNanos, TimeUnit.NANOSECONDS))
                    .groupBy(LedStrip::getDistanceBetweenLed)
                    .flatMapSingle(groupedObservable -> groupedObservable.toList())
                    .toMap(list -> list.get(0).getDistanceBetweenLed(), list -> list)
                    .blockingGet();
            return statistics;
        });
    }

    @Benchmark
    public long measureFlowableWithBackpressure() {
        final Map<Integer, List<LedStrip>> result = new HashMap<>();

        Flowable.fromArray(ledStripList)
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
                        // Processing complete
                    }
                });

        return result.size();
    }

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }
}
