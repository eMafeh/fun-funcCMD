package com.qr.core;

import util.StringSplitUtil;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum ShowOutOrderImpl implements AbstractCmdOutOrder {
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
        Boolean noSilent = isSilent(StringSplitUtil.nextWord(order, -1));
        if (noSilent != null) {
            CmdBoot.noSilent = noSilent;
        }
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
