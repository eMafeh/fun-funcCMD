package com.qr.core;

import com.qr.function.FunctionWorkshop;
import com.qr.injection.FieldInjection;
import util.FindClassUtils;

import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 2017/9/8  9:50
 *
 * @author qianrui
 */
public class CmdBoot {
    private static final Scanner SC = new Scanner(System.in);

    private static final long START_TIME = System.currentTimeMillis();


    static {
        //加载系统类
        final Class<?>[] systemClasses = FindClassUtils.SINGLETON.getSystemClasses();
        //加载用户类
        final Set<Class<?>> classes = FindClassUtils.SINGLETON.getClasses();

        final long begin = System.currentTimeMillis();
        cmdPrintln("\ntry find functions in all classes");
        //系统类函数采集
        FunctionWorkshop.addFunction(systemClasses);
        //用户类函数采集
        FunctionWorkshop.addFunction(classes.toArray(new Class[classes.size()]));
        cmdPrintln("\nfunctions (type : " + FunctionWorkshop.getFUNCTIONS().size() + " ,count : " + FunctionWorkshop.getCount() + ") found success in " + (System.currentTimeMillis() - begin) + " ms");

        cmdPrintln("\ntry insert functions to user class");
        classes.forEach(FieldInjection::insertField);
        cmdPrintln("inserted function over");
        cmdPrintln("\nfind commands");
        cmdPrintln("\nStarted Success In " + (System.currentTimeMillis() - START_TIME) + "ms");
    }


    public static void main(String[] args) {
//        System.getProperties().forEach((a,b)-> System.out.println(a+"    "+b));
//        final Package[] packages = Package.getPackages();
//        System.out.println(packages.length);
//        Arrays.stream(packages).filter(a -> !a.getName().startsWith("java") && !a.getName().startsWith("sun")).forEach(System.out::println);

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Phoenix.robotRun();
//        System.exit(0);
        SystemCmdOutCommand main = MainOutCommandImpl.INSTANCE;
        main.install(CmdBoot::orderLine);
        main.useCommand("run mouse");
        main.useCommand("mouse move true");
        main.useCommand("log mouse debug");
        boolean goOn;
        do goOn = main.useCommand(SC.nextLine());
        while (goOn);
    }


    /**
     * TODO basic cmd input or output operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

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
