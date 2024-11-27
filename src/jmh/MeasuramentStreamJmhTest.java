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
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 10, time = 1)
public class MeasuramentStreamJmhTest {

    private LedStrip[] ledStripList;

    @Param({"5000", "50000", "250000"})
    private int ledStripCount;

//    @Param({"true", "false"})
    private boolean useParallel;

//    @Param({"0", "2"})
    private int delayNanos;

    @Setup
    public void setup() {
        var gen = new LedStripGenerator();
        ledStripList = gen.generate(ledStripCount);
    }

//    @Benchmark
    public long measureStream() {
        return LedStripListPerformanceTests.measureStream(ledStripList, useParallel, delayNanos);
    }

    @Benchmark
    public long measureForkJoin() {
        return LedStripListPerformanceTests.measureForkJoin(ledStripList, 2);
    }

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }
}
