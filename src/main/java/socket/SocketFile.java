package socket;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SocketFile {
    private static final Scanner SC = new Scanner(System.in);

    private static final int PORT = 4044;

    private static final ServerSocket serverSocket = ThreadLocal.withInitial(() -> {
        try {
            return new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }).get();

    public static void main(String[] args) throws IOException {
//        while (true) {
//            System.out.println("输入路径");
//            DirectoryModel file = getFileModel(SC.nextLine().replaceAll("\\\\", "\\\\\\\\"));
//            Object o = JSON.toJSON(file);
//            System.out.println(o);
//        }
//        while (true) {
//            System.out.println("输入路径");
//            getAllFileList(SC.nextLine().replaceAll("\\\\", "\\\\\\\\")).forEach(System.out::println);
//        }
//        getFileInt();
//        DirectoryModel fileModel = getFileModel("D:\\IDES\\apache-maven-3.0.4");
//        buildDirectoryFile(fileModel, "D:\\");
//        System.out.println(new File("G:/c.txt").createNewFile());
        System.out.println(JSON.toJSON(IOSocketFileSend.getFileModel("D:\\a.txt")));
    }


    //接受文件流,成功返回true，失败返回false
    public static boolean getFileInt(NewFile newFile, boolean append) throws IOException {
        File path = newFile.file;
        long fulength = newFile.length;
        long nowlength = append ? path.length() : 0;
        if (append && fulength <= nowlength) return true;
        Socket socket = serverSocket.accept();
        try (FileOutputStream fileOutputStream = new FileOutputStream(path, append); InputStream inputStream = socket.getInputStream()) {
            byte[] b = new byte[1 << 16];
            int read;
            while (nowlength < fulength) {
                read = inputStream.read(b);
                fileOutputStream.write(b, 0, read);
                nowlength += read;
            }
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

    //---------------------------TODO-------------------- 接收方操作
    //根据目录对象和目标路径生成空文件目录，返回空文件的File和实际文件的总长度记录的list
    public static FileList buildDirectoryFile(DirectoryModel directoryModel, File file) {
        return buildDirectoryFile(directoryModel, file.getPath());
    }

    //根据目录对象和目标路径生成空文件目录，返回空文件的File和实际文件的总长度记录的list
    public static FileList buildDirectoryFile(DirectoryModel directoryModel, String path) {
        FileList filelist = new FileList();
        directoryModel.name = path + "\\" + directoryModel.name;
        buildDirectoryModel(directoryModel, filelist.files);
        return filelist;
    }

    //根据目录对象递归生成空文件目录，把空文件的File和实际文件的总长度记录在list中
    private static void buildDirectoryModel(DirectoryModel directoryModel, List<NewFile> lists) {
        String name = directoryModel.name;
        //参数有效性判断
        if (name == null || name.length() == 0)
            throw new NullPointerException();
        new File(name).mkdir();//文件夹生成

        //文件生成记录
        directoryModel.files.forEach(a -> {
            NewFile newFile = new NewFile();
            File file = new File(name + "\\" + a.name);
            try {
                file.createNewFile();//无所谓文件是否是新建的，后续还有操作
            } catch (IOException e) {
                //这个异常在请求文件无法生成的时候发生，添加这个文件也没有意义了
                e.printStackTrace();
                return;
            }

            //文件队列追加结果
            newFile.file = file;
            newFile.length = a.length;
            lists.add(newFile);
        });

        //递归文件夹
        if (directoryModel.directorys != null)
            directoryModel.directorys.forEach(a -> {
                a.name = name + "\\" + a.name;
                buildDirectoryModel(a, lists);
            });
    }

}
