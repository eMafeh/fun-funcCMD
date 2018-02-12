package util;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author qianrui
 * 2018/2/9
 */
public class IntelligentLoggerFactory {
    private static final Map<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> LOGGER_FACTORY = new ConcurrentSkipListMap<>(Comparator.comparing(Class::getName));
    private static Function<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> DEFAULT_BUILDER;

    public static Function<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> getFactory() {

        return a -> LOGGER_FACTORY.computeIfAbsent(a, DEFAULT_BUILDER);
    }
}
