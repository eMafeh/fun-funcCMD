package com.qr.core;

import com.qr.log.IntelligentLogger;

import java.util.function.Function;

/**
 * 2017/9/12  10:24
 *
 * @author qianrui
 */
public interface CmdOutOrder extends IntelligentLogger {
    String getNameSpace();

    boolean useOrder(String order) throws Throwable;

    boolean isStart();

    void install(Function<String, String> getLine) throws Throwable;

    void shutDown();

    default String getDescription() {
        return "";
    }

}