package test;

import socket.core.ServerSocketMessageQueue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Test {
    public static void main(String[] args) {
        System.out.println(1);
        ServerSocketMessageQueue.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        class Flag{
            boolean flag=true;
        }
        final Flag flag= new Flag();
        new Thread(() -> {
            while (flag.flag) System.out.println(ServerSocketMessageQueue.nextMessage());
        }).start();
        for (int i = 0; i < 100; i++)
            try {
                Socket socket = new Socket("10.39.14.191", 4043);
                try (OutputStream outputStream = socket.getOutputStream()
                ) {
                    System.out.println(i + "a");
                    outputStream.write((i + "").getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        System.out.println(3);
        ServerSocketMessageQueue.shutdown();
        flag.flag=false;
    }

}
