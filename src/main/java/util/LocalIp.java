package util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 动态获取本机的inet4ip地址
 * @author qianrui
 */
public class LocalIp {
    private static String IP;

    static {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            //
        }
        InetAddress ipadd;
        assert networkInterfaces != null;
        m:
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                ipadd = inetAddresses.nextElement();
                if (ipadd != null && ipadd instanceof Inet4Address) {
                    IP = ipadd.getHostAddress();
                    if (IP != null && !"127.0.0.1".equals(IP)) {
                        break m;
                    }
                }
            }
        }

    }

    public static String getIP() {
        return IP;
    }
}
