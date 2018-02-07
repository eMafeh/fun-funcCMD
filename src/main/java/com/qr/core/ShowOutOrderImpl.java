package com.qr.core;

import com.qr.order.AbstractCmdOutOrder;
import util.StringSplitUtil;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum ShowOutOrderImpl implements AbstractCmdOutOrder {
    INSTANCE;

    @Override
    public String getNameSpace() {
        return "log";
    }

    @Override
    public void useOrder(String order) throws Throwable {
        Boolean noSilent = isSilent(StringSplitUtil.nextWord(order, -1));
        if (noSilent != null) {
            CmdBoot.noSilent = noSilent;
        }
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
