package com.qr.order;

import java.util.function.Function;

/**
 * @author kelaite
 * 2018/2/7
 */
public interface AbstractCmdOutOrder extends CmdOutOrder {
    @Override
    default void useOrder(String order) throws Throwable {
    }

    @Override
    default void install(Function<String, String> getLine) throws Throwable {

    }

    @Override
    default void shutDown(){

    }
}
