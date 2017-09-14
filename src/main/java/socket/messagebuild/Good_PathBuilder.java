package socket.messagebuild;

import java.io.File;

public class Good_PathBuilder {
    public static String getFromPath(File file) {
        char[] chars = file.getPath().toCharArray();
        if (chars.length < 2) return "";
        for (int i = chars.length - 2; i >= 0; i--)
            if (chars[i] == '\\' || chars[i] == '/') {
                return new String(chars, 0, i);
            }
        return "";
    }

    public static String addFileName(String path, String name) {//文件夹名字为null的时候，注意判断，文件路径为null的时候，抛异常
        if (name == null) throw new NullPointerException();
        if (path == null) path = "";
        char c = path.charAt(path.length());
        if (c == '\\' || c == '/') return path + name;
        return path + "/" + name;
    }
}
