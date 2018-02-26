package com.qr.log;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.qr.log.Config.Nucleus.*;

/**
 * @author qianrui
 * 2018/2/7
 */
public interface IntelligentLogger {
    /**
     * 设置全局的最低级别
     */
    static void setRootLevel(LogLevel rootLevel) {
        if (rootLevel != null) {
            Config.Level.rootLevel = rootLevel;
        }
    }

    static LogLevel getRootLevel() {
        return Config.Level.rootLevel;
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
        final Boolean enough = THIS_ENOUGH_LEVEL.apply(level.getLevel());
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
