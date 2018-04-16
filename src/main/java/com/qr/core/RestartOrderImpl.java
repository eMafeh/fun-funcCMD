package com.qr.core;

import util.Phoenix;

import javax.annotation.Resource;
import java.util.function.Function;

public enum  RestartOrderImpl implements SystemCmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    @Resource
    private Function<String, Boolean> caseTrueFalse;

    @Override
    public String getNameSpace() {
        return "restart";
    }

    @Override
    public boolean useOrder(String order) throws Throwable {
        Boolean restart = caseTrueFalse.apply(order);
        if (restart) {
            System.err.println("system will restart in new windows!!!");
            Phoenix.robotRun();
            System.exit(0);
        }
        return restart;
    }
}
