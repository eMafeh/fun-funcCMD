package com.qr.core;

import com.qr.order.AbstractCmdOutOrder;
import com.qr.order.CmdOutOrder;
import util.StringSplitUtil;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum RunOutOrderImpl implements AbstractCmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;

    @Override
    public String getNameSpace() {
        return "run";
    }

    @Override
    public void useOrder(String order) throws Throwable {
        if (order == null) {
            return;
        }
        String target = StringSplitUtil.nextWord(order, -1);
        CmdOutOrder cmdOutOrder = CmdBoot.NAMESPACE.get(target);
        if (cmdOutOrder != null) {
            try {
                cmdOutOrder.install(CmdBoot::getString);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            System.out.println("-bash: warn: server does not exist : " + order);
        }
    }

}
