package util;

import java.io.File;
/**
 * Created by QianRui(Mafeh)
 */
public class Good_PathBuilder {

    /**
     * get a file Parent path.if it do not have Parent,return "";
     *
     * @param file
     * @return
     */
    public static String getFromPath(File file) {
        if (file == null) return null;
        char[] chars = file.getPath().toCharArray();

        //if path.length <2 .that mean it cannot have a fromPath
        if (chars.length < 2) return "";

        //get the last \ or / ,sub it and return
        for (int i = chars.length - 2; i >= 0; i--)
            if (chars[i] == '\\' || chars[i] == '/')
                return new String(chars, 0, i);

        //if do not have \ or /,that mean it do not have fromPath
        return "";
    }

    /**
     * use filename and directorypath ,bulid a fix path,never return null
     * @param path
     * @param name
     * @return never null
     */
    public static String addFileName(String path, String name) {
        //when file name is null,will return this directoryPath
        if (name == null) name = "";

        //when directory is null,will return file or just ""
        if (path == null || path.equals("")) return name;

        //get directory last char ,whatever directory last char,will return a good path
        char c = path.charAt(path.length() - 1);
        if (c == '\\' || c == '/') return path + name;
        return path + "/" + name;
    }
}
