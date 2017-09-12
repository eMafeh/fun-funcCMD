package socket.enums;

import socket.core.XiaoQiuYinBoot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by snb on 2017/9/12  10:24
 */
public enum XiaoQiuYinOrder implements CmdControOrder {
    ;
//    exit(XiaoQiuYinBoot);

    private Function<String,String> function;

    XiaoQiuYinOrder(Function<String,String> function) {
        this.function = function;
    }

    public static Map<String, XiaoQiuYinOrder> orderTank = new HashMap<>();

    @Override
    public Function<String,String> getFunction() {
        return null;
    }
}
