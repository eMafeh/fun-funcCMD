package socket.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * 2017/9/14  22:32
 * @author qianrui
 */
public class ClientSocketMessageSend {
    private static final Flag FLAG = new Flag();
    private static Thread thread;

    public static synchronized void start() {
        if (thread != null) {
            return;
        }
        FLAG.flag = true;
        thread = getSendThread();
        thread.start();
        CmdMessageController.cmdPrintln("发送端已打开");
    }

    private static Thread getSendThread() {
        return new Thread(() -> {
            while (FLAG.flag) {
                try {
                    sendMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void sendMessage() throws InterruptedException {
        if (!SendMessages.MESSAGES.isEmpty()) {
            SendMessages send = SendMessages.MESSAGES.get(0);
            while (FLAG.flag) {
                try (OutputStream outputStream = new Socket(send.host, send.port).getOutputStream()) {
                    outputStream.write(send.message.getBytes());
                    SendMessages.MESSAGES.remove(0);
                    break;
                } catch (IOException e) {
                    Thread.sleep(100);
                }
            }
            return;
        }
        Thread.sleep(100);
    }

    public static void addMessage(String message, String host, int port) {
        SendMessages.MESSAGES.add(new SendMessages(message, host, port));
    }

    public static synchronized void shutDown() {
        if (!FLAG.flag) {
            return;
        }
        FLAG.flag = false;
        thread = null;
        SendMessages.MESSAGES.forEach(CmdMessageController::cmdPrintln);
        SendMessages.MESSAGES.clear();
    }

    public static synchronized boolean isalive() {
        return FLAG.flag;
    }

    private static class SendMessages {
        private static final List<SendMessages> MESSAGES = new Vector<>();

        private String message;
        private String host;
        private int port;

        @Override
        public String toString() {
            return "SendMessages{" + "message='" + message + '\'' + ", host='" + host + '\'' + ", port=" + port + '}';
        }

        private SendMessages(String message, String host, int port) {
            this.message = message;
            this.host = host;
            this.port = port;
        }
    }
}
