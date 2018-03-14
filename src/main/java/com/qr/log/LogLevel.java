package com.qr.log;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author qianrui
 */

enum LogLevel {
    /**
     * 调试
     */
    DEBUG(1), /**
     * 正常信息
     */
    INFO(2), /**
     * 警告信息
     */
    WARN(3), /**
     * 异常信息
     */
    ERROR(4);

    private int level;

    LogLevel(int level) {
        this.level = level;
    }

    int getLevel() {
        return level;
    }

    /**
     * 记录一个类的日志级别
     * 初始值默认为0
     */
    private static final Map<Class<?>, LogLevel> LEVEL_CACHE = new ConcurrentSkipListMap<>(Comparator.comparing(Class::getName));

    private final static LogLevel DEFAULT_LEVEL = LogLevel.INFO;
    private static LogLevel rootLevel = LogLevel.INFO;

    /**
     * 设置全局的最低级别
     */
    static void setRootLevel(String rootLevel) {
        LogLevel.rootLevel = getLoggerLevel(rootLevel);
    }

    static String getRootLevel() {
        return rootLevel.name();
    }

    /**
     * 提供一个函数方法，该方法描述指定类的级别是否足够
     */
    static Boolean enoughLevel(Class<?> aClass) {
        return LEVEL_CACHE.computeIfAbsent(aClass, b -> DEFAULT_LEVEL).getLevel() >= rootLevel.getLevel();
    }

    static String logLevel(Class<?> aClass) {
        return LEVEL_CACHE.computeIfAbsent(aClass, b -> DEFAULT_LEVEL).name();
    }

    /**
     * 提供一个函数方法，该方法描述指定调用的级别是否足够
     */
    static boolean stringEnoughLevel(String level) {
        return getLoggerLevel(level).getLevel() >= rootLevel.getLevel();
    }

    /**
     * 提供一个消费方法，该方法设定指定类的级别值
     */
    static void changeLevel(Class<?> aClass, String level) {
        LEVEL_CACHE.put(aClass, getLoggerLevel(level));
    }

    static LogLevel getLoggerLevel(String string) {
        if (string != null && !"".equals(string)) {
            try {
                return LogLevel.valueOf(string.toUpperCase());
            } catch (Exception e) {
                //去下面抛异常
            }
        }
        throw new NullPointerException(errorMessage());

    }

    private static String errorMessage() {
        StringBuilder result = new StringBuilder("support log level : ");
        Arrays.stream(LogLevel.values()).forEach(a -> result.append(a.name()).append(" "));
        return result.toString();
    }
}