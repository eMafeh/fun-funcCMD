package com.qr.core;

import util.StringValueUtil;

/**
 * @author kelaite
 * 2018/2/25
 */
public enum HelpOutCommandImpl implements SystemCmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;

    @Override
    public String getNameSpace() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "\n" + addSpacingToLength.get(0).apply("'" + getNameSpace() + "'", 30) + "introduce this app basic information\n" + StringValueUtil.addSpacingToLength("'" + getNameSpace() + " [order]" + "'", 30) + "show [order] description( [order] must exist in the order list )\n";
    }

    @Override
    public boolean useCommand(String order) {
        String target = nextWord.get(0).apply(order, -1);
        CmdOutCommand cmdOutCommand = NAMESPACE.get(target);
        if (cmdOutCommand != null) {
            print(cmdOutCommand::getDescription);
            return true;
        }
        return false;
    }

}
