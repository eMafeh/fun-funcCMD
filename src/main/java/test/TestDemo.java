package test;

import develop.show.ShowObject;
import util.LocalIp;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author kelaite
 * 2018/2/5
 */
public class TestDemo {
    @Resource
    public static void main(String[] args) {
        init();
        try (final Socket socket = new Socket(LocalIp.getIP(), 4044)) {
            Thread.sleep(1000);
            final byte[] bytes = new byte[1024];
            socket.getOutputStream().write("buhao".getBytes());
            final int read = socket.getInputStream().read(bytes);
            System.out.println(new String(bytes, 0, read));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(4044)) {

                final Socket accept = serverSocket.accept();
                ShowObject.everyNoParamPublicMethod(accept);
                final OutputStream outputStream = accept.getOutputStream();
                final byte[] bytes1 = "nihao".getBytes();
                outputStream.write(bytes1,0,1);
                outputStream.write(bytes1,1,1);
                outputStream.write(bytes1,2,1);
                outputStream.write(bytes1,3,1);
                outputStream.write(bytes1,4,1);
                final byte[] bytes = new byte[1024];
                System.out.println("shuhcu");
                final int read = accept.getInputStream().read(bytes);
                System.out.println(new String(bytes, 0, read));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
