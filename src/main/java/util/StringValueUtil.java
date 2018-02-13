package util;

/**
 * @author kelaite
 * 2018/2/8
 */
public class StringValueUtil {
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
