package socket.model;

/**
 * 用于描述一个主动网络传输的文件夹
 */
public class IODirectoryModelPackage {
    private String ip = Good_LocalIP.getIP();
    private int getport;
    private String path;
    private DirectoryModel directoryModel;

    public String getIp() {
        return ip;
    }


    @Override
    public String toString() {
        return "IODirectoryModelPackage{" +
                "ip='" + ip + '\'' +
                ", directoryModel=" + directoryModel +
                ", path='" + path + '\'' +
                ", getport=" + getport +
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
