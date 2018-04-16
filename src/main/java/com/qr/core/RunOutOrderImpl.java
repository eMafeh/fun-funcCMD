package com.qr.core;

import javax.annotation.Resource;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum RunOutOrderImpl implements SystemCmdOutOrder {
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
    public boolean useOrder(String order) throws Throwable {
        String target = nextWord.apply(order, -1);
        CmdOutOrder cmdOutOrder = CmdBoot.NAMESPACE.get(target);
        if (cmdOutOrder != null) {
            if (cmdOutOrder.isStart()) {
                throw new RuntimeException(cmdOutOrder.getNameSpace() + " is started");
            }
            try {
                cmdOutOrder.install(orderLine);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
