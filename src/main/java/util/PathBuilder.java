package util;

import java.io.File;

/**
 * @author QianRui(Mafeh)
 */
public class PathBuilder {
    private static final int LESS = 2;
    private static final char[] C = {'/', '\\'};

    /**
     * get a file Parent path.if it do not have Parent,return "";
     */
    public static String getFromPath(File file) {
        if (file == null) {
            return null;
        }
        char[] chars = file.getPath().toCharArray();

        //if path.length <2 .that mean it cannot have a fromPath
        if (chars.length < LESS) {
            return "";
        }

        //get the last \ or / ,sub it and return
        for (int i = chars.length - LESS; i >= 0; i--) {
            if (chars[i] == '\\' || chars[i] == '/') {
                return new String(chars, 0, i);
            }
        }

        //if do not have \ or /,that mean it do not have fromPath
        return "";
    }

    /**
     * use filename and directorypath ,bulid a fix path,never return null
     *
     * @return never null
     */
    public static String addFileName(String path, String name) {
        //when file name is null,will return this directoryPath
        if (name == null) {
            name = "";
        }

        //when directory is null,will return file or just ""
        if (path == null || "".equals(path)) {
            return name;
        }

        //get directory last char ,whatever directory last char,will return a good path
        char c = path.charAt(path.length() - 1);

        if (c == C[0] || c == C[1]) {
            return path + name;
        }
        return path + C[0] + name;
    }
}
