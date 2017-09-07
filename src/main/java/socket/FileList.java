package socket;

import socket.model.NewFile;

import java.util.ArrayList;
import java.util.List;

public class FileList {
    private List<NewFile> files = new ArrayList<>();
    private String sourcePath;
    private String nowPath;
    private String sourceIp;

    @Override
    public String toString() {
        return "FileList{" +
                "files=" + files +
                ", sourcePath='" + sourcePath + '\'' +
                ", nowPath='" + nowPath + '\'' +
                ", sourceIp='" + sourceIp + '\'' +
                '}';
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

    public String getNowPath() {
        return nowPath;
    }

    public void setNowPath(String nowPath) {
        this.nowPath = nowPath;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}
