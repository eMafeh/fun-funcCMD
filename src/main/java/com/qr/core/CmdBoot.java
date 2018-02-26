package com.qr.core;

import com.qr.log.LogLevel;
import com.qr.order.CmdOutOrder;
import com.qr.order.FileOutOrderImpl;
import com.qr.order.MouseOutOrderImpl;
import com.qr.order.XqyOutOrderImpl;
import util.AllThreadUtil;
import util.StringSplitUtil;
import util.StringValueUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
        //load system order
        addOutOrder(ExitOutOrderImpl.INSTANCE, RunOutOrderImpl.INSTANCE, LogOutOrderImpl.INSTANCE, HelpOutOrderImpl.INSTANCE);
        NAMESPACE.forEach((a, b) -> b.setLogLevel(LogLevel.ERROR.name()));
        //load server order
        addOutOrder(XqyOutOrderImpl.INSTANCE, FileOutOrderImpl.INSTANCE, MouseOutOrderImpl.INSTANCE);
    }

    static void addOutOrder(CmdOutOrder... cmdOutOrders) {
        for (CmdOutOrder outOrder : cmdOutOrders) {
            addOutOrder(outOrder);
        }
    }

    static String getDescription() {
        StringBuilder result = new StringBuilder("QianRui Cmd\nis a test fun work and it support some order\n");
        result.append("\ninput '").append(HelpOutOrderImpl.INSTANCE.getNameSpace()).append(" [order]'\nto show how to use this order\n");
        result.append("\nnow have order list is : \n");
        NAMESPACE.forEach((a, b) -> result.append(StringValueUtil.addSpacingToLength(a, 20)).append("by(").append(b.getClass()).append(")\n"));
        result.append("\nif you have some awesome ideas or codes or you just want to join this work\n");
        result.append("please contact me!!!\n>>>\temail 1135901259@qq.com\n>>>\tphoneNumber 18715600499\n");
        return result.toString();
    }

    static void addOutOrder(CmdOutOrder outOrder) {
        final String nameSpace = outOrder.getNameSpace();
        final CmdOutOrder cmdOutOrder = NAMESPACE.get(nameSpace);
        if (cmdOutOrder != null) {
            throw new RuntimeException(cmdOutOrder.getClass() + " | " + outOrder.getClass() + " have same namespace : " + nameSpace);
        }
        NAMESPACE.put(nameSpace, outOrder);
    }


    public static void main(String[] args) {
//        final Package[] packages = Package.getPackages();
//        System.out.println(packages.length);
//        Arrays.stream(packages).filter(a -> !a.getName().startsWith("java") && !a.getName().startsWith("sun")).forEach(System.out::println);
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
                System.out.println(e.getMessage());
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
        NAMESPACE.forEach((a, b) -> {
            if (b != null && !(b instanceof SystemCmdOutOrder)) {
                try {
                    b.shutDown();
                } catch (Throwable exit) {
                    exit.printStackTrace();
                }
            }
        });
        AllThreadUtil.exit();
        System.out.println("system is exited");
//        final Package[] packages2 = Package.getPackages();
//        System.out.println(packages2.length);
//        Arrays.stream(packages2).filter(a -> !a.getName().startsWith("java") && !a.getName().startsWith("sun")).forEach(System.out::println);
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
        System.out.println(e);
    }

}
