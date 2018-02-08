package com.qr.core;

import com.qr.order.CmdOutOrder;
import com.qr.order.FileOutOrderImpl;
import com.qr.order.MouseOutOrderImpl;
import com.qr.order.XqyOutOrderImpl;
import util.AllThreadUtil;
import util.StringSplitUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 2017/9/8  9:50
 *
 * @author qianrui
 */
public class CmdBoot {
    private static final Scanner SC = new Scanner(System.in);
    public static final Consumer<Callable<String>> LOGGER = a -> {
        try {
            cmdPrintln(a.call());
        } catch (Exception e) {
            cmdPrintln(e);
        }
    };
    static final Map<String, CmdOutOrder> NAMESPACE = new HashMap<>();

    static {
        NAMESPACE.put(ExitOutOrderImpl.INSTANCE.getNameSpace(), ExitOutOrderImpl.INSTANCE);
        NAMESPACE.put(RunOutOrderImpl.INSTANCE.getNameSpace(), RunOutOrderImpl.INSTANCE);
        NAMESPACE.put(ShowOutOrderImpl.INSTANCE.getNameSpace(), ShowOutOrderImpl.INSTANCE);
        NAMESPACE.put(XqyOutOrderImpl.INSTANCE.getNameSpace(), XqyOutOrderImpl.INSTANCE);
        NAMESPACE.put(FileOutOrderImpl.INSTANCE.getNameSpace(), FileOutOrderImpl.INSTANCE);
        NAMESPACE.put(MouseOutOrderImpl.INSTANCE.getNameSpace(), MouseOutOrderImpl.INSTANCE);
    }

    static volatile boolean noSilent = true;

    public static void main(String[] args) {

        String line;
        String[] orders;
        CmdOutOrder cmdOutOrder;
        while (true) {
            line = SC.nextLine();
            orders = StringSplitUtil.maxSplitWords(line, 2);
            if (orders[0] == null) {
                continue;
            }

            cmdOutOrder = NAMESPACE.get(orders[0]);
            if (cmdOutOrder == null) {
                System.out.println("-bash: warn: command not found : " + orders[0]);
                continue;
            }
            orders[1] = orders[1] == null ? "" : orders[1];

            try {
                boolean isStart = cmdOutOrder.isStart();
                if (isStart) {
                    boolean success = cmdOutOrder.useOrder(orders[1]);
                    if (!success) {
                        System.out.println("-" + cmdOutOrder.getNameSpace() + ": warn: command not found : " + orders[1]);
                    }

                } else {
                    System.out.println("-" + cmdOutOrder.getNameSpace() + " is not install");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                System.out.println(throwable.getMessage());
                try {
                    cmdOutOrder.shutDown();
                } catch (Throwable exit) {
                    if (exit.equals(ExitOutOrderImpl.EXIT_EXCEPTION)) {
                        System.out.println(exit.getMessage());
                        break;
                    } else {
                        exit.printStackTrace();
                    }
                }
            }
        }
        CmdOutOrder notAgain = cmdOutOrder;
        NAMESPACE.forEach((a, b) -> {
            if (b != null && !b.equals(notAgain)) {
                try {
                    b.shutDown();
                } catch (Throwable exit) {
                    exit.printStackTrace();
                }
            }
        });
        AllThreadUtil.exit();
        System.out.println("system is exited");
        System.exit(0);
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
