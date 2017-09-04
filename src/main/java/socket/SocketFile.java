package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SocketFile {
    private static final Scanner SC = new Scanner(System.in);

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
        getFileInt();
    }

    public static void getFileInt() throws IOException {
        String path = "D:\\b.zip";
        ServerSocket serverSocket = new ServerSocket(4044);
        Socket socket = serverSocket.accept();
        byte[] b = new byte[1 << 20];
        try (InputStream inputStream = socket.getInputStream(); FileOutputStream fileOutputStream = new FileOutputStream(path, true)) {
            int read;
            while ((read = inputStream.read(b)) > -1) {
//                System.out.println(new String(b, 0, read, "GBK"));
                fileOutputStream.write(b, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<File> getAllFileList(String path) {
        File file = new File(path);
        List<File> list = new ArrayList<>();
        getAllFileList(file, list);
        return list;
    }

    private static void getAllFileList(File file, List<File> list) {
        list.add(file);
        File[] files;
        if (file.isDirectory() && (files = file.listFiles()) != null)
            Arrays.stream(files).forEach(a -> getAllFileList(a, list));
    }

    public static DirectoryModel getFile(String path) {
        File file = new File(path);
        DirectoryModel directoryModel = new DirectoryModel();
        //文件，操作然后返回
        if (!file.isDirectory()) {
            directoryModel.name = file.getParent();
            directoryModel.files.add(new FileModel(file));
            return directoryModel;
        }
        buildDirectoryModel(directoryModel, file);
        return directoryModel;
    }

    private static void buildDirectoryModel(DirectoryModel directoryModel, File file) {
        //目录，操作准备递归
        directoryModel.name = file.getName();
        File[] files = file.listFiles();
        if (files != null)
            Arrays.stream(files).forEach(a -> {
                if (a.isDirectory()) {
                    DirectoryModel directory = new DirectoryModel();
                    directoryModel.directorys.add(directory);
                    buildDirectoryModel(directory, a);
                } else {
                    directoryModel.files.add(new FileModel(a));
                }
            });
    }

    public static void messageOrder(String message) {

    }

    public static List<NewFile> buildDirectoryFile(DirectoryModel directoryModel, File file) {
        directoryModel.name = file.getPath() + "\\" + directoryModel.name;
        List<NewFile> list = new ArrayList<>();
        buildDirectoryModel(directoryModel, list);
        return list;
    }

    public static List<NewFile> buildDirectoryFile(DirectoryModel directoryModel, String path) {
        directoryModel.name = path + "\\" + directoryModel.name;
        List<NewFile> list = new ArrayList<>();
        buildDirectoryModel(directoryModel, list);
        return list;
    }

    private static void buildDirectoryModel(DirectoryModel directoryModel, List<NewFile> lists) {
        String name = directoryModel.name;
        directoryModel.files.forEach(a -> {
            NewFile newFile = new NewFile();
            File file = new File(name + "\\" + a.name);
            try {
                if(!file.createNewFile()&&file.length()>=a.length)
                    return;
            } catch (IOException e) {
                return;
            }

            newFile.file = file;
            newFile.length = a.length;
            lists.add(newFile);
        });

        directoryModel.directorys.forEach(a->{
            a.name=name+"\\";
        });
    }

}
