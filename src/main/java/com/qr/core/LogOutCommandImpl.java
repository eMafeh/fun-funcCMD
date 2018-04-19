package com.qr.core;

import javax.annotation.Resource;
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

    @Override
    public String getNameSpace() {
        return "log";
    }

    @Override
    public boolean useCommand(String order) {
        String[] strings = maxSplitWords.get(0).apply(order, 3);
        final String order1 = strings[0];
        final String order2 = strings[1];
        if (order1 == null && order2 == null) {
            print(() -> "root log level is " + getRootLevel.get());
            return true;
        }
        CmdOutCommand cmdOutCommand = NAMESPACE.get(order1);
        if (order2 == null) {
            if (cmdOutCommand != null) {
                print(() -> cmdOutCommand.getNameSpace() + " : log level is " + cmdOutCommand.getLogLevel());
            }
            setRootLevel.accept(order1);
            print(() -> "root log level change to " + getRootLevel.get());
            return true;
        }

        if (cmdOutCommand != null) {
            cmdOutCommand.setLogLevel(order2);
            print(() -> cmdOutCommand.getNameSpace() + " : log level change to " + cmdOutCommand.getLogLevel());
        }
        return true;
    }
}
