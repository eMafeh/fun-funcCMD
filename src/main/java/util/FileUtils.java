package util;

import java.io.*;

/**
 * @author kelaite
 * 2018/2/7
 */
public class FileUtils {
    public static byte[] getFile(final File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void toFile(File file, byte[] data) throws IOException {
        toFile(file, data, false);
    }

    public static void toFile(File file, byte[] data, boolean append) throws IOException {
        try (OutputStream out = openOutputStream(file, append)) {
            out.write(data);
        }
    }

    public static void toFile(File file, String str) throws IOException {
        toFile(file, str.getBytes(), false);
    }

    public static void toFile(File file, String str, boolean append) throws IOException {
        toFile(file, str.getBytes(), append);
    }

    public static void toFile(String fileName, String str) throws IOException {
        toFile(new File(fileName), str);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            createDirectory(file.getParentFile());
        }
        return new FileOutputStream(file, append);
    }

    private static void createDirectory(final File file) {
        if (!file.exists()) {
            createDirectory(file.getParentFile());
            if (!file.mkdirs()) {
                throw new RuntimeException("Directory '" + file + "' could not be created");
            }
        }
    }


}
