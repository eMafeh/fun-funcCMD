package com.qr.core;

import com.qr.function.FunctionWorkshop;
import com.qr.function.ProxyFunction;
import util.AllThreadUtil;
import util.FindClassUtils;
import util.StringSplitUtil;
import util.StringValueUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 2017/9/8  9:50
 *
 * @author qianrui
 */
public class CmdBoot {
    private static final Scanner SC = new Scanner(System.in);
    static final Map<String, CmdOutOrder> NAMESPACE = new HashMap<>();


    static {
        //加载系统类
        final Class<?>[] systemClasses = FindClassUtils.SINGLETON.getSystemClasses();
        //系统类函数采集
        FunctionWorkshop.addFunction(systemClasses);

        //加载用户类
        final Set<Class<?>> classes = FindClassUtils.SINGLETON.getClasses();
        //用户类函数采集
        FunctionWorkshop.addFunction(classes.toArray(new Class[classes.size()]));

        System.out.println("com.qr包下内容进行自动注入");
        classes.stream().filter(aClass -> aClass.getName().startsWith("com.qr")).forEach(aClass -> {
            final Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                final Map<Method, ProxyFunction> functionMap = FunctionWorkshop.getFUNCTIONS().get(field.getGenericType());
                if (functionMap != null) {
                    Method mark = null;
                    int count = 0;
                    for (Method method : functionMap.keySet()) {
                        if (method.getName().equals(field.getName())) {
                            mark = method;
                            count++;
                        }
                    }
                    if (mark == null) {
                        mark = (Method) functionMap.values().toArray()[0];
                    }
                    try {
                        field.setAccessible(true);
                        final ProxyFunction proxyFunction = functionMap.get(mark);
                        System.out.println(field + " 有" + count + "个合适的函数可用，选则了" + proxyFunction);
                        field.set(null, proxyFunction);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //加载指令
        classes.stream().filter(CmdOutOrder.class::isAssignableFrom).forEach(a -> {
            @SuppressWarnings({"unchecked"}) final Class<? extends CmdOutOrder> outOrder = (Class<? extends CmdOutOrder>) a;
            addOutOrder(outOrder);
        });


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
        NAMESPACE.put(nameSpace, outOrder);
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


    public static void main(String[] args) {
//        try {
//            final Field trueFalse = MouseOutOrderImpl.INSTANCE.getClass().getDeclaredField("caseTrueFalse");
//            trueFalse.setAccessible(true);
//            final Object[] objects = getFUNCTIONS().get(trueFalse.getGenericType()).values().toArray();
//            for (Object object : objects) {
//                System.out.println(object);
//            }
//            final Object object = objects[SecureRandom.getInstanceStrong().nextInt(objects.length)];
//            System.out.println("chose : " + object);
//            trueFalse.set(MouseOutOrderImpl.INSTANCE, object);
//        } catch (IllegalAccessException | NoSuchAlgorithmException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }

//        System.getProperties().forEach((a,b)-> System.out.println(a+"    "+b));
//        final Package[] packages = Package.getPackages();
//        System.out.println(packages.length);
//        Arrays.stream(packages).filter(a -> !a.getName().startsWith("java") && !a.getName().startsWith("sun")).forEach(System.out::println);
        String line;
        String[] orders;
        CmdOutOrder cmdOutOrder;
        while (true) {
//            System.out.print("[cmd@root]$ ");
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
                System.out.println(getMessage(e));
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

    private static String getMessage(Throwable e) {
        if (e.getCause() != null) {
            return getMessage(e.getCause());
        }
        return e.getMessage();
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

    public static <E> void cmdPrintln(Supplier<String> message) {
        cmdPrintln(message.get());
    }
}
