package socket.file.model.simglefile;


import socket.file.model.morefile.IoDirectoryModelPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于记录本地需要接受的新文件
 * @author qianrui
 */
public class FileList {
    private List<NewFile> files = new ArrayList<>();
    private String sourcePath;
    private String sourceIp;
    private int noticePort;

    public void setMessageByIo(IoDirectoryModelPackage filePackage) {
        noticePort = filePackage.getNoticePort();
        sourcePath = filePackage.getPath();
        sourceIp = filePackage.getIp();
    }

    public int getNoticePort() {
        return noticePort;
    }

    public void setNoticePort(int noticePort) {
        this.noticePort = noticePort;
    }

    public List<NewFile> getFiles() {
        return files;
    }

    public void setFiles(List<NewFile> files) {
        this.files = files;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Override
    public String toString() {
        return "FileList{" +
                "files=" + files +
                ", sourcePath='" + sourcePath + '\'' +
                ", sourceIp='" + sourceIp + '\'' +
                ", noticePort=" + noticePort +
                '}';
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}
