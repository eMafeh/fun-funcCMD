package util;

/**
 * @author kelaite
 * 2018/2/7
 */
public class ClassUtils {
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
