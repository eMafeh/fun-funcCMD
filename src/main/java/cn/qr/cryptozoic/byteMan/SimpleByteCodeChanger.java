package cn.qr.cryptozoic.byteMan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleByteCodeChanger {
    public static void change(String path, String regex, String append) {
        final File file = new File(path);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            int read = inputStream.read(bytes);
            assert read == file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] regexBytes = regex.getBytes();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(addString(bytes, regexBytes, append.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] addString(byte[] bytes, byte[] regexBytes, byte[] appendBytes) {
        final int index = index(bytes, regexBytes);
        if (index < 0) {
            return bytes;
        }
        final byte[] result = new byte[bytes.length + appendBytes.length];
        System.arraycopy(bytes, 0, result, 0, index + 1);
        System.arraycopy(appendBytes, 0, result, index + 1, appendBytes.length);
        System.arraycopy(bytes, index + 1, result, index + 1 + appendBytes.length, bytes.length - index - 1);
        return result;
    }

    private static int index(byte[] bytes, byte[] regexBytes) {
        for (int i = 0, j = 0; j < bytes.length; j++) {
            if (bytes[j] == regexBytes[i]) {
                i++;
            } else {
                i = 0;
                continue;
            }
            if (i == regexBytes.length) {
                return j - i;
            }
        }
        return -1;
    }
}
