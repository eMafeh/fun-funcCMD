package socket.core;

import socket.FileOutListener;
import util.AllThreadUtil;
import util.LocalIp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 2017/9/8  9:50
 *
 * @author qianrui
 */
public class CmdMessageController {
    private static final Scanner SC = new Scanner(System.in);

    private static final List<String> nameSpace = new ArrayList<>();
    private static volatile boolean noSilent = true;

    public static void main(String[] args) {
        FileOutListener.init();


        XiaoQiuYinBoot instance = null;
        while (true) {
            String s = SC.nextLine().trim();
            if ("exit".equals(s)) {
                break;
            }

            if ("silent".equals(s)) {
                noSilent = false;
                continue;
            }
            if ("show".equals(s)) {
                noSilent = true;
                continue;
            }

            if ("xqy run".equals(s) && instance == null) {
                try {
                    instance = xqy();
                } catch (Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
                continue;
            }
            if (instance == null) {
                continue;
            }

            if ("xqy exit".equals(s)) {

                instance.shutdown();
                instance = null;
                continue;
            }
            try {
                if (s.startsWith("xqy ")) {
                    instance.sendMessage(s.substring(4));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileOutListener.shutDown();
        XiaoQiuYinBoot.exit();
        AllThreadUtil.exit();
    }

    /**
     * TODO cmd core operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

    static XiaoQiuYinBoot xqy() throws Throwable {
        int farPort;
        String farHost;

        String downLoadPath = "E:\\XqlDownload";

        System.out.println("欢迎使用小蚯蚓聊天工具");

        ServerSocketInMessageQueue xqyServer;
        while (true) {
            try {
                xqyServer = ServerSocketInMessageQueue.getServer(getInt("请确认小蚯蚓信息接收端口"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("小蚯蚓信息接收端口成功打开" + xqyServer.getPort());

        farPort = getInt("请确认对方端口");

        System.out.println("请确认对方ip");
        farHost = SC.nextLine().trim();
        System.out.println("使用愉快，输入 xqy over 退出，输入help查看完整指令");
        return XiaoQiuYinBoot.getInstance(farHost.endsWith("") ? LocalIp.getIP() : farHost, farPort, xqyServer, new File(downLoadPath));
    }

//    private static final Map<String, CmdControOrder> ORDERTANK = new HashMap<>();
//    private static final Map<String, CmdControOrder> jobTank = new HashMap<>();

//    static synchronized void add() {
//        String name = check(XiaoQiuYinOrder.orderTank);
//        System.out.println(name == null ? "新指令添加成功" : "新指令中该指令名称冲突 : " + name);
//        ORDERTANK.putAll(XiaoQiuYinOrder.orderTank);
//
//    }
//
//    private static String check(Map<String, XiaoQiuYinOrder> orderTank) {
//        Set<String> strings = orderTank.keySet();
//        Set<String> old = ORDERTANK.keySet();
//        for (String s : strings)
//            for (String s1 : old)
//                if (s.equals(old)) return s;
//        return null;
//    }

    /**
     * TODO basic cmd input or output operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */
    public static int getInt(String title) throws Throwable {
        while (true) {
            try {
                System.out.println(title);
                String line = SC.nextLine();
                if ("exit".equals(line.trim())) {
                    throw new Throwable("终止操作");
                }
                return Integer.parseInt(line);
            } catch (Exception e) {
                //
            }
        }
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
