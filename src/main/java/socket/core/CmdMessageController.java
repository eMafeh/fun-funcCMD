package socket.core;

import socket.FileOutListener;
import socket.config.IOPortConfig;
import util.Good_LocalIP;

import java.io.File;
import java.util.Scanner;

import static util.UnUsePort.unUsePort;

/**
 * 2017/9/8  9:50
 *
 * @author qianrui
 */
public class CmdMessageController {
    private static final Scanner SC = new Scanner(System.in);


    private static volatile boolean noSilent = true;

    public static void main(String[] args) {
        boolean[] flag = new boolean[1];
        flag[0] = true;
        //先完成文件监听
        new Thread(() -> FileOutListener.init(IOPortConfig.MESSAGE_PORT.getPort(), flag)).start();


        XiaoQiuYinBoot instance = null;
        while (true) {
            String s = SC.nextLine();
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
                instance = xqy();
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
        flag[0] = false;
        XiaoQiuYinBoot.exit();
    }

    /**
     * TODO cmd core operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */
    static XiaoQiuYinBoot xqy() {
        int inPort;
        int farPort;
        String farHost;

        String downLoadPath = "E:\\XqlDownload";

        System.out.println("欢迎使用小蚯蚓聊天工具");

        while (true) {
            inPort = getInt("请确认接收端口");
            if (unUsePort(inPort)) {
                break;
            }
            System.out.println(inPort + "该端口被占用，请选择其他端口");
        }
        farPort = getInt("请确认对方端口");


//        System.out.println("请确认对方ip");
//        farHost = SC.nextLine().trim();
        farHost = Good_LocalIP.getIP();
        System.out.println("使用愉快，输入 xqy over 退出，输入help查看完整指令");
        return XiaoQiuYinBoot.getInstance(farHost, farPort, inPort, new File(downLoadPath));
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
    public static int getInt(String title) {
        while (true) {
            try {
                System.out.println(title);
                return Integer.parseInt(SC.nextLine());
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
