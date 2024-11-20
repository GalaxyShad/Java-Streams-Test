package utils;

import java.util.function.Supplier;

public class Delayer {
    public static <T> T delayAndExecute(int nanos, Supplier<T> task, T defaultValueWhenError) {
        try {
            Thread.sleep(0, nanos);

            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Восстанавливаем прерванный статус потока
            e.printStackTrace();

            return defaultValueWhenError; // Возвращаем дефолтное значение в случае исключения
        }
    }

    public static <T> T delayAndExecute(int millis, Supplier<T> task) {
        return delayAndExecute(millis, task, null);
    }
}