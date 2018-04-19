package util;

public class ExceptionUtil {
    static String deepMessage(Throwable e) {
        if (e.getCause() != null) {
            return deepMessage(e.getCause());
        }
        return e.getMessage();
    }
}
