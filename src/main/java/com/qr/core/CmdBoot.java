package com.qr.core;

import com.qr.function.FunctionWorkshop;
import com.qr.injection.FieldInjection;
import util.AllThreadUtil;
import util.FindClassUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * 2017/9/8  9:50
 *
 * @author qianrui
 */
public class CmdBoot {
    /**
     * 需要第一个注入，避免空指针
     */
    @Resource
    private static BiFunction<String, Integer, String> addSpacingToLength;
    @Resource
    private static BiFunction<String, Integer, String[]> maxSplitWords;
    private static final Scanner SC = new Scanner(System.in);
    final static Map<String, CmdOutOrder> NAMESPACE = new HashMap<>();
    private static final long START_TIME = System.currentTimeMillis();


    static {
        //加载系统类
        final Class<?>[] systemClasses = FindClassUtils.SINGLETON.getSystemClasses();
        //加载用户类
        final Set<Class<?>> classes = FindClassUtils.SINGLETON.getClasses();

        final long begin = System.currentTimeMillis();
        System.out.println("\ntry find functions in all classes");
        //系统类函数采集
        FunctionWorkshop.addFunction(systemClasses);
        //用户类函数采集
        FunctionWorkshop.addFunction(classes.toArray(new Class[classes.size()]));
        System.out.println("\nfunctions (type : " + FunctionWorkshop.getFUNCTIONS().size() + " ,count : " + FunctionWorkshop.getCount() + ") found success in " + (System.currentTimeMillis() - begin) + " ms");

        System.out.println("\ntry insert functions to user class");
        classes.forEach(FieldInjection::insertField);
        System.out.println("inserted function over");

        System.out.println("\nfind orders");
        //加载指令
        classes.stream().filter(CmdOutOrder.class::isAssignableFrom).forEach(a -> {
            @SuppressWarnings({"unchecked"}) final Class<? extends CmdOutOrder> outOrder = (Class<? extends CmdOutOrder>) a;
            addOutOrder(outOrder);
        });
        System.out.println("loaded orders over");


    }


    static void addOutOrder(Class<? extends CmdOutOrder> outOrder) {
        try {
            if (outOrder.isInterface() || outOrder.isAnonymousClass() || outOrder.isLocalClass() || outOrder.isMemberClass()) {
                return;
            }
            CmdOutOrder cmdOutOrder;
            if (outOrder.isEnum()) {
                final CmdOutOrder[] invoke = (CmdOutOrder[]) outOrder.getMethod("values").invoke(null);
                if (invoke.length != 1) {
                    throw new RuntimeException(outOrder + " enumType must have only one instance");
                }
                cmdOutOrder = invoke[0];
            } else {
                cmdOutOrder = outOrder.newInstance();
            }
            addOutOrder(cmdOutOrder);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    static void addOutOrder(CmdOutOrder outOrder) {
        final String nameSpace = outOrder.getNameSpace();
        final CmdOutOrder cmdOutOrder = NAMESPACE.get(nameSpace);
        if (cmdOutOrder != null) {
            throw new RuntimeException(cmdOutOrder.getClass() + " | " + outOrder.getClass() + " have same namespace : " + nameSpace);
        }
        if (outOrder instanceof SystemCmdOutOrder) {
            outOrder.setLogLevel("ERROR");
        }
        System.out.println(addSpacingToLength.apply(nameSpace, 20) + "load success");
        NAMESPACE.put(nameSpace, outOrder);
    }

    static String getDescription() {
        StringBuilder result = new StringBuilder("QianRui Cmd\nis a test fun work and it support some order\n");
        result.append("\ninput '").append(HelpOutOrderImpl.INSTANCE.getNameSpace()).append(" [order]'\nto show how to use this order\n");
        result.append("\nnow have order list is : \n");
        NAMESPACE.forEach((a, b) -> result.append(addSpacingToLength.apply(a, 20)).append("by(").append(b.getClass()).append(")\n"));
        result.append("\nif you have some awesome ideas or codes or you just want to join this work\n");
        result.append("please contact me!!!\n>>>\temail 1135901259@qq.com\n>>>\tphoneNumber 18715600499\n");
        return result.toString();
    }


    public static void main(String[] args) {
//        System.getProperties().forEach((a,b)-> System.out.println(a+"    "+b));
//        final Package[] packages = Package.getPackages();
//        System.out.println(packages.length);
//        Arrays.stream(packages).filter(a -> !a.getName().startsWith("java") && !a.getName().startsWith("sun")).forEach(System.out::println);
        System.out.println("\nStarted Success In " + (System.currentTimeMillis() - START_TIME) + "ms");
        System.out.println("\n欢迎使用集成工具cmd");
        String line;
        String[] orders;
        CmdOutOrder cmdOutOrder;
        while (true) {
//            System.out.print("[cmd@root]$ ");
            line = SC.nextLine();
            orders = maxSplitWords.apply(line, 2);
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
                System.out.println(deepMessage(e));
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

    static String deepMessage(Throwable e) {
        if (e.getCause() != null) {
            return deepMessage(e.getCause());
        }
        return e.getMessage();
    }


    /**
     * TODO basic cmd input or output operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */
    static int whileInt(Supplier<String> getLine, Runnable title) throws Throwable {
        while (true) {
            try {
                title.run();
                String line = getLine.get();
                if ("exit".equals(line.trim())) {
                    throw new Throwable("终止操作");
                }
                return Integer.parseInt(line);
            } catch (Exception e) {
                //
            }
        }
    }

    static String orderLine() {
        return SC.nextLine();
    }

    public static <E> void cmdPrintln(E e) {
        System.out.println(e);
    }

    public static void cmdPrintln(Supplier<String> message) {
        cmdPrintln(message.get());
    }
}
