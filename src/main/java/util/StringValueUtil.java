package util;

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

    public static Boolean caseTrueFalse(String s) {
        switch (s) {
            case "true":
            case "TRUE":
                return true;
            case "false":
            case "FALSE":
                return false;
            default:
                return null;
        }
    }

    public static Integer caseInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
