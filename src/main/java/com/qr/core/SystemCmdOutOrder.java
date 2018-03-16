package com.qr.core;

import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public interface SystemCmdOutOrder extends CmdOutOrder {

    @Override
    default void install(Supplier<String> getLine) throws Throwable {
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
