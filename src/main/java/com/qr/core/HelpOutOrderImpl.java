package com.qr.core;

import util.StringValueUtil;

import java.util.function.BiFunction;

/**
 * @author kelaite
 * 2018/2/25
 */
public enum HelpOutOrderImpl implements SystemCmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    static BiFunction<String, Integer, String> nextWord;

    @Override
    public String getNameSpace() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "\n" + StringValueUtil.addSpacingToLength("'" + getNameSpace() + "'", 30) + "introduce this app basic information\n" + StringValueUtil.addSpacingToLength("'" + getNameSpace() + " [order]" + "'", 30) + "show [order] description( [order] must exist in the order list )\n";
    }

    @Override
    public boolean useOrder(String order) throws Throwable {
        if (order == null || "".equals(order)) {
            print(CmdBoot::getDescription);
            return true;
        }
        String target = nextWord.apply(order, -1);
        CmdOutOrder cmdOutOrder = CmdBoot.NAMESPACE.get(target);
        if (cmdOutOrder != null) {
            print(cmdOutOrder::getDescription);
            return true;
        }
        return false;
    }


}
