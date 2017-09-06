package socket.core;

import socket.ServerSocketFactory;
import socket.model.Good_LocalIP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ServerSocketMessageQueue {
    private static Map<Integer, ServerSocketMessageQueue> SERVERS = new HashMap<>();
    private int port;
    private Thread thread;
    private ServerSocket serverSocket;

    private final List<String> messageQueue = new Vector<>();
    private volatile boolean flag;
    private byte[] b = new byte[1 << 16];


    public static ServerSocketMessageQueue getServer(int port) {
        if (port < 100 || port > 1 << 16) throw new IllegalArgumentException(port + "");
        ServerSocketMessageQueue server = SERVERS.computeIfAbsent(port, a -> new ServerSocketMessageQueue(a));
        synchronized (server) {
            if (!server.flag)
                server.start();
            return server;
        }
    }

    private ServerSocketMessageQueue(int port) {
        this.port = port;
        start();
    }

    private synchronized void start() {
        if (serverSocket != null) return;
        serverSocket = ServerSocketFactory.getServerSocket(port);
        flag = true;
        thread = new Thread(() -> {
            while (flag) {
                try {
                    String message = getMessage();
                    if (message != null && message != "")
                        messageQueue.add(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        System.out.println("接收端已打开");
    }

    private String getMessage() throws IOException {
        StringBuilder result = new StringBuilder();
        try (Socket socket = serverSocket.accept(); InputStream inputStream = socket.getInputStream()) {
            int read;
            while ((read = inputStream.read(b)) > -1)
                result.append(new String(b, 0, read));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public synchronized String nextMessage() {
        return messageQueue.isEmpty() ? null : messageQueue.remove(0);
    }

    public synchronized void shutdown(String over) {
        if (!flag) return;
        flag = false;
        try (Socket socket = new Socket(Good_LocalIP.getIP(), port); OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write((over == null ? "" : over).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messageQueue.forEach(System.out::println);
        messageQueue.clear();
        serverSocket = null;
    }

    public synchronized boolean isalive() {
        return flag;
    }
}
