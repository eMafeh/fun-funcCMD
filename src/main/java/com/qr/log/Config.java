package com.qr.log;

import com.qr.core.CmdBoot;

import java.util.function.*;

/**
 * @author qianrui
 * 2018/2/13
 */
interface Config {
    /**
     * 核心方法，用于被包间连丝对象调用，产生外部服务
     */
    class Nucleus {
        final static Function<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> MESSAGE_CONSUMER = IntelligentLoggerFactory.getFactory();
        final static Function<Class<? extends IntelligentLogger>, Boolean> CLASS_ENOUGH_LEVEL = IntelligentLoggerLevel.classLevel();
        final static Function<Integer, Boolean> THIS_ENOUGH_LEVEL = IntelligentLoggerLevel.thisLevel();
        final static BiConsumer<Class<? extends IntelligentLogger>, String> CHANGE_LEVEL = IntelligentLoggerLevel.changeLevel();
    }

    /**
     * 服务对象所需要的方法支持
     */
    class Apparatus {
        final static Function<Class<? extends IntelligentLogger>, Consumer<Supplier<String>>> DEFAULT_BUILDER = a -> b -> CmdBoot.cmdPrintln(b.get());
    }

    /**
     * 级别过滤所需要的方法支持
     */
    class Level {
        final static LogLevel DEFAULT_LEVEL = LogLevel.INFO;
        static LogLevel rootLevel = LogLevel.DEBUG;
        static Function<String, LogLevel> toLevel = LogLevel::getLoggerLevel;
    }
}
