package compile;


import util.PrintlnLogger;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/1
 */
public enum ApplicationHome implements PrintlnLogger {
    /**
     * 唯一实例
     */
    INSTANCE;
    public static Consumer<Supplier<String>> logger = System.out::println;
    private static final String CLASSES_PATH = "classes";
    private static final String SEPARATOR_CHAR = "/";
    private static final String SEPARATOR_CHAR2 = "\\";
    private static File root;
    private static String classesPath;

    public File getRoot() {
        return root;
    }

    public synchronized void setRoot(File root) {
        if (root == null || Objects.equals(root.getPath(), "")) {
            String errMsg = "ApplicationHome cannot be empty";
            throw new NullPointerException(errMsg);
        }
        if (ApplicationHome.root != null) {
            return;
        }
        ApplicationHome.root = root;
        print(() -> "ApplicationHome is setting : " + getRoot().getPath());
    }

    /**
     * 获取项目运行的真实根路径
     * 空指针是正常情况，如果空指针，该类不应当继续运行
     *
     * @return 根路径
     */
    public String getClassesHome() {
        if (classesPath != null) {
            print(() -> "ApplicationHome is getting : " + classesPath);
            return classesPath;
        }
        return getClassesHome0();
    }

    private synchronized String getClassesHome0() {
        if (classesPath != null) {
            print(() -> "ApplicationHome is getting : " + classesPath);
            return classesPath;
        }
        classesPath = getClassesHome1();
        print(() -> "ApplicationHome is getting : " + classesPath);
        return classesPath;
    }

    private String getClassesHome1() {
        String path = root.getPath();
        if (path.endsWith(CLASSES_PATH + SEPARATOR_CHAR) || path.endsWith(CLASSES_PATH + SEPARATOR_CHAR2)) {
            return path;
        }
        if (path.endsWith(CLASSES_PATH)) {
            return path + SEPARATOR_CHAR;
        }
        if (path.endsWith(SEPARATOR_CHAR) || path.endsWith(SEPARATOR_CHAR2)) {
            return path + CLASSES_PATH + SEPARATOR_CHAR;
        }
        return path + SEPARATOR_CHAR + CLASSES_PATH + SEPARATOR_CHAR;
    }

    @Override
    public Consumer<Supplier<String>> getLogger() {
        return logger;
    }

    @Override
    public void setLogger(Consumer<Supplier<String>> logger) {
        ApplicationHome.logger = logger;
    }
}
