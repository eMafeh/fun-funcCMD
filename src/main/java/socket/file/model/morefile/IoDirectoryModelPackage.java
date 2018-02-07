package socket.file.model.morefile;


/**
 * 用于描述一个主动网络传输的文件夹
 *
 * @author qianrui
 */
public class IoDirectoryModelPackage {
    private String ip;

    private int noticePort;
    private String path;
    private DirectoryModel directoryModel;
    private int fileSize;
    private int directorySize;
    private long length;

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getDirectorySize() {
        return directorySize;
    }

    public void setDirectorySize(int directorySize) {
        this.directorySize = directorySize;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public DirectoryModel getDirectoryModel() {
        return directoryModel;
    }

    public void setDirectoryModel(DirectoryModel directoryModel) {
        this.directoryModel = directoryModel;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNoticePort() {
        return noticePort;
    }

    public void setNoticePort(int noticePort) {
        this.noticePort = noticePort;
    }

    @Override
    public String toString() {
        return "IoDirectoryModelPackage{" + "ip='" + ip + '\'' + ", noticePort=" + noticePort + ", path='" + path + '\'' + ", fileSize=" + fileSize + ", directorySize=" + directorySize + ", length=" + length + '}';
    }
}
