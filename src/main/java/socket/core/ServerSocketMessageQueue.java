package socket.core;

import socket.ServerSocketFactory;
import socket.SocketFileClient;
import socket.config.IOPortConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ServerSocketMessageQueue {
    private static final IOPortConfig message_port=IOPortConfig.MESSAGE_PORT;
    private static final ServerSocket serverSocket = ServerSocketFactory.getServerSocket(message_port);

    private static final List<String> messageQueue = new Vector<>();

    private static volatile boolean flag = true;
    private static byte[] b = new byte[1 << 16];

    private static String getMessage() throws IOException {
        Socket socket = serverSocket.accept();
        StringBuilder result = new StringBuilder();
        try (InputStream inputStream = socket.getInputStream()) {
            int read;
            while ((read = inputStream.read(b)) > -1)
                result.append(new String(b, 0, read));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void start() {
        Thread messageThread = new Thread(() -> {
            while (flag) {
                try {
                    System.out.println(1);
                    String message = getMessage();
                    messageQueue.add(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        messageThread.start();
    }

    public static String nextMessage() {
        return  messageQueue.isEmpty()?null:messageQueue.remove(0);
    }

    public static void shutdown() {
        flag = false;
        try {
            Socket socket = new Socket("localhost", message_port.getPort());
            try (OutputStream outputStream = socket.getOutputStream()
            ) {
                outputStream.write("".getBytes());
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageQueue.forEach(System.out::println);
        messageQueue.clear();
    }
}
