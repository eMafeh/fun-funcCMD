package util;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * @author qianrui
 * 2018/2/7
 */
public interface PrintlnLogger {
    Consumer<Callable<String>> getLogger();

    default void print(Callable<String> message) {
        try {
            Consumer<Callable<String>> logger = getLogger();
            if (logger != null) {
                logger.accept(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
