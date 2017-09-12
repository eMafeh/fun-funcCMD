package socket.core;

import socket.enums.CmdControOrder;
import socket.enums.XiaoQiuYinOrder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static util.UnUsePort.*;

/**
 * Created by snb on 2017/9/8  9:50
 */
public class CmdMessageController {
    private static final Scanner sc = new Scanner(System.in);


    private static volatile boolean noSilent = true;

    public static void main(String[] args) {
        XiaoQiuYinBoot instance = null;
        while (true) {
            String s = sc.nextLine();
            if (s.equals("exit")) break;

            if (s.equals("silent")) {
                noSilent = false;
                continue;
            }
            if (s.equals("show")) {
                noSilent = true;
                continue;
            }

            if (s.equals("xqy run") && instance == null) {
                instance = xqy();
                continue;
            }
            if (instance == null) continue;

            if (s.equals("xqy exit")) {

                instance.shutdown();
                instance = null;
                continue;
            }
            try {
                if (s.startsWith("xqy ")) instance.sendMessage(s.substring(4));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        XiaoQiuYinBoot.exit();
    }

    //TODO cmd core operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    static XiaoQiuYinBoot xqy() {
        int inport;
        int farport;
        String farhost = "10.39.14.191";
        String downLoadPath = "E:\\XqlDownload";

        System.out.println("欢迎使用小蚯蚓聊天工具");
        while (true) {
            inport = getInt("请确认接收端口");
            if (unUsePort(inport)) break;
            System.out.println(inport + "该端口被占用，请选择其他端口");
        }
        farport = getInt("请确认对方端口");

        System.out.println("使用愉快，输入 xqy over 退出，输入help查看完整指令");
        return XiaoQiuYinBoot.getInstance(farhost, farport, inport, new File(downLoadPath));
    }

    private static final Map<String, CmdControOrder> ORDERTANK = new HashMap<>();
    private static final Map<String, CmdControOrder> jobTank = new HashMap<>();

    static synchronized void add() {
        String name = check(XiaoQiuYinOrder.orderTank);
        System.out.println(name == null ? "新指令添加成功" : "新指令中该指令名称冲突 : " + name);
        ORDERTANK.putAll(XiaoQiuYinOrder.orderTank);

    }

    private static String check(Map<String, XiaoQiuYinOrder> orderTank) {
        Set<String> strings = orderTank.keySet();
        Set<String> old = ORDERTANK.keySet();
        for (String s : strings)
            for (String s1 : old)
                if (s.equals(old))
                    return s;
        return null;
    }

    //TODO basic cmd input or output operate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static int getInt(String title) {
        while (true) try {
            System.out.println(title);
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
        }
    }

    public static <E> void cmdprintln(E e) {
        if (noSilent)
            System.out.println(e);
    }

    public static boolean isNoSilent() {
        return noSilent;
    }
}
