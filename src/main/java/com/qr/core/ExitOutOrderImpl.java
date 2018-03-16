package com.qr.core;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum ExitOutOrderImpl implements SystemCmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    static Supplier<String> orderLine;

    private static BiFunction<String, Integer, String> nextWord;
    private static final String TITLE = "yes/no";
    private static final String YES = "yes";
    static final RuntimeException EXIT_EXCEPTION = new RuntimeException("system exiting");

    @Override
    public String getNameSpace() {
        return "exit";
    }

    @Override
    public boolean useOrder(String order) throws Throwable {
        if ("".equals(order)) {
            throw new Throwable("system try exit");
        }
        String target = nextWord.apply(order, -1);
        CmdOutOrder cmdOutOrder = CmdBoot.NAMESPACE.get(target);
        if (cmdOutOrder == INSTANCE) {
            SystemCmdOutOrder.super.shutDown();
            return true;
        }
        if (cmdOutOrder != null) {
            cmdOutOrder.shutDown();
            return true;
        }
        return false;
    }

    @Override
    public void shutDown() {
        print("error", () -> TITLE);
        if (YES.equals(orderLine.get().trim())) {
            throw EXIT_EXCEPTION;
        } else {
            print(() -> "system exit is cancel");
        }
    }
}
