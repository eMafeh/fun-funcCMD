package socket;

import socket.config.IOPortConfig;
import socket.messagebuild.Good_PathBuilder;
import socket.model.DirectoryModel;
import socket.model.FileModel;
import socket.model.IODirectoryModelPackage;

import java.io.File;
import java.util.Arrays;

/**
 * 这个类基本不用改了
 */
public class IOSocketFileSend {
    /**
     * 传入了一个文件（文件夹）的路径
     * 根据该路径，判断是否是个文件，如果是个文件，返回的目录模型，目录名是null，子文件夹是空的，文件里有一个文件模型
     * 如果不是文件，递归这个路径，返回的目录模型里，全部都是名称，最高级的目录是当前路径的名字
     * 将这个文件夹模型包装为网络包，返回的网络包中表明网络上找到这些文件需要的全部信息
     *
     * @param file
     * @return
     */
    public static IODirectoryModelPackage getFileModel(File file) {
        String path = file.getPath();
        DirectoryModel directoryModel = new DirectoryModel();
        if (!file.isDirectory())
            directoryModel.getFiles().add(new FileModel(file));
        else
            buildDirectoryModel(directoryModel, file);
        return buildIOPackage(directoryModel, file);
    }

    public static IODirectoryModelPackage getFileModel(String path) {
        return getFileModel(new File(path));
    }

    //包装成网络格式返回
    private static IODirectoryModelPackage buildIOPackage(DirectoryModel directoryModel, File file) {
        IODirectoryModelPackage ioDirectoryModelPackage = new IODirectoryModelPackage();
        ioDirectoryModelPackage.setDirectoryModel(directoryModel);
        ioDirectoryModelPackage.setGetport(IOPortConfig.GETPORT.getPort());
        ioDirectoryModelPackage.setPath(Good_PathBuilder.getFromPath(file));
        return ioDirectoryModelPackage;
    }

    //对一个设定的目录模型，根据路径递归填充文件信息
    private static void buildDirectoryModel(DirectoryModel directoryModel, File file) {
        //目录，操作准备递归
        directoryModel.setName(file.getName());
        File[] files = file.listFiles();
        if (files != null)
            Arrays.stream(files).forEach(a -> {
                if (a.isDirectory()) {
                    DirectoryModel directory = new DirectoryModel();
                    directoryModel.getDirectorys().add(directory);
                    buildDirectoryModel(directory, a);
                } else {
                    directoryModel.getFiles().add(new FileModel(a));
                }
            });
    }

}
