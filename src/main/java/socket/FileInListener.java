package socket;

import socket.core.CmdMessageController;
import socket.model.FileList;
import socket.model.IODirectoryModelPackage;

import java.io.File;

/**
 * Created by snb on 2017/9/8  9:08
 */
public class FileInListener {
    public static void listenForFile(IODirectoryModelPackage filePackage, File directory) {
        if (filePackage == null) return;
        FileList fileList = IOSocketFileReceive.buildIODirectory(filePackage, directory);
        fileList.getFiles().forEach(CmdMessageController::cmdprintln);

    }
}
