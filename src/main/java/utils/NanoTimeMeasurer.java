package utils;

import java.util.function.Supplier;

public class NanoTimeMeasurer {
    public static <T> long executeAndGetTime(Supplier<T> task) {
        var start = System.nanoTime();

        task.get();

        return System.nanoTime() - start;
    }
}
