package compile;


import util.ClassUtils;
import util.FileUtils;
import util.PrintlnLogger;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author kelaite
 */
public enum DynamicCompile implements PrintlnLogger {
    /**
     * 唯一实例
     */
    INSTANCE;

    public static Consumer<Supplier<String>> logger = System.out::println;
    private static final String TITLE = "< DynamicCompile >";

    @Override
    public Consumer<Supplier<String>> getLogger() {
        return logger;
    }

    @Override
    public void setLogger(Consumer<Supplier<String>> logger) {
        DynamicCompile.logger = logger;
    }

    /**
     * 类加载器，由spring方法说明
     */
    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    /**
     * 类加载器的defineClass方法，反射加载新的class
     */
    private Method defineClassMethod = getDefineClass(classLoader.getClass());

    /**
     * 根据全类名和java文件，编译文件，将文件输出到对应的包路径下，并且加载该class
     *
     * @param fullClassName 全类名
     * @param getJavaCode   java文件
     * @throws Exception 编译失败返回错误原因
     */
    public synchronized void compileToClass(String fullClassName, Callable<String> getJavaCode) throws Exception {

        //java的编译器实例
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        //编译各参数准备
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        ClassFileManager manager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));
        List<String> options = JavaCompileOptionsBuilder.INSTANCE.getOptions();
        print(() -> TITLE + "options : " + options);
        Iterable<JavaFileObject> compilationUnits = getCompilationUnits(fullClassName, getJavaCode);

        //编译
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, options, null, compilationUnits);
        Boolean success = task.call();
        print(() -> TITLE + "toClass success : " + success);

        if (success) {
            //成功获取字节码，输出文件，加载该类
            JavaClassObject jco = manager.getJavaClassObject();
            byte[] bytes = jco.getBytes();
            writeClass(fullClassName, bytes);
            loadClass(fullClassName, bytes);
        } else {
            //失败抛出错误信息
            String error = compileError(diagnostics);
            throw new Exception(error);
        }
    }

    /**
     * 根据全类名和字节码输出.class文件
     *
     * @param fullClassName 全类名
     * @param bytes         字节码
     * @throws IOException io异常
     */
    private void writeClass(String fullClassName, byte[] bytes) throws IOException {
        String name = ApplicationHome.INSTANCE.getClassesHome() + className2Path(fullClassName) + JavaFileObject.Kind.CLASS.extension;
        print(() -> TITLE + "filePath : " + name);
        File file = new File(name);
        //调用fileUtils来输出文件
        FileUtils.writeByteArrayToFile(file, bytes);
        print(() -> TITLE + "writeByteArrayToFile : success");
        files.add(file);
    }

    private Set<File> files = new TreeSet<>();

    public synchronized void clearFile() {
        for (File file : files) {
            try {
                if (file.exists()) {
                    if (file.delete()) {
                        print(() -> TITLE + "file is delete : " + file);
                    } else {
                        print(() -> TITLE + "file is not delete : " + file);
                    }
                } else {
                    print(() -> TITLE + "file is not exists : " + file);
                }
            } catch (Exception e) {
                print(() -> {
                    e.printStackTrace();
                    return TITLE + "clearFile : " + file;
                });
            }
        }
    }

    /**
     * 反射获取类加载器的defineClass方法
     *
     * @param loader 类加载器
     * @return 该类加载器的defineClass（String, byte[], int, int）方法
     * @throws RuntimeException 如果该类和该类的父类找不到这个方法，抛出异常
     */
    private Method getDefineClass(Class<?> loader) throws RuntimeException {
        print(() -> TITLE + "getDeclaredMethod Class<?>: " + loader);
        try {
            //该类有这个方法，返回这个方法破除了private
            Method method = loader.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            method.setAccessible(true);
            print(() -> TITLE + "getDeclaredMethod : success");
            return method;
        } catch (NoSuchMethodException e) {
            //该类没有这个方法，去父类找
            Class<?> superclass = loader.getSuperclass();
            print(() -> TITLE + "want superClass help :" + superclass);
            if (!superclass.equals(Object.class)) {
                return getDefineClass(superclass);
            }
            print(() -> TITLE + "superClass can not help");
            //父类是Object，说明到头了，抛出找不到的异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 反射加载新类
     *
     * @param fullClassName 全类名
     * @param bytes         字节码字节数组
     * @throws InvocationTargetException 目标对象没有这个方法
     * @throws IllegalAccessException    参数错误
     */
    private void loadClass(String fullClassName, byte[] bytes) throws InvocationTargetException, IllegalAccessException {
        defineClassMethod.invoke(classLoader, fullClassName, bytes, 0, bytes.length);
        print(() -> TITLE + "defineClassMethod success");
    }

    private Iterable<JavaFileObject> getCompilationUnits(String fullClassName, Callable<String> getJavaCode) throws Exception {
        ArrayList<JavaFileObject> result = new ArrayList<>();
        String javaCode = getJavaCode.call();
        print(() -> TITLE + "javaCode : " + javaCode);
        JavaFileObject.Kind kind = JavaFileObject.Kind.SOURCE;
        result.add(new SimpleJavaFileObject(createURI(fullClassName, kind), kind) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
                return javaCode;
            }
        });
        return result;
    }

    /**
     * 加工异常信息
     *
     * @param diagnostics 编译的错误返回
     * @return 异常信息
     */
    private String compileError(DiagnosticCollector<JavaFileObject> diagnostics) {
        StringBuilder error = new StringBuilder();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            error.append("LineNumber:").append(diagnostic.getLineNumber()).append("\n").append("ColumnNumber").append(diagnostic.getColumnNumber()).append("\n").append("Message").append(diagnostic.getMessage(null)).append("\n");
        }
        error.append("COMPILE ERROR");
        return error.toString();
    }

    /**
     * 全类名转URI
     *
     * @param name 全类名
     * @param kind 文件类型
     * @return URI
     */
    private URI createURI(String name, JavaFileObject.Kind kind) {
        return URI.create("string:///" + className2Path(name) + kind.extension);
    }

    /**
     * 全类名转相对类路径
     *
     * @param name 全类名
     * @return 相对类路径
     */
    private String className2Path(String name) {
        return name.replace('.', '/');
    }

    /**
     * 编译用实现类，产生一个class文件实例
     */
    private static class JavaClassObject extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        private JavaClassObject(String name, Kind kind) {
            super(INSTANCE.createURI(name, kind), kind);
        }

        private byte[] getBytes() {
            return outputStream.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return outputStream;
        }
    }

    /**
     * class类持有控制类
     */
    private static class ClassFileManager extends ForwardingJavaFileManager {
        private JavaClassObject javaClassObject;

        private ClassFileManager(StandardJavaFileManager standardFileManager) {
            super(standardFileManager);
        }

        private JavaClassObject getJavaClassObject() {
            return javaClassObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return (javaClassObject = new JavaClassObject(className, kind));
        }
    }

}
