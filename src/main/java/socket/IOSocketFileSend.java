package socket;

import java.io.File;
import java.util.Arrays;

public class IOSocketFileSend {

    private static final int  GETPORT=4045;
    /**
     * 传入了一个文件（文件夹）的路径
     * 根据该路径，判断是否是个文件，如果是个文件，返回的目录模型，目录名是null，子文件夹是空的，文件里有一个文件模型
     * 如果不是文件，递归这个路径，返回的目录模型里，全部都是名称，最高级的目录是当前路径的名字
     * 将这个文件夹模型包装为网络包，返回的网络包中表明网络上找到这些文件需要的全部信息
     *
     * @param path
     * @return
     */
    public static IODirectoryModelPackage getFileModel(String path) {
        File file = new File(path);
        DirectoryModel directoryModel = new DirectoryModel();
        if (!file.isDirectory()) {
            directoryModel.files.add(new FileModel(file));
        }
        else {
            buildDirectoryModel(directoryModel, file);
        }
        //包装成网络格式返回
        IODirectoryModelPackage ioDirectoryModelPackage =new IODirectoryModelPackage();
        ioDirectoryModelPackage.directoryModel=directoryModel;
        ioDirectoryModelPackage.getport=GETPORT;
        ioDirectoryModelPackage.path=path.substring(0,path.length()-file.getName().length());
        return ioDirectoryModelPackage;
    }

    //对一个设定的目录模型，根据路径递归填充文件信息
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

}
