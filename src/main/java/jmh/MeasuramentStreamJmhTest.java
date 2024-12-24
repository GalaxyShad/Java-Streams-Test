package jmh;

import ledstrips.domain.LedStrip;

import ledstrips.domain.LedStripGenerator;
import ledstrips.LedStripListPerformanceTests;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
public class MeasuramentStreamJmhTest {

    private LedStrip[] ledStripList;

//    @Param({"110000", "200000", "300000"})
//    @Param({"500", "2000", "110000"})
    @Param({"500", "2000"})
    private int ledStripCount;

    @Param({"true"})
//    @Param({"true", "false"})
    private boolean useParallel;

//    @Param({"0"})
    @Param({"0", "2"})
    private int delayNanos;

    @Setup
    public void setup() {
        var gen = new LedStripGenerator();
        ledStripList = gen.generate(ledStripCount);
    }

//    @Threads(6)
//    @Benchmark
    public long measureRxJavaFlowable() {
        return LedStripListPerformanceTests.measureRxJavaFlowable(ledStripList, useParallel, delayNanos);
    }

    @Threads(6)
    @Benchmark
    public long measureRxJava() {
        return LedStripListPerformanceTests.measureRxJava(ledStripList, useParallel, delayNanos);
    }

    @Threads(6)
    @Benchmark
    public long measureStream() {
        return LedStripListPerformanceTests.measureStream(ledStripList, useParallel, delayNanos);
    }

//    @Benchmark
    public long measureForkJoin() {
        return LedStripListPerformanceTests.measureForkJoin(ledStripList, 2);
    }

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }
}
