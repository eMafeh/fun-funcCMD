package com.qr.core;

import javax.annotation.Resource;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum RunOutCommandImpl implements SystemCmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    @Resource
    private BiFunction<String, Integer, String> nextWord;
    @Resource
    private Supplier<String> orderLine;

    @Override
    public String getNameSpace() {
        return "run";
    }

    @Override
    public boolean useCommand(String order) throws Throwable {
        String target = nextWord.apply(order, -1);
        CmdOutCommand cmdOutCommand = CmdBoot.NAMESPACE.get(target);
        if (cmdOutCommand != null) {
            if (cmdOutCommand.isStart()) {
                throw new RuntimeException(cmdOutCommand.getNameSpace() + " is started");
            }
            try {
                cmdOutCommand.install(orderLine);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
