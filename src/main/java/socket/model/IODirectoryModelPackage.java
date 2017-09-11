package socket.model;

/**
 * 用于描述一个主动网络传输的文件夹
 */
public class IODirectoryModelPackage {
    private String ip = Good_LocalIP.getIP();
    private int getport;
    private String path;
    private DirectoryModel directoryModel;
    private int fileSize;
    private int directorySize;
    private long length;

    public IODirectoryModelPackage( int getport, String path, DirectoryModel directoryModel, int fileSize, int directorySize, long length) {
        this.getport = getport;
        this.path = path;
        this.directoryModel = directoryModel;
        this.fileSize = fileSize;
        this.directorySize = directorySize;
        this.length = length;
    }

    public IODirectoryModelPackage() {

    }

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


    @Override
    public String toString() {
        return "IODirectoryModelPackage{" +
                "ip='" + ip + '\'' +
                ", getport=" + getport +
                ", path='" + path + '\'' +
                ", fileSize=" + fileSize +
                ", directorySize=" + directorySize +
                ", length=" + length +
                '}';
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

    public int getGetport() {
        return getport;
    }

    public void setGetport(int getport) {
        this.getport = getport;
    }

}
