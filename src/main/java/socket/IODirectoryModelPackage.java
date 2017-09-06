package socket;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IODirectoryModelPackage {
    private static String IP;
    private String ip = IP;
    DirectoryModel directoryModel;
    String path;
    int getport;

    public String getIp() {
        return ip;
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

    public static String getIP() {
        return IP;
    }

    static {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
        }
        InetAddress ipadd = null;
        m:
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                ipadd = inetAddresses.nextElement();
                if (ipadd != null && ipadd instanceof Inet4Address) {
                    IP = ipadd.getHostAddress();
                    if (IP != null && !IP.equals("127.0.0.1"))
                        break m;
                }
            }
        }

    }
}
