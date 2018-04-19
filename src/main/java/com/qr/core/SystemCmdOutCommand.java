package com.qr.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */

public interface SystemCmdOutCommand extends CmdOutCommand {
    default <T> void cmdPrintln(T t) {
        System.out.println(t);
    }

    Map<String, CmdOutCommand> NAMESPACE = new HashMap<>();

    @Override
    default void install(Supplier<String> getLine) {
    }

    @Override
    default boolean useCommand(String order) {
        return true;
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
