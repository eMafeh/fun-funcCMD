package com.qr.phoenix;

import javax.annotation.Resource;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.awt.event.KeyEvent.*;

public class Phoenix {

    @Resource
    private static BiConsumer<String, Integer> clickKeyEvents;
    @Resource
    private static BiConsumer<Integer, Integer> chickCombination;
    @Resource
    private static Consumer<Integer> chickOneKey;
    @Resource
    private static BiFunction<String, String, String> addFileName;

    private static class Config {

        private static final String DEFAULT_NAME = "test.jar";

        private static final String BOOT_PATH;

        static {
            String property = System.getProperty("user.dir");
            BOOT_PATH = property.endsWith(".jar") ? property : addFileName.apply(property, DEFAULT_NAME);
        }

        private static final String MARKER = "restart";
    }

    public static void robotRun(String path, Supplier<String> orderLine) throws InterruptedException {
        String jarPath = getJarPath(path);
        downCaseAndCheck(jarPath, orderLine);
        chickCombination.accept(VK_WINDOWS, VK_R);
        Thread.sleep(1000);
        clickKeyEvents.accept("cmd\n\n", 500);
        clickKeyEvents.accept("chcp 437\n", 100);
        clickKeyEvents.accept("chcp\n 437\n", 500);

        clickKeyEvents.accept("start/min java -jar -Duser.dir=" + jarPath + " " + jarPath + "\n", 10);
        clickKeyEvents.accept("taskkill /f /im cmd.exe\n", 0);
    }

    private static void downCaseAndCheck(String jarPath, Supplier<String> orderLine) {
        if (!new File(jarPath).exists()) {
            throw new RuntimeException(jarPath + " : jar is not exists, cannot restart");
        }
        new Thread(() -> {
            try {
                Thread.sleep(300);
                //两次回车防止中文输入法问题
                clickKeyEvents.accept(Config.MARKER + "\n\n", 0);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }).start();
        String testCase = orderLine.get();

        if (!testCase.equals(Config.MARKER)) {
            if (!testCase.equals(Config.MARKER.toUpperCase())) {
                throw new RuntimeException("cancel restart");
            }
            //说明当前是大写环境,改小写
            chickOneKey.accept(VK_CAPS_LOCK);
        }
    }

    private static String getJarPath(String path) {
        return path == null || path.equals("") ? Config.BOOT_PATH : path;
    }

    public static void run() throws IOException, AWTException {
        Runtime RUNTIME = Runtime.getRuntime();
        Process exec = RUNTIME.exec("java -jar test.jar", new String[0], new File("D:\\XqlDownload\\test\\out\\artifacts\\test_jar"));
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream(), "GBK"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if ("欢迎使用集成工具cmd".equals(line)) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("over");
    }
}
