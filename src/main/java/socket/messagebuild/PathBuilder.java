package socket.messagebuild;

import java.io.File;

public class PathBuilder {
    public static String getFromPath(File file) {
        char[] chars = file.getPath().toCharArray();
        if (chars.length < 2) return "";
        for (int i = chars.length - 2; i >= 0; i--)
            if (chars[i] == '\\' || chars[i] == '/') {
                return new String(chars, 0, i);
            }
        return "";
    }

    public static String addFileName(String path,String name){
        return null;
    }
}
