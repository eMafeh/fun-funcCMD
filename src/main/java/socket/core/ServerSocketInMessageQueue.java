package socket.core;

import socket.config.CharsetConfig;
import util.LocalIp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static util.UnUsePort.unUsePort;

/**
 * @author qianrui
 */
public class ServerSocketInMessageQueue {
    private static final Map<Integer, ServerSocketInMessageQueue> SERVERS = new HashMap<>();

    private int port;
    private ServerSocket serverSocket;
    private volatile boolean flag = false;
    private final List<String> messageQueue = new Vector<>();
    private byte[] b = new byte[1 << 16];

    public synchronized static ServerSocketInMessageQueue getServer(int inPort) throws IllegalArgumentException {
        ServerSocketInMessageQueue messageQueue = SERVERS.get(inPort);
        if (messageQueue != null && messageQueue.flag) {
            throw new IllegalArgumentException(inPort + "该信息队列已经被使用");
        }
        checkPort(inPort);
        if (messageQueue == null) {
            messageQueue = new ServerSocketInMessageQueue();
        }

        try {
            messageQueue.serverSocket = new ServerSocket(inPort);
        } catch (IOException e) {
            throw new IllegalArgumentException(inPort + "端口被占用", e);
        }
        messageQueue.port = inPort;
        messageQueue.start();
        SERVERS.put(inPort, messageQueue);
        return messageQueue;
    }

    private static void checkPort(int inPort) throws IllegalArgumentException {
        if (inPort < 100 || inPort > 1 << 16) {
            throw new IllegalArgumentException(inPort + "端口不合适");
        }
        if (!unUsePort(inPort)) {
            throw new IllegalArgumentException(inPort + "端口被占用");
        }
    }

    private ServerSocketInMessageQueue() {
    }

    private synchronized void start() {
        if (flag) {
            return;
        }
        flag = true;
        new Thread(() -> {
            String message;
            while (flag) {
                try {
                    message = getMessage();
                    if (!"".equals(message)) {
                        messageQueue.add(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("接收端已打开");
    }


    private String getMessage() throws IOException {
        StringBuilder result = new StringBuilder();
        try (Socket socket = serverSocket.accept(); InputStream inputStream = socket.getInputStream()) {
            int read;
            while ((read = inputStream.read(b)) > -1) {
                result.append(new String(b, 0, read, CharsetConfig.UTF8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public synchronized String nextMessage() {
        return messageQueue.isEmpty() ? null : messageQueue.remove(0);
    }

    public synchronized void shutdown(String over) {
        if (!flag) {
            return;
        }
        flag = false;
        try (Socket socket = new Socket(LocalIp.getIP(), port); OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write((over == null ? "" : over).getBytes(CharsetConfig.UTF8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
            serverSocket.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        if (CmdMessageController.isNoSilent()) {
            messageQueue.forEach(CmdMessageController::cmdPrintln);
        }
        messageQueue.clear();
        serverSocket = null;
    }

    public synchronized boolean isalive() {
        return flag;
    }

    public int getPort() {
        return port;
    }
}
