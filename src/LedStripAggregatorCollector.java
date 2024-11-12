import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class LedStripAggregatorCollector implements Collector<LedStrip, LedStripAggregator, Double> {

    @Override
    public Supplier<LedStripAggregator> supplier() {
        return LedStripAggregator::new;
    }

    @Override
    public BiConsumer<LedStripAggregator, LedStrip> accumulator() {
        return LedStripAggregator::accumulate;
    }

    @Override
    public BinaryOperator<LedStripAggregator> combiner() {
        return LedStripAggregator::combine;
    }

    @Override
    public Function<LedStripAggregator, Double> finisher() {
        return LedStripAggregator::getMaxColorTemperature;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));
    }
}
