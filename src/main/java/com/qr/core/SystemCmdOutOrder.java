package com.qr.core;

import com.qr.order.CmdOutOrder;

import java.util.function.Function;

/**
 * @author kelaite
 * 2018/2/7
 */
public interface SystemCmdOutOrder extends CmdOutOrder {

    @Override
    default void install(Function<String, String> getLine) throws Throwable {
    }

    @Override
    default void shutDown() {
        print(() -> getNameSpace() + " is system order , can not exit");
    }

    @Override
    default boolean isStart() {
        return true;
    }
}
