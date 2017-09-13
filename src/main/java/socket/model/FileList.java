package socket.model;


import java.util.ArrayList;
import java.util.List;

/**
 * 用于记录本地需要接受的新文件
 */
public class FileList {
    private List<NewFile> files = new ArrayList<>();
    private String sourcePath;
    private String sourceIp;
    private int getport;

    public void setMessageByIo(IODirectoryModelPackage filePackage) {
        getport = filePackage.getGetport();
        sourcePath = filePackage.getPath();
        sourceIp = filePackage.getIp();
    }

    public int getGetport() {
        return getport;
    }

    public void setGetport(int getport) {
        this.getport = getport;
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
                ", getport=" + getport +
                '}';
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}
