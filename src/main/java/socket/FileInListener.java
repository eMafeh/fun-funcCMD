package socket;

import socket.core.CmdMessageController;
import socket.model.CountNumValue;
import socket.model.FileList;
import socket.model.IODirectoryModelPackage;
import socket.model.NewFile;
import util.LoopThread;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by snb on 2017/9/8  9:08
 */
public class FileInListener {
    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        numberFormat.setMaximumFractionDigits(2);
    }

    public static void listenForFile(IODirectoryModelPackage filePackage, File directory) {
        if (filePackage == null) return;

        FileList fileList = new FileList();
        //方法结束后会包含所有的文件信息和远程地址信息，需要后续的线程自动处理
        fileList.setMessageByIo(filePackage);

        List<NewFile> files = fileList.getFiles();
        double fileSize = filePackage.getFileSize();

        LoopThread.TankKey tankKey = showFileNow(files, fileSize);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            IOSocketFileReceive.buildIODirectory(fileList, filePackage, directory);
            CmdMessageController.cmdprintln("文件生成完毕");
            LoopThread.getLoopThread().removeLoopTank(tankKey);
        });
        executorService.shutdown();
    }

    private static LoopThread.TankKey showFileNow(List<NewFile> files, double fileSize) {
        CountNumValue<Double> c = new CountNumValue<>(0D);
        return LoopThread.getLoopThread().addLoopTankByTenofOneSecond(() -> {
            if ((double) files.size() * 100 / fileSize > c.i + 1) {
                c.i = (double) files.size() * 100 / fileSize;
                CmdMessageController.cmdprintln(files.size() + " 个空文件（" + numberFormat.format(c.i) + "%）已经生成");
            }
        }, 3, 0);
    }
}
