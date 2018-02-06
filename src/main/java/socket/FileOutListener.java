package socket;

import socket.core.CmdMessageController;
import socket.core.ServerSocketInMessageQueue;
import socket.model.WantFile;
import util.AllThreadUtil;
import util.BeanToMap;

import java.io.*;
import java.net.Socket;

/**
 * Created by snb on 2017/9/8  9:08
 */
public class FileOutListener {
    private static ServerSocketInMessageQueue server;
    private static int listenPort;
    private static AllThreadUtil.Key key;

    public static int getListenPort() {
        return listenPort;
    }

    private static ServerSocketInMessageQueue getFileServer() {
        ServerSocketInMessageQueue fileServer;
        while (true) {
            try {
                int inPort = CmdMessageController.getInt("请输入文件服务监听端口");
                fileServer = ServerSocketInMessageQueue.getServer(inPort);
                System.out.println("启动文件服务器");
                return fileServer;
            } catch (Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        }
    }

    public static void init() {
        server = getFileServer();

        //先完成文件监听
        FileOutListener.listenPort = server.getPort();
        key = AllThreadUtil.whileTrueThread(() -> {
            String json;
            WantFile file;
            if ((json = server.nextMessage()) != null && (file = BeanToMap.getBean(json, WantFile.class)) != null) {
                giveFile(file);
                return false;
            }
            return true;
        }, 100);
    }

    private static void giveFile(WantFile file) {
        String path = file.getPath();
        File localFile = new File(path);
        long length = localFile.length();
        long beginLength = file.getBeginLength();
        byte[] bytes = new byte[1 << 16];
        long nowLength = 0L;
        int re;

        try (Socket socket = new Socket(file.getWantIp(), file.getPort()); OutputStream outputStream = socket.getOutputStream(); InputStream inputStream = new FileInputStream(localFile)) {
            while (nowLength != length && (re = inputStream.read(bytes)) > -1) {
                if (nowLength > beginLength) {
                    outputStream.write(bytes, 0, re);
                    nowLength += re;
                    continue;
                }
                nowLength += re;
                if (nowLength < beginLength) {
                    continue;
                }
                outputStream.write(bytes, (int) (re - nowLength + beginLength), (int) (nowLength - beginLength));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shutDown() {
        server.shutdown("文件服务器退出成功");
        AllThreadUtil.stop(key);
    }

}
