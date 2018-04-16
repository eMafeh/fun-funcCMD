package com.qr.order;

import com.alibaba.fastjson.JSON;
import com.qr.core.CmdOutCommand;
import socket.core.ClientSocketMessageSend;
import socket.core.ServerSocketInMessageQueue;
import socket.file.FileOutListener;
import socket.file.messagebuild.IoFilePackageBuilder;
import socket.file.model.morefile.IoDirectoryModelPackage;
import socket.file.model.simglefile.WantFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum FileOutCommandImpl implements CmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    @Resource
    private BiFunction<Supplier<String>, Runnable, Integer> whileInt;
    @Resource
    private Function<Throwable, String> deepMessage;
    private static final String EXIT = "exit";
    public static final String ALL_FILE = "A";
    public static final String WANT_FILE = "W";
    public File downLoadPath = new File("D:\\XqlDownload");
    String farHost;
    int farPort;
    private ClientSocketMessageSend send = new ClientSocketMessageSend();

    @Override
    public String getNameSpace() {
        return "file";
    }

    @Override
    public void install(Supplier<String> getLine) throws Throwable {
        farPort = whileInt.apply(getLine, () -> print("error", () -> "请确认对方端口"));
        print("error", () -> "请确认对方ip");
        farHost = getLine.get().trim();
        ServerSocketInMessageQueue fileServer;
        while (true) {
            try {
                int inPort = whileInt.apply(getLine, () -> print("error", () -> "请输入文件服务监听端口"));
                fileServer = ServerSocketInMessageQueue.getServer(inPort);
                print("error", () -> "启动文件服务器");
                break;
            } catch (Throwable throwable) {
                print("error", () -> deepMessage.apply(throwable));
            }
        }
        FileOutListener.init(fileServer);
        send.start();
    }

    @Override
    public boolean useCommand(String order) throws Throwable {
        IoDirectoryModelPackage fileModel = IoFilePackageBuilder.getFileModel(order);
        if (fileModel != null) {
            send.addMessage(ALL_FILE + JSON.toJSONString(fileModel), farHost, farPort);
            return true;
        }
        print(() -> " file is not found" + order);
        return true;
    }

    public void wantFile(WantFile wantFile, String sourceIp, int noticePort) {
        if (!send.isalive()) {
            return;
        }
        if (wantFile != null) {
            send.addMessage(WANT_FILE + JSON.toJSONString(wantFile), sourceIp, noticePort);
        }
    }

    @Override
    public void shutDown() {
        send.shutDown();
        FileOutListener.shutDown();
    }

    @Override
    public boolean isStart() {
        return send.isalive();
    }
}
