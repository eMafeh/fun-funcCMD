import java.io.*;
import java.net.URL;

/**
 * Created by snb on 2017/9/29  11:16
 */
public class ChangCode {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String taget = getMessage("E:\\data\\test\\src\\test\\java\\test.json");
        System.out.println(new String(taget.getBytes("GBK"),"UTF-8"));
    }
    public static String getMessage(String path) {
        try (InputStream resourceAsStream = new FileInputStream(new File(path))) {
            return getStreamString(resourceAsStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    static String getStreamString(InputStream inputStream, String encode) throws IOException {
        StringBuilder str = new StringBuilder();
        byte[] b = new byte[1 << 20];
        int read;
        if (encode != null)
            while ((read = inputStream.read(b)) > -1)
                str.append(new String(b, 0, read, encode));
        else while ((read = inputStream.read(b)) > -1)
            str.append(new String(b, 0, read));
        return str.toString();
    }
}
