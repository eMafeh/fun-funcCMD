package com.qr.core;

import com.qr.log.IntelligentLogger;

import java.util.function.Supplier;

/**
 * 2017/9/12  10:24
 *
 * @author qianrui
 */
public interface CmdOutCommand extends IntelligentLogger {
    String getNameSpace();

    boolean useCommand(String order) throws Throwable;

    boolean isStart();

    void install(Supplier<String> getLine) throws Throwable;

    void shutDown();

    default String getDescription() {
        return "";
    }

}
