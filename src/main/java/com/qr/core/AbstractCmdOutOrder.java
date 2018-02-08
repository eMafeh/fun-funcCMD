package com.qr.core;

import com.qr.order.CmdOutOrder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public interface AbstractCmdOutOrder extends CmdOutOrder {

    @Override
    default void install(Function<String, String> getLine) throws Throwable {
    }

    @Override
    default void shutDown() {
    }

    @Override
    default void setLogger(Consumer<Supplier<String>> logger) {
    }

    @Override
    default boolean isStart() {
        return true;
    }
}
