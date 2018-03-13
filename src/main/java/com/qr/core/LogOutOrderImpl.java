package com.qr.core;

import com.qr.log.LogLevel;
import util.StringSplitUtil;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum LogOutOrderImpl implements SystemCmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;

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
            print(() -> "root log level is " + LogLevel.getRootLevel().name());
            return true;
        }
        CmdOutOrder cmdOutOrder = CmdBoot.NAMESPACE.get(order1);
        if (cmdOutOrder == null) {
            if (order2 == null) {
                LogLevel.setRootLevel(LogLevel.getLoggerLevel(order1));
                print(() -> "root log level change to " + LogLevel.getRootLevel().name());
                return true;
            }
            return false;
        }
        if (order2 == null) {
            print(() -> cmdOutOrder.getNameSpace() + " : log level is " + cmdOutOrder.getLogLevel().name());
            LogLevel.setRootLevel(LogLevel.getLoggerLevel(order1));
            print(() -> "root log level change to " + LogLevel.getRootLevel().name());
            return true;
        }
        cmdOutOrder.setLogLevel(order2);
        print(() -> cmdOutOrder.getNameSpace() + " : log level change to " + cmdOutOrder.getLogLevel().name());
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
