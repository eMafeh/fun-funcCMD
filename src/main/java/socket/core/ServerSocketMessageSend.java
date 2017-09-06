package socket.core;

import socket.model.Good_LocalIP;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

/**
 * Created by snb on 2017/9/14  22:32
 */
public class ServerSocketMessageSend {
    private static Map<String, ServerSocketMessageSend> SENDERS = new HashMap<>();
    private String address;
    private String host;
    private int port;
    private Thread thread;
    private List<String> messages = new Vector<>();
    private volatile boolean flag;

    public static ServerSocketMessageSend getSender(String host, int port) {
        if (port < 100 || port > 1 << 16) throw new IllegalArgumentException(port + "");
        ServerSocketMessageSend sender = SENDERS.computeIfAbsent(host + ":" + port, a -> new ServerSocketMessageSend(a));
        synchronized (sender) {
            if (!sender.flag)
                sender.start();
            return sender;
        }

    }

    private ServerSocketMessageSend(String address) {
        this.address = address;
        String[] split = address.split(":");
        host = split[0];
        port = Integer.parseInt(split[1]);
        start();
    }

    private void start() {
        this.flag = true;
        this.thread = new Thread(() -> {
            while (flag) try {
                sendMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        System.out.println("发送端已打开");
    }

    private void sendMessage() throws InterruptedException {
        if (!messages.isEmpty()) {
            String message = messages.remove(0);
            while (flag) {
                try (Socket socket = new Socket(host, port); OutputStream outputStream = socket.getOutputStream()) {
                    outputStream.write(message.getBytes());
                    break;
                } catch (IOException e) {
                    Thread.sleep(100);
                }
            }
            return;
        }
        Thread.sleep(100);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public String getAddress() {
        return address;
    }

    public synchronized void shutDown() {
        if (!flag) return;
        flag = false;
        messages.forEach(System.out::println);
        messages.clear();
    }

    public synchronized boolean isalive() {
        return flag;
    }

}
