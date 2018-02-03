package socket;

import socket.core.ServerSocketInMessageQueue;
import socket.model.WantFile;
import util.BeanToMap;

import java.io.*;
import java.net.Socket;

/**
 * Created by snb on 2017/9/8  9:08
 */
public class FileOutListener {
//    public static void main(String[] args) throws IOException, InterruptedException {
//        init(4344);
//    }

    public static void init(int listenPort, boolean[] flag) {
        ServerSocketInMessageQueue server = ServerSocketInMessageQueue.getServer(listenPort,"启动文件服务器");
        String json;
        WantFile file;
        while (flag[0]) {
            if ((json = server.nextMessage()) == null || (file = BeanToMap.getBean(json, WantFile.class)) == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            giveFile(file);
        }
        server.shutdown("文件监听端口已退出");
    }

    private static void giveFile(WantFile file) {
        String path = file.getPath();
        File localFile = new File(path);
        long length = localFile.length();
        long beginLength = file.getBeginLength();
        byte[] bytes = new byte[1<<16];
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

}
