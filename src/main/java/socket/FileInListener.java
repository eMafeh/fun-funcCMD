package socket;

import socket.core.CmdMessageController;
import socket.model.CountNumValue;
import socket.model.FileList;
import socket.model.IODirectoryModelPackage;
import socket.model.NewFile;
import util.LoopThread;
import util.TankKey;

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
        List<NewFile> files = fileList.getFiles();
        double fileSize = filePackage.getFileSize();

        TankKey tankKey = showFileNow(files, fileSize);

        Executors.newSingleThreadExecutor().submit(() -> {
            IOSocketFileReceive.buildIODirectory(fileList, filePackage, directory);
            LoopThread.getLoopThread().removeLoopTank(tankKey);
        });
    }

    private static TankKey showFileNow(List<NewFile> files, double fileSize) {
        CountNumValue<Double> c = new CountNumValue<>(0D);
        return CmdMessageController.isNoSilent() ? LoopThread.getLoopThread().addLoopTankByTenofOneSecond(() -> {
            if ((double) files.size() * 100 / fileSize > c.i + 1) {
                c.i = (double) files.size() * 100 / fileSize;
                CmdMessageController.cmdprintln(files.size() + " 个空文件（" + numberFormat.format(c.i) + "%）已经生成");
            }
        }, 3, 0) : null;
    }
}
