package com.qr.order;

import java.util.function.Function;

/**
 * 2017/9/12  10:24
 *
 * @author qianrui
 */
public interface CmdOutOrder {
    String getNameSpace();

    boolean useOrder(String order) throws Throwable;

    boolean isStart();

    void install(Function<String, String> getLine) throws Throwable;

    void shutDown();
}
