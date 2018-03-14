package com.qr.core;

import java.util.function.BiFunction;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum RunOutOrderImpl implements SystemCmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    static BiFunction<String, Integer, String> nextWord;

    @Override
    public String getNameSpace() {
        return "run";
    }

    @Override
    public boolean useOrder(String order) throws Throwable {
        String target = nextWord.apply(order, -1);
        CmdOutOrder cmdOutOrder = CmdBoot.NAMESPACE.get(target);
        if (cmdOutOrder != null) {
            try {
                cmdOutOrder.install(CmdBoot::getString);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
