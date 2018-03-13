package com.qr.log;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * @author qianrui
 * 2018/2/9
 */
class IntelligentLoggerFactory {
    private final static Map<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> LOGGER_FACTORY = new ConcurrentSkipListMap<>(Comparator.comparing(Class::getName));
    static Consumer<Supplier<String>> DEFAULT_BUILDER;

    static Consumer<Supplier<String>> getFactory(Class<? extends IntelligentLogger> aClass) {
        return LOGGER_FACTORY.computeIfAbsent(aClass, a -> DEFAULT_BUILDER);
    }

}
