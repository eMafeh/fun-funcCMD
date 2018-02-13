package com.qr.log;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author qianrui
 * 2018/2/9
 */
class IntelligentLoggerLevel extends Config.Level {
    /**
     * 记录一个类的日志级别
     * 初始值默认为0
     */
    private static final Map<Class<? extends IntelligentLogger>, Integer> LEVEL_CACHE = new ConcurrentSkipListMap<>(Comparator.comparing(Class::getName));

    /**
     * 提供一个函数方法，该方法描述指定类的级别是否足够
     */
    static Function<Class<? extends IntelligentLogger>, Boolean> classLevel() {
        return (Function<Class<? extends IntelligentLogger>, Boolean> & Serializable) a -> LEVEL_CACHE.computeIfAbsent(a, b -> DEFAULT_LEVEL.getLevel()) >= rootLevel.getLevel();
    }

    /**
     * 提供一个函数方法，该方法描述指定调用的级别是否足够
     */
    static Function<Integer, Boolean> thisLevel() {
        return (Function<Integer, Boolean> & Serializable) a -> a >= rootLevel.getLevel();
    }

    /**
     * 提供一个消费方法，该方法设定指定类的级别值
     */
    static BiConsumer<Class<? extends IntelligentLogger>, String> changeLevel() {
        return (BiConsumer<Class<? extends IntelligentLogger>, String> & Serializable) (a, b) -> LEVEL_CACHE.put(a, toLevel.apply(b).getLevel());
    }

}
