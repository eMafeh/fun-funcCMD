package com.qr.core;

import javax.annotation.Resource;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum ExitOutCommandImpl implements SystemCmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    @Resource
    private Supplier<String> orderLine;
    @Resource
    private BiFunction<String, Integer, String> nextWord;
    private static final String TITLE = "yes/no";
    private static final String YES = "yes";
    static final RuntimeException EXIT_EXCEPTION = new RuntimeException("system exiting");

    @Override
    public String getNameSpace() {
        return "exit";
    }

    @Override
    public boolean useCommand(String order) throws Throwable {
        if ("".equals(order)) {
            throw new Throwable("system try exit");
        }
        String target = nextWord.apply(order, -1);
        CmdOutCommand cmdOutCommand = CmdBoot.NAMESPACE.get(target);
        if (cmdOutCommand == INSTANCE) {
            SystemCmdOutCommand.super.shutDown();
            return true;
        }
        if (cmdOutCommand != null) {
            if (!cmdOutCommand.isStart()) {
                throw new RuntimeException(cmdOutCommand.getNameSpace() + " is not start");
            }
            cmdOutCommand.shutDown();
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
