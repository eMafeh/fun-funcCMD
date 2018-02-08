package com.qr.order;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 2017/9/12  10:24
 *
 * @author qianrui
 */
public interface CmdOutOrder {
    String getNameSpace();

    boolean useOrder(String order) throws Throwable;

    boolean isStart();

    void setLogger(Consumer<Supplier<String>> logger);

    void install(Function<String, String> getLine) throws Throwable;

    void shutDown();
}
