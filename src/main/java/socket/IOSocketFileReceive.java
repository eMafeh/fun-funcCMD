package socket;

import socket.model.DirectoryModel;
import socket.model.NewFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IOSocketFileReceive {
    //根据目录对象和目标路径生成空文件目录，返回空文件的File和实际文件的总长度记录的list
    public static FileList buildDirectoryFile(DirectoryModel directoryModel, File file) {
        return buildDirectoryFile(directoryModel, file.getPath());
    }

    //根据目录对象和目标路径生成空文件目录，返回空文件的File和实际文件的总长度记录的list
    public static FileList buildDirectoryFile(DirectoryModel directoryModel, String path) {
        FileList filelist = new FileList();
//        path=path.endsWith()
        directoryModel.setName(path + "\\" + directoryModel.getName());
        buildDirectoryModel(directoryModel, filelist.files);
        return filelist;
    }

    //根据目录对象递归生成空文件目录，把空文件的File和实际文件的总长度记录在list中
    private static void buildDirectoryModel(DirectoryModel directoryModel, List<NewFile> lists) {
        String name = directoryModel.getName();
        //参数有效性判断
        if (name == null || name.length() == 0)
            throw new NullPointerException();
        new File(name).mkdir();//文件夹生成

        //文件生成记录
        directoryModel.getFiles().forEach(a -> {
            NewFile newFile = new NewFile();
            File file = new File(name + "\\" + a.getName());
            try {
                file.createNewFile();//无所谓文件是否是新建的，后续还有操作
            } catch (IOException e) {
                //这个异常在请求文件无法生成的时候发生，添加这个文件也没有意义了
                e.printStackTrace();
                return;
            }

            //文件队列追加结果
            newFile.setFile(file);
            newFile.setLength(a.getLength());
            lists.add(newFile);
        });

        //递归文件夹
        if (directoryModel.getDirectorys() != null)
            directoryModel.getDirectorys().forEach(a -> {
                a.setName(name + "\\" + a.getName());
                buildDirectoryModel(a, lists);
            });
    }

}
