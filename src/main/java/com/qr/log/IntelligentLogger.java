package com.qr.log;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.qr.log.IntelligentLogger.LogFunction.*;


/**
 * @author qianrui
 * 2018/2/7
 */
public interface IntelligentLogger {
    class LogFunction {
        static Function<Class<?>, Consumer<Supplier<String>>> logFactory;
        static Function<Class<?>, Boolean> enoughLevel;
        static Function<String, Boolean> stringEnoughLevel;
        static BiConsumer<Class<?>, String> changeLevel;
        static Function<Class<?>, String> logLevel;
    }

    default void print(Supplier<String> message) {
        if (message == null) {
            return;
        }
        Class<? extends IntelligentLogger> thisType = this.getClass();
        final Boolean enough = enoughLevel.apply(thisType);
        if (enough == null || !enough) {
            return;
        }
        Consumer<Supplier<String>> logger = logFactory.apply(thisType);
        if (logger == null) {
            return;
        }
        logger.accept(message);
    }

    default void print(String level, Supplier<String> message) {
        if (level == null || message == null) {
            return;
        }
        final Boolean enough = stringEnoughLevel.apply(level);
        if (enough == null || !enough) {
            return;
        }
        Class<? extends IntelligentLogger> thisType = this.getClass();
        Consumer<Supplier<String>> logger = logFactory.apply(thisType);
        if (logger == null) {
            return;
        }
        logger.accept(message);
    }

    default void setLogLevel(String level) {
        changeLevel.accept(this.getClass(), level);
    }

    default String getLogLevel() {
        return logLevel.apply(this.getClass());
    }
}
