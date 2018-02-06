package socket.core;

import socket.FileInListener;
import socket.IOSocketFileSend;
import util.LocalIp;
import util.LoopThread;

import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Created by snb on 2017/9/6  11:18
 */
public class XiaoQiuYinBoot {
    private static final List<XiaoQiuYinBoot> XQY_BOOTS = new Vector<>();
    private final Flag flag = new Flag();
    private String host;
    private int farPort;
    private File directory;
    private ServerSocketInMessageQueue server;
    private Runnable runnable = () -> {
        String s = server.nextMessage();
        if (s != null) {
            userMessage(s);
        }
    };
    private LoopThread.TankKey tankKey;

    private XiaoQiuYinBoot(String host, int farPort, ServerSocketInMessageQueue server, File directory) {
        this.directory = directory;
        this.farPort = farPort;
        this.host = host;

        ClientSocketMessageSend.start();
        flag.isRun = true;
        this.server = server;
        XQY_BOOTS.add(this);

    }

    public static XiaoQiuYinBoot getInstance(String host, int farPort, ServerSocketInMessageQueue server, File directory) {
        XiaoQiuYinBoot boot = new XiaoQiuYinBoot(host, farPort, server, directory);
        boot.tankKey = LoopThread.getLoopThread(1).addLoopTankByTenofOneSecond(boot.runnable, 1, -1);
        return boot;
    }

    public void shutdown() {
        LoopThread.getLoopThread().removeLoopTank(tankKey);
        flag.isRun = false;
        server.shutdown("退出成功，欢迎下次使用");
        XQY_BOOTS.remove(this);
    }

    public synchronized static void exit() {
        XQY_BOOTS.forEach(a -> {
            LoopThread.getLoopThread().removeLoopTank(a.tankKey);
            a.flag.isRun = false;
            a.server.shutdown("退出成功，欢迎下次使用");
        });
        XQY_BOOTS.clear();
        ClientSocketMessageSend.shutDown();
    }

    public void sendMessage(String message) {
        String xqyMessage = XqytpMessage.jsonMessage(message, LocalIp.getIP(), server.getPort(), IOSocketFileSend.getFileModel(message));
        ClientSocketMessageSend.addMessage(xqyMessage, host, farPort);
    }

    private void userMessage(String message) {
        XqytpMessage xqytpMessage = XqytpMessage.readObject(message);
        FileInListener.listenForFile(xqytpMessage.filePackage, directory);
        CmdMessageController.cmdPrintln(xqytpMessage);
    }
}
