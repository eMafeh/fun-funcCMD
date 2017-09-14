package socket;

import socket.config.IOPortConfig;
import socket.model.NewFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SocketFile {
    private static final Scanner SC = new Scanner(System.in);
    private static final ServerSocket serverSocket = ServerSocketFactory.getServerSocket(IOPortConfig.GETPORT);


    public static void main(String[] args) throws IOException {
        System.out.println("请输入路径");
        String path = SC.nextLine();
        getFileInt(new NewFile(new File(path)),false);

    }


    //接受文件流,成功返回true，失败返回false
    public static boolean getFileInt(NewFile newFile, boolean append) throws IOException {
        File path = newFile.getFile();
        long fulength = newFile.getLength();
        long nowlength = append ? path.length() : 0;
        if (append && fulength <= nowlength) return true;
        Socket socket = serverSocket.accept();
        try (FileOutputStream fileOutputStream = new FileOutputStream(path, append); InputStream inputStream = socket.getInputStream()) {
            byte[] b = new byte[1 << 16];
            int read;
            if (fulength > 0)
                while (nowlength < fulength) {
                    read = inputStream.read(b);
                    fileOutputStream.write(b, 0, read);
                    nowlength += read;
                }
            else while ((read = inputStream.read(b)) > -1)
                fileOutputStream.write(b, 0, read);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //返回文件集合
    public static List<File> getAllFileList(String path) {
        File file = new File(path);
        List<File> list = new ArrayList<>();
        getAllFileList(file, list);
        return list;
    }

    //根据文件目录，返回文件集合
    private static void getAllFileList(File file, List<File> list) {
        list.add(file);
        File[] files;
        if (file.isDirectory() && (files = file.listFiles()) != null)
            Arrays.stream(files).forEach(a -> getAllFileList(a, list));
    }

    //根据路径，返回该路径下的文件目录模型


    public static void messageOrder(String message) {

    }


}
