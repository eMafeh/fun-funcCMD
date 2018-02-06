package socket.laboratory;


import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static develop.show.ShowObject.noParamPublicStaticMethod;

/**
 * @author kelaite
 * 2018/2/6
 */
public class InetAddressTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UnknownHostException {
        noParamPublicStaticMethod(InetAddress.class);
        byte[] b = {10,39,14, (byte) 33};
        final InetAddress byAddress = InetAddress.getByAddress(b);
        System.out.println(byAddress);
    }

}
