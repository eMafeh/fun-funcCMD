package com.qr.order;

import com.alibaba.fastjson.JSON;
import com.qr.core.CmdBoot;
import socket.core.ClientSocketMessageSend;
import socket.core.ServerSocketInMessageQueue;
import socket.xqy.XqytpMessage;
import util.AllThreadUtil;
import com.qr.log.IntelligentLogger;
import util.LocalIp;

import java.util.function.Function;

import static socket.config.CharsetConfig.EXIT;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum XqyOutOrderImpl implements CmdOutOrder, IntelligentLogger {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    private AllThreadUtil.Key key;
    private String host;
    private int farPort;
    private ServerSocketInMessageQueue server;
    private ClientSocketMessageSend send = new ClientSocketMessageSend();

    @Override
    public String getNameSpace() {
        return "xqy";
    }

    @Override
    public void install(Function<String, String> getString) throws Throwable {
        print(() -> "欢迎使用小蚯蚓聊天工具");
        INSTANCE.host = getString.apply("请确认对方ip").trim();
        if (EXIT.equals(INSTANCE.host.trim())) {
            throw new Throwable("终止操作");
        }
        INSTANCE.farPort = CmdBoot.getInt(getString, "请确认对方端口");
        while (true) {
            try {
                INSTANCE.server = ServerSocketInMessageQueue.getServer(CmdBoot.getInt(getString, "请确认小蚯蚓信息接收端口"));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        print(() -> "小蚯蚓信息接收端口成功打开" + INSTANCE.server.getPort());
        INSTANCE.send.start();
        INSTANCE.key = AllThreadUtil.whileTrueThread(this::useMessage, 100);
        print(() -> "使用愉快，输入 xqy exit 退出，输入help查看完整指令");
    }

    @Override
    public boolean useOrder(String order) throws Throwable {
        sendMessage(order);
        return true;
    }

    @Override
    public void shutDown() {
        AllThreadUtil.stop(key);
        key = null;
        send.shutDown();
        if (server != null) {
            server.shutdown("小蚯蚓聊天退出成功，欢迎下次使用");
        }
    }

    private void sendMessage(String message) {
        String xqyMessage = JSON.toJSONString(new XqytpMessage(message, LocalIp.getIP(), server.getPort()));
        send.addMessage(xqyMessage, host, farPort);
    }

    private boolean useMessage() {
        String message = server.nextMessage();
        if (message != null) {
            XqytpMessage xqytpMessage = JSON.parseObject(message, XqytpMessage.class);
            CmdBoot.cmdPrintln(xqytpMessage);
            return false;
        }
        return true;
    }

    @Override
    public boolean isStart() {
        return key != null && key.isRun();
    }
}
