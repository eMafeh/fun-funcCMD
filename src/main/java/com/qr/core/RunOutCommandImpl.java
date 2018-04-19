package com.qr.core;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum RunOutCommandImpl implements SystemCmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;


    @Override
    public String getNameSpace() {
        return "run";
    }

    @Override
    public boolean useCommand(String order) {
        String target = nextWord.get(0).apply(order, -1);
        CmdOutCommand cmdOutCommand = NAMESPACE.get(target);
        if (cmdOutCommand != null) {
            if (cmdOutCommand.isStart()) {
                throw new RuntimeException(cmdOutCommand.getNameSpace() + " is started");
            }
            try {
                cmdOutCommand.install(orderLine.get(0));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
