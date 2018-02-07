package com.qr.core;

import com.qr.order.CmdOutOrder;
import com.qr.order.FileOutOrderImpl;
import socket.file.FileOutListener;
import com.qr.order.XqyOutOrderImpl;
import util.AllThreadUtil;
import util.StringSplitUtil;

import java.util.*;
import java.util.function.Function;

/**
 * 2017/9/8  9:50
 *
 * @author qianrui
 */
public class CmdBoot {
    private static final Scanner SC = new Scanner(System.in);

    static final Map<String, CmdOutOrder> NAMESPACE = new HashMap<>();

    static {
        NAMESPACE.put(XqyOutOrderImpl.INSTANCE.getNameSpace(), XqyOutOrderImpl.INSTANCE);
        NAMESPACE.put(RunOutOrderImpl.INSTANCE.getNameSpace(), RunOutOrderImpl.INSTANCE);
        NAMESPACE.put(ShowOutOrderImpl.INSTANCE.getNameSpace(), ShowOutOrderImpl.INSTANCE);
        NAMESPACE.put(FileOutOrderImpl.INSTANCE.getNameSpace(), FileOutOrderImpl.INSTANCE);
    }

    static volatile boolean noSilent = true;

    public static void main(String[] args) {

        String line;
        String[] orders;
        while (true) {
            line = SC.nextLine();
            orders = StringSplitUtil.maxSplitWords(line, 2);
            if (orders[0] == null) {
                continue;
            }
            if ("exit".equals(orders[0])) {
                break;
            }
            CmdOutOrder cmdOutOrder = NAMESPACE.get(orders[0]);
            if (cmdOutOrder == null) {
                System.out.println("-bash: warn: command not found : " + orders[0]);
            } else {
                try {
                    cmdOutOrder.useOrder(orders[1]);
                } catch (Throwable throwable) {
                    cmdOutOrder.shutDown();
                    System.out.println( throwable.getMessage());
                }
            }
        }
        NAMESPACE.forEach((a, b) -> {
            if (b != null) {
                b.shutDown();
            }
        });
        AllThreadUtil.exit();
    }


    /**
     * TODO basic cmd input or output operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */
    public static int getInt(Function<String, String> getString, String title) throws Throwable {
        while (true) {
            try {
                String line = getString.apply(title);
                if ("exit".equals(line.trim())) {
                    throw new Throwable("终止操作");
                }
                return Integer.parseInt(line);
            } catch (Exception e) {
                //
            }
        }
    }

    public static String getString(String title) {
        System.out.println(title);
        return SC.nextLine();
    }

    public static <E> void cmdPrintln(E e) {
        if (noSilent) {
            System.out.println(e);
        }
    }

    public static boolean isNoSilent() {
        return noSilent;
    }
}
