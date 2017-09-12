package socket.core;

import socket.FileInListener;
import socket.IOSocketFileSend;
import socket.model.Good_LocalIP;
import util.LoopThread;

import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Created by snb on 2017/9/6  11:18
 */
public class XiaoQiuYinBoot {
    private static final List<XiaoQiuYinBoot> XQYBOOTS = new Vector<>();
    private final Flag flag = new Flag();
    private String host;
    private int farport;
    private int inport;
    private File directory;
    private ServerSocketInMessageQueue server;
    private Runnable runnable = () -> {
        String s = server.nextMessage();
        if (s != null) userMessage(s);
    };
    private LoopThread.TankKey tankKey;

    private XiaoQiuYinBoot(String host, int farport, int inport, File directory) {
        this.directory = directory;
        this.farport = farport;
        this.host = host;
        this.inport = inport;

        ClientSocketMessageSend.start();
        flag.flag = true;
        server = ServerSocketInMessageQueue.getServer(inport);
        XQYBOOTS.add(this);

    }

    public static XiaoQiuYinBoot getInstance(String host, int farport, int inport, File directory) {
        XiaoQiuYinBoot boot = new XiaoQiuYinBoot(host, farport, inport, directory);
        boot.tankKey = LoopThread.getLoopThread(1).addLoopTankByTenofOneSecond(boot.runnable, 1, -1);
        return boot;
    }

    public void shutdown() {
        LoopThread.getLoopThread().removeLoopTank(tankKey);
        flag.flag = false;
        server.shutdown("退出成功，欢迎下次使用");
        XQYBOOTS.remove(this);
    }

    public synchronized static void exit() {
        XQYBOOTS.forEach(a -> {
            LoopThread.getLoopThread().removeLoopTank(a.tankKey);
            a.flag.flag = false;
            a.server.shutdown("退出成功，欢迎下次使用");
        });
        XQYBOOTS.clear();
        ClientSocketMessageSend.shutDown();
    }

    public void sendMessage(String message) {
        String xqymessage = XqytpMessage.jsonMessage(message, Good_LocalIP.getIP(), inport, IOSocketFileSend.getFileModel(new File(message)));
        ClientSocketMessageSend.addMessage(xqymessage, host, farport);
    }

    private void userMessage(String message) {
        XqytpMessage xqytpMessage = XqytpMessage.readObject(message);
        FileInListener.listenForFile(xqytpMessage.filePackage, directory);
        CmdMessageController.cmdprintln(xqytpMessage);
    }


}
