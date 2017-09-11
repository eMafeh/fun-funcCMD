package socket.core;

import socket.model.UnUsePort;

import java.io.File;
import java.util.Scanner;

import static socket.model.UnUsePort.*;

/**
 * Created by snb on 2017/9/8  9:50
 */
public class CmdMessageController {

    private static volatile boolean silent = true;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("欢迎使用小蚯蚓聊天工具");
        int inport;
        while (true) {
            inport = getInt("请确认接收端口");
            if (unUsePort(inport)) break;
            System.out.println(inport + "该端口被占用，请选择其他端口");
        }
        int farport = getInt("请确认对方端口");
        XiaoQiuYinBoot instance = XiaoQiuYinBoot.getInstance("10.39.14.191", farport, inport, new File("E:\\XqlDownload"));
        System.out.println("使用愉快，输入 xqy over 退出，输入help查看完整指令");
        while (true) {
            String s = sc.nextLine();
            if (s.startsWith("xqy over")) {
                instance.shutdown();
                break;
            }
            try {
                if (s.startsWith("xqy ")) instance.sendMessage(s.substring(4));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        XiaoQiuYinBoot.exit();
    }

    public static int getInt(String title) {
        while (true) try {
            System.out.println(title);
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
        }
    }

    public static <E> void cmdprintln(E e) {
        if (silent)
            System.out.println(e);
    }

}
