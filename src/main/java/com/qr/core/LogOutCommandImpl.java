package com.qr.core;

import javax.annotation.Resource;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum LogOutCommandImpl implements SystemCmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    @Resource
    private Consumer<String> setRootLevel;
    @Resource
    private Supplier<String> getRootLevel;
    @Resource
    private BiFunction<String, Integer, String[]> maxSplitWords;

    @Override
    public String getNameSpace() {
        return "log";
    }

    @Override
    public boolean useCommand(String order) throws Throwable {
        String[] strings = maxSplitWords.apply(order, 3);
        final String order1 = strings[0];
        final String order2 = strings[1];
        if (order1 == null || "".equals(order1)) {
            print(() -> "root log level is " + getRootLevel.get());
            return true;
        }
        CmdOutCommand cmdOutCommand = CmdBoot.NAMESPACE.get(order1);
        if (cmdOutCommand == null) {
            if (order2 == null) {
                setRootLevel.accept(order1);
                print(() -> "root log level change to " + getRootLevel.get());
                return true;
            }
            return false;
        }
        if (order2 == null) {
            print(() -> cmdOutCommand.getNameSpace() + " : log level is " + cmdOutCommand.getLogLevel());
            setRootLevel.accept(order1);
            print(() -> "root log level change to " + getRootLevel.get());
            return true;
        }
        cmdOutCommand.setLogLevel(order2);
        print(() -> cmdOutCommand.getNameSpace() + " : log level change to " + cmdOutCommand.getLogLevel());
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
