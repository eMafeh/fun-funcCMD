package socket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketFileClient {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("请输入文件路径");
        String path = sc.nextLine().replaceAll("\\\\","\\\\\\\\");
        Socket socket = new Socket("10.39.14.192", 4044);
        byte[] b = new byte[1 << 20];
        try (OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(path)) {
            int read;
            while ((read = fileInputStream.read(b)) > -1)
                outputStream.write(b, 0, read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
