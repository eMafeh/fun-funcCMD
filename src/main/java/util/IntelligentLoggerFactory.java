package util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author qianrui
 * 2018/2/9
 */
public class IntelligentLoggerFactory {
    public static Function<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> getFactory() {
        return a -> b -> System.out.println(b.get());
    }
}
