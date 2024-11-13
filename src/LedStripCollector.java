import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class LedStripCollector implements Collector<LedStrip, Map<Integer, List<LedStrip>>, Map<Integer, List<LedStrip>>> {

    @Override
    public Supplier<Map<Integer, List<LedStrip>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Integer, List<LedStrip>>, LedStrip> accumulator() {
        return (map, strip) -> map.computeIfAbsent(strip.getDistanceBetweenLed(), k -> new ArrayList<>()).add(strip);
    }

    @Override
    public BinaryOperator<Map<Integer, List<LedStrip>>> combiner() {
        return (map1, map2) -> {
            map2.forEach((key, value) -> map1.merge(key, value, (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            }));
            return map1;
        };
    }

    @Override
    public Function<Map<Integer, List<LedStrip>>, Map<Integer, List<LedStrip>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.IDENTITY_FINISH);
    }
}
