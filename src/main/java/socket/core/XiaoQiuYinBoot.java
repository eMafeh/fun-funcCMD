package socket.core;

import socket.model.Good_LocalIP;

import java.util.Date;
import java.util.Scanner;

/**
 * Created by snb on 2017/9/6  11:18
 */
public class XiaoQiuYinBoot {
    static Scanner sc = new Scanner(System.in);
    static String host = "10.39.14.191";

    public static void main(String[] args) {

        System.out.println("欢迎使用小蚯蚓聊天工具");

        System.out.println("请确认接收端口");
        int inport = sc.nextInt();
        System.out.println("请确认对方端口");
        int farport = sc.nextInt();


        final Flag flag = new Flag();
        flag.flag = true;
        Thread sendThread = getSendThread(flag, farport, inport);
        sendThread.start();

        ServerSocketMessageQueue server = ServerSocketMessageQueue.getServer(inport);
        TimeWaitTank.tank(() -> {
            String s = server.nextMessage();
            if (s != null) System.out.println(XqytpMessage.readObject(s));
        }, flag, 100);

        server.shutdown("退出成功，欢迎下次使用");
    }

    static Thread getSendThread(Flag flag, int farport, int inport) {
        return new Thread(() -> {
            ServerSocketMessageSend sender = ServerSocketMessageSend.getSender(host, farport);
            while (flag.flag) {
                String s = sc.nextLine();
                if (!s.isEmpty())
                    sender.addMessage(XqytpMessage.jsonMessage(s, Good_LocalIP.getIP(), inport));
                if (s.equals("over")) {
                    flag.flag = false;
                    break;
                }
            }
            sender.shutDown();
        });
    }
}
