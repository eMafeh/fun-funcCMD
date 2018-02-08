package util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author qianrui
 * 2018/2/7
 */
public interface PrintlnLogger {
    Consumer<Supplier<String>> getLogger();

    void setLogger(Consumer<Supplier<String>> logger);

    default void print(Supplier<String> message) {
        Consumer<Supplier<String>> logger;
        if ((logger = getLogger()) != null) {
            logger.accept(message);
        }
    }
}
