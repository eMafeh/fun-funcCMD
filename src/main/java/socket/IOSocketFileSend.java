package socket;

import socket.config.IOPortConfig;
import socket.messagebuild.Good_PathBuilder;
import socket.model.CountNumValue;
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
        CountNumValue<Integer> fileSize = new CountNumValue<>(0);
        CountNumValue<Integer> directorySize = new CountNumValue<>(0);
        CountNumValue<Long> allLength = new CountNumValue<>(0L);

        if (!file.exists()) return null;
        DirectoryModel directoryModel = new DirectoryModel();
        if (!file.isDirectory()) {
            directoryModel.getFiles().add(new FileModel(file));
            allLength.i += file.length();
            fileSize.i++;
        } else
            buildDirectoryModel(directoryModel, file, fileSize, directorySize, allLength);
        return buildIOPackage(directoryModel, file, fileSize, directorySize, allLength);
    }

    //包装成网络格式返回
    private static IODirectoryModelPackage buildIOPackage(DirectoryModel directoryModel, File file, CountNumValue<Integer> fileSize, CountNumValue<Integer> directorySize, CountNumValue<Long> allLength) {
        return new IODirectoryModelPackage(IOPortConfig.GETPORT.getPort(), Good_PathBuilder.getFromPath(file), directoryModel, fileSize.i, directorySize.i, allLength.i);
    }

    //对一个设定的目录模型，根据路径递归填充文件信息
    private static void buildDirectoryModel(DirectoryModel directoryModel, File file, CountNumValue<Integer> fileSize, CountNumValue<Integer> directorySize, CountNumValue<Long> allLength) {
        directorySize.i++;
        //目录，操作准备递归
        directoryModel.setName(file.getName().equals("") ? file.getPath().substring(0, 1) : file.getName());
        File[] files = file.listFiles();
        if (files != null)
            Arrays.stream(files).forEach(a -> {
                if (a.isDirectory()) {
                    DirectoryModel directory = new DirectoryModel();
                    directoryModel.getDirectorys().add(directory);
                    buildDirectoryModel(directory, a, fileSize, directorySize, allLength);
                } else {
                    directoryModel.getFiles().add(new FileModel(a));
                    allLength.i += a.length();
                    fileSize.i++;
                }
            });
    }

}
