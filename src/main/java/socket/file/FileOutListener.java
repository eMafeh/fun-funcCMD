package socket.file;

import com.qr.core.CmdBoot;
import com.qr.order.FileOutOrderImpl;
import socket.core.ServerSocketInMessageQueue;
import socket.file.model.morefile.IoDirectoryModelPackage;
import socket.file.model.simglefile.WantFile;
import util.AllThreadUtil;
import util.BeanToMap;

import java.io.*;
import java.net.Socket;
import java.util.function.BiFunction;

/**
 * 2017/9/8  9:08
 *
 * @author qianrui
 */
public class FileOutListener {
    private static BiFunction<String, Integer, String[]> split;
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
                int inPort = CmdBoot.getInt(CmdBoot::getString, "请输入文件服务监听端口");
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
            String json = server.nextMessage();
            if (json == null) {
                return true;
            }
            String[] split = FileOutListener.split.apply(json, 1);
            if (split[0].equals(FileOutOrderImpl.ALL_FILE)) {
                IoDirectoryModelPackage filePackage = BeanToMap.getBean(split[1], IoDirectoryModelPackage.class);
                if (filePackage != null) {
                    FileInListener.listenForFile(filePackage, FileOutOrderImpl.INSTANCE.downLoadPath);
                    return false;
                }
            } else if (split[0].equals(FileOutOrderImpl.WANT_FILE)) {
                WantFile file = BeanToMap.getBean(split[1], WantFile.class);
                if (file != null) {
                    giveFile(file);
                    return false;
                }
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
        if (server != null) {
            server.shutdown("文件服务器退出成功");
        }
        AllThreadUtil.stop(key);
    }

}
