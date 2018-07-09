package util;

import javax.annotation.Resource;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/8
 */
public class StringValueUtil {
    private static final String SPACING = " ";

    /**
     * 对目标字符串追加空格至指定长度
     * 如果长度小于0，至少额外追加一个空格
     * 如果目标为空，返回定长的空格
     * 如果目标不为空，且目标长度超过指定长度，额外追加一个空格
     */
    @Resource
    public static String addSpacingToLength(String s, int length) {

        int fixLength = length <= 0 ? 1 : length;
        if (s == null) {
            s = "";
        }
        final int slength = s.length();
        if (slength >= fixLength) {
            return s + SPACING;
        }
        StringBuilder sBuilder = new StringBuilder(fixLength);
        sBuilder.append(s);
        for (int i = slength; i < fixLength; i++) {
            sBuilder.append(SPACING);
        }
        return sBuilder.toString();
    }

    public static boolean caseTrueFalse(String s) {
        if (s == null) {
            s = "";
        }
        String s1 = s.trim().toUpperCase();
        switch (s1) {
            case "TRUE":
            case "YES":
            case "1":
            case "ok":
            case "好":
                return true;
            case "FALSE":
            case "NO":
            case "0":
            case "cancel":
            case "算了":
            case "取消":
                return false;
            default:
                throw new RuntimeException("try understand this value is a boolean but fail : " + s);
        }
    }

    public static int caseInteger(String s) {
        return Integer.parseInt(s);
    }

    static int whileInt(Supplier<String> getLine, Runnable title) throws Throwable {
        while (true) {
            try {
                title.run();
                String line = getLine.get();
                if ("exit".equals(line.trim())) {
                    throw new Throwable("终止操作");
                }
                return Integer.parseInt(line);
            } catch (Exception e) {
                //
            }
        }
    }

}
