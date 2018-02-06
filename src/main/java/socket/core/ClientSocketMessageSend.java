package socket.core;

import socket.config.CharsetConfig;
import util.AllThreadUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * 2017/9/14  22:32
 *
 * @author qianrui
 */
public class ClientSocketMessageSend {
    private static AllThreadUtil.Key key;

    public static synchronized void start() {
        if (key != null) {
            return;
        }
        key = AllThreadUtil.whileTrueThread(ClientSocketMessageSend::sendMessage, 100);
    }

    private static boolean sendMessage() throws InterruptedException {
        if (!SendMessage.MESSAGES.isEmpty()) {
            SendMessage send = SendMessage.MESSAGES.get(0);
            while (key.isRun()) {
                try (Socket socket = new Socket(send.host, send.port); OutputStream outputStream = socket.getOutputStream()) {
                    outputStream.write(send.message.getBytes(CharsetConfig.UTF8));
                    SendMessage.MESSAGES.remove(0);
                    break;
                } catch (IOException e) {
                    Thread.sleep(100);
                }
            }
            return false;
        }
        return true;
    }

    public static void addMessage(String message, String host, int port) {
        SendMessage.addMessage(message, host, port);
    }

    public static synchronized void shutDown() {
        AllThreadUtil.stop(key);
        key = null;
        SendMessage.MESSAGES.forEach(CmdMessageController::cmdPrintln);
        SendMessage.MESSAGES.clear();
    }

    public static boolean isalive() {
        return key.isRun();
    }

    private static class SendMessage {
        private static final List<SendMessage> MESSAGES = new Vector<>();

        private String message;
        private String host;
        private int port;

        @Override
        public String toString() {
            return "SendMessage{" + "message='" + message + '\'' + ", host='" + host + '\'' + ", port=" + port + '}';
        }

        private SendMessage(String message, String host, int port) {
            this.message = message;
            this.host = host;
            this.port = port;
        }

        public static void addMessage(String message, String host, int port) {
            if (badMessage(message, host, port)) {
                //坏信息不添加至信息空间
                return;
            }
            MESSAGES.add(new SendMessage(message, host, port));
        }

        private static boolean badMessage(String message, String host, int port) {
            //不是坏信息，返回false
            return false;
        }
    }

}
