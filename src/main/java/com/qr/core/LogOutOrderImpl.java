package com.qr.core;

import util.StringSplitUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum LogOutOrderImpl implements SystemCmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    private static Consumer<String> setRootLevel;
    private static Supplier<String> getRootLevel;

    @Override
    public String getNameSpace() {
        return "log";
    }

    @Override
    public boolean useOrder(String order) throws Throwable {
        String[] strings = StringSplitUtil.maxSplitWords(order, 3);
        final String order1 = strings[0];
        final String order2 = strings[1];
        if (order1 == null || "".equals(order1)) {
            print(() -> "root log level is " + getRootLevel.get());
            return true;
        }
        CmdOutOrder cmdOutOrder = CmdBoot.NAMESPACE.get(order1);
        if (cmdOutOrder == null) {
            if (order2 == null) {
                setRootLevel.accept(order1);
                print(() -> "root log level change to " + getRootLevel.get());
                return true;
            }
            return false;
        }
        if (order2 == null) {
            print(() -> cmdOutOrder.getNameSpace() + " : log level is " + cmdOutOrder.getLogLevel());
            setRootLevel.accept(order1);
            print(() -> "root log level change to " + getRootLevel.get());
            return true;
        }
        cmdOutOrder.setLogLevel(order2);
        print(() -> cmdOutOrder.getNameSpace() + " : log level change to " + cmdOutOrder.getLogLevel());
        return true;
    }

    private Boolean isSilent(String order) {
        switch (order) {
            case "silent":
            case "false":
            case "off":
            case "0":
                return false;
            case "show":
            case "true":
            case "on":
            case "1":
                return true;
            default:
                return null;
        }
    }
}
