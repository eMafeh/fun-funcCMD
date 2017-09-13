package test;

import socket.IOSocketFileReceive;
import socket.core.CmdMessageController;
import util.LoopThread;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Future<?> submit = executorService.submit(() -> {
//            Thread.sleep(1000);
//            System.out.println(1);
//            return 2;
//        });
//        System.out.println(0);
//        System.out.println(submit.get());
//        System.out.println(1);
//        System.out.println(submit.get());
//        System.out.println(new File("D:\\").getPath());
//        System.out.println(1);
//        ServerSocketInMessageQueue.start();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        class Flag{
//            boolean flag=true;
//        }
//        final Flag flag= new Flag();
//        new Thread(() -> {
//            while (flag.flag) System.out.println(ServerSocketInMessageQueue.nextMessage());
//        }).start();
//        for (int i = 0; i < 100; i++)
//            try {
//                Socket socket = new Socket("10.39.14.191", 4043);
//                try (OutputStream outputStream = socket.getOutputStream()
//                ) {
//                    System.out.println(i + "a");
//                    outputStream.write((i + "").getBytes());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        System.out.println(3);
//        ServerSocketInMessageQueue.shutdown();
//        flag.flag=false;
    }

}
