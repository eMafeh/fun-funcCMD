package socket.file;

import com.qr.core.CmdBoot;
import socket.file.messagebuild.IoFilePackageReceiveToLocal;
import socket.file.model.simglefile.FileList;
import socket.file.model.morefile.IoDirectoryModelPackage;
import socket.file.model.simglefile.NewFile;
import util.LoopThread;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2017/9/8  9:08
 *
 * @author qianrui
 */
public class FileInListener {
    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        numberFormat.setMaximumFractionDigits(2);
    }

    public static void listenForFile(IoDirectoryModelPackage filePackage, File directory) {
        if (filePackage == null) {
            throw new NullPointerException();
        }
        System.out.println(filePackage);
        FileList fileList = new FileList();
        //方法结束后会包含所有的文件信息和远程地址信息，需要后续的线程自动处理
        fileList.setMessageByIo(filePackage);

        List<NewFile> files = fileList.getFiles();
        double fileSize = filePackage.getFileSize();

        LoopThread.TankKey tankKey = showFileNow(files, fileSize);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            IoFilePackageReceiveToLocal.buildIODirectory(fileList, filePackage, directory);
            CmdBoot.cmdPrintln("文件生成完毕");
            FileGetter.wantFileList(fileList);
//            LoopThread.getLoopThread().removeLoopTank(tankKey);
        });
        executorService.shutdown();

    }

    private static LoopThread.TankKey showFileNow(List<NewFile> files, double fileSize) {
        Double[] d = {0D};
        LoopThread.TankKey[] key = new LoopThread.TankKey[1];
        key[0] = LoopThread.getLoopThread().addLoopTankByTenofOneSecond(() -> {
            if ((double) files.size() * 100 / fileSize > d[0] + 1) {
                d[0] = (double) files.size() * 100 / fileSize;
                CmdBoot.cmdPrintln(files.size() + " 个文件（" + numberFormat.format(d[0]) + "%）已经生成");
            }
            if (files.size() == fileSize) {
                LoopThread.getLoopThread().removeLoopTank(key[0]);
            }
        }, 3, 0);
        return key[0];
    }
}
