package compile;

import util.ClassUtils;
import util.PrintlnLogger;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * @author kelaite
 * 2018/1/24
 */
public enum JavaCompileOptionsBuilder implements PrintlnLogger {
    /**
     * 唯一实例
     */
    INSTANCE;

    public static Consumer<Callable<String>> logger;
    private static final String JAR_KIND = ".jar";

    @Override
    public Consumer<Callable<String>> getLogger() {
        return logger;
    }

    @Override
    public void setLogger(Consumer<Callable<String>> logger) {
        JavaCompileOptionsBuilder.logger = logger;
    }

    private Set<String> paths = new TreeSet<>();


    /**
     * 获取编译用的包含classes的options
     *
     * @return 编译用options
     */
    public List<String> getOptions() {
        List<String> options = new ArrayList<>();
        options.add("-encoding");
        options.add("UTF-8");
        options.add("-classpath");
        StringBuilder stringBuilder = new StringBuilder();
        //空指针是正常情况，如果空指针，该类不应当继续运行
        for (String file : paths) {
            stringBuilder.append(file).append(File.pathSeparator);
        }
        options.add(stringBuilder.toString());
        return options;
    }


    JavaCompileOptionsBuilder() {
        try {
            //优先尝试URLClassLoader的方式，这个比较稳也比较快
            setClassUrl();
            print(() -> "ClassLoader is URLClassLoader,load classURL");
        } catch (Exception e) {
            print(() -> "ClassLoader is not URLClassLoader,try recursion");
            //如果类加载器不是URLClassLoader，则根据实际项目路径自己递归添加url
            setClassUrl(ApplicationHome.INSTANCE.getRoot());
            //除了jar的url，还有class的根路径
            paths.add(ApplicationHome.INSTANCE.getClassesHome());
        }
        print(() -> "load classURL : " + paths);
    }

    private void setClassUrl() {
        //空指针是正常情况，如果空指针，该类不应当继续运行
        URL[] urLs = ((URLClassLoader) ClassUtils.getDefaultClassLoader()).getURLs();
        for (URL urL : urLs) {
            paths.add(urL.getFile());
        }
    }

    private void setClassUrl(File root) {
        //路径不存在，不添加该文件
        if (!root.exists()) {
            return;
        }
        //文件存在，判断是否是jar文件
        if (root.isFile()) {
            String path = root.getPath();

            if (path.endsWith(JAR_KIND)) {
                paths.add(path);
            }
            return;
        }
        //目录则递归继续判断
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                setClassUrl(file);
            }
        }
    }

}
