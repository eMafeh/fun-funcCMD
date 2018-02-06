package socket.laboratory;

import java.io.IOException;
import java.net.*;

/**
 * @author kelaite
 * 2018/2/6
 */
public class UdpTest {
    public static void main(String[] args) throws UnknownHostException {
        String nihao = "nihao";
        final byte[] bytes = nihao.getBytes();
        final byte[] b = new byte[1024];
        final DatagramPacket request = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("10.39.14.26"), 4044);
        final DatagramPacket response = new DatagramPacket(b, b.length);
        try  {
            DatagramSocket socket = new DatagramSocket(4044);
            new Thread(() -> {
                try {
                    socket.receive(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final byte[] data = response.getData();
                System.out.println(new String(data));
            }).start();
            Thread.sleep(1000);
            socket.send(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
