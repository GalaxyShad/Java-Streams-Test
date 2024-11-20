package jmh;

import ledstrips.LedStrip;

import ledstrips.LedStripGenerator;
import ledstrips.Measuraments;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class MeasuramentStreamJmhTest {

    private LedStrip[] ledStripList;

    @Param({"5000", "50000", "250000"})
    private int ledStripCount;

    @Param({"true", "false"})
    private boolean useParallel;

    @Param({"2"})
    private int delayNanos;

    @Setup
    public void setup() {
        var gen = new LedStripGenerator();
        ledStripList = gen.generate(ledStripCount);
    }

    @Benchmark
    public long measureStream() {
        return Measuraments.measureStream(ledStripList, useParallel, delayNanos);
    }

    public static void main(String[] args) throws IOException {
        Main.main(args);
    }
}
