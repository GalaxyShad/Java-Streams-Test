import java.util.function.Supplier;

public class Delayer {

    /**
     * Метод для выполнения заданного метода с задержкой.
     *
     * @param millis количество миллисекунд для задержки
     * @param task метод, который нужно выполнить после задержки
     * @param <T> тип возвращаемого значения метода
     * @return результат выполнения метода
     */
    public static <T> T delayAndExecute(long millis, Supplier<T> task) {
        try {
            Thread.sleep(millis);
            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Восстанавливаем прерванный статус потока
            e.printStackTrace();
            return null; // Возвращаем null в случае исключения
        }
    }
}