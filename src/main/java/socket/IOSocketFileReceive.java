package socket;

import socket.messagebuild.Good_PathBuilder;
import socket.model.DirectoryModel;
import socket.model.FileList;
import socket.model.IODirectoryModelPackage;
import socket.model.NewFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IOSocketFileReceive {
    /**
     * 追加远程文件的位置信息
     * @param ioDirectoryModelPackage
     * @param file
     * @return
     */
    public static void buildIODirectory(FileList filelist ,IODirectoryModelPackage ioDirectoryModelPackage, File file) {
        buildDirectoryFile(filelist,ioDirectoryModelPackage.getDirectoryModel(), file);
        filelist.setSourceIp(ioDirectoryModelPackage.getIp());
        filelist.setSourcePath(ioDirectoryModelPackage.getPath());
        filelist.setGetport(ioDirectoryModelPackage.getGetport());
    }

    /**
     * 根据目录对象和目标路径生成空文件目录，返回空文件的File和实际文件的总长度记录的list
     * 目标路径如果不是文件夹，抛出异常
     * 目标路径是文件夹，则在该文件夹下生成空文件，记录所有尝试生成的文件
     * 返回的list中包含这些文件，包含远程文件的长度信息
     */
    private static void buildDirectoryFile(FileList filelist ,DirectoryModel directoryModel, File file) {
        if (file.isFile()) throw new IllegalArgumentException(file.getPath() + "路径是一个文件");
        if(!file.exists())file.mkdirs();
        buildDirectoryModel(file.getPath(), directoryModel, filelist.getFiles());
    }

    //根据目录对象递归生成空文件目录，把空文件的File和实际文件的总长度、文件的相对路径记录在list中
    private static void buildDirectoryModel(String path, DirectoryModel directoryModel, List<NewFile> lists) {
        String directoryName = directoryModel.getName();
        String localDirectory = Good_PathBuilder.addFileName(path, directoryName);
        new File(localDirectory).mkdirs();//文件夹生成
        //文件生成记录
        directoryModel.getFiles().forEach(a -> {
            NewFile newFile = new NewFile();
            File file = new File(Good_PathBuilder.addFileName(localDirectory, a.getName()));
            try {
                file.createNewFile();//无所谓文件是否是新建的，后续还有操作
            } catch (IOException e) {
                //这个异常在请求文件无法生成的时候发生，添加这个文件也没有意义了
                e.printStackTrace();
                return;
            }
            //文件队列追加结果
            newFile.setRemoteName(Good_PathBuilder.addFileName(directoryName, a.getName()));
            newFile.setFile(file);
            newFile.setLength(a.getLength());
            lists.add(newFile);
        });
        //递归文件夹
        if (directoryModel.getDirectorys() != null)
            directoryModel.getDirectorys().forEach(a -> {
                a.setName(Good_PathBuilder.addFileName(directoryName, a.getName()));
                buildDirectoryModel(path, a, lists);
            });
    }

}
