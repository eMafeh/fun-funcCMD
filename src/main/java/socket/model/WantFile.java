package socket.model;

/**
 * @author kelaite
 * 2018/2/3
 */
public class WantFile {
    private String path;
    private String wantIp;
    private long beginLength;
    private int port;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWantIp() {
        return wantIp;
    }

    public void setWantIp(String wantIp) {
        this.wantIp = wantIp;
    }

    public long getBeginLength() {
        return beginLength;
    }

    public void setBeginLength(long beginLength) {
        this.beginLength = beginLength;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "WantFile{" + "path='" + path + '\'' + ", wantIp='" + wantIp + '\'' + ", beginLength=" + beginLength + ", port=" + port + '}';
    }
}
