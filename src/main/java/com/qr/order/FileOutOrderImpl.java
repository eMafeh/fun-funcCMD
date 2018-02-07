package com.qr.order;

import com.alibaba.fastjson.JSON;
import com.qr.core.CmdBoot;
import socket.core.ClientSocketMessageSend;
import socket.file.FileOutListener;
import socket.file.messagebuild.IoFilePackageBuilder;
import socket.file.model.morefile.IoDirectoryModelPackage;
import socket.file.model.simglefile.WantFile;

import java.io.File;
import java.util.function.Function;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum FileOutOrderImpl implements CmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    private static final String EXIT = "exit";
    public static final String ALL_FILE = "A";
    public static final String WANT_FILE = "W";
    public File downLoadPath = new File("E:\\XqlDownload");
    String farHost;
    int farPort;
    private ClientSocketMessageSend send = new ClientSocketMessageSend();

    @Override
    public String getNameSpace() {
        return "file";
    }

    @Override
    public void install(Function<String, String> getString) throws Throwable {
        farPort = CmdBoot.getInt(getString, "请确认对方端口");
        farHost = getString.apply("请确认对方ip").trim();
        if (EXIT.equals(farHost.trim())) {
            throw new Throwable("终止操作");
        }
        FileOutListener.init();
        send.start();
    }

    @Override
    public void useOrder(String order) throws Throwable {
        if (!send.isalive()) {
            System.out.println("file server is not install");
            return;
        }
        if (EXIT.equals(order)) {
            shutDown();
            return;
        }
        IoDirectoryModelPackage fileModel = IoFilePackageBuilder.getFileModel(order);
        if (fileModel != null) {
            send.addMessage(ALL_FILE + JSON.toJSONString(fileModel), farHost, farPort);
        }
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

}
