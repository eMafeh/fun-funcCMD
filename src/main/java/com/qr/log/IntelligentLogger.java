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
        static Function<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> MESSAGE_CONSUMER;
        static Function<Class<? extends IntelligentLogger>, Boolean> CLASS_ENOUGH_LEVEL;
        static Function<LogLevel, Boolean> THIS_ENOUGH_LEVEL;
        static BiConsumer<Class<? extends IntelligentLogger>, String> CHANGE_LEVEL;
        static Function<Class<? extends IntelligentLogger>, LogLevel> CLASS_LOG_LEVEL;
    }

    default void print(Supplier<String> message) {
        if (message == null) {
            return;
        }
        Class<? extends IntelligentLogger> thisType = this.getClass();
        final Boolean enough = CLASS_ENOUGH_LEVEL.apply(thisType);
        if (enough == null || !enough) {
            return;
        }
        Consumer<Supplier<String>> logger = MESSAGE_CONSUMER.apply(thisType);
        if (logger == null) {
            return;
        }
        logger.accept(message);
    }

    default void print(LogLevel level, Supplier<String> message) {
        if (level == null || message == null) {
            return;
        }
        final Boolean enough = THIS_ENOUGH_LEVEL.apply(level);
        if (enough == null || !enough) {
            return;
        }
        Class<? extends IntelligentLogger> thisType = this.getClass();
        Consumer<Supplier<String>> logger = MESSAGE_CONSUMER.apply(thisType);
        if (logger == null) {
            return;
        }
        logger.accept(message);
    }

    default void setLogLevel(String level) {
        CHANGE_LEVEL.accept(this.getClass(), level);
    }

    default LogLevel getLogLevel() {
        return CLASS_LOG_LEVEL.apply(this.getClass());
    }
}
