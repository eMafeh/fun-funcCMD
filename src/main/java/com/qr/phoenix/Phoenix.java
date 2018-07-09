package com.qr.phoenix;

import javax.annotation.Resource;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.*;

import static java.awt.event.KeyEvent.*;

public class Phoenix {

    @Resource
    private static BiConsumer<String, Integer> clickKeyEvents;
    @Resource
    private static BiConsumer<Integer, Integer> chickCombination;
    @Resource
    private static Consumer<Integer> chickOneKey;
    @Resource
    private static BiFunction<String, String, String[]> getTargetPath;

    private static final String MARKER = "restart";

    public static void robotRun(String path, Supplier<String> orderLine) throws InterruptedException {
        String jarPath = getJarPath(path);

        downCaseAndCheck(orderLine);
        Thread.sleep(150);
        chickCombination.accept(VK_WINDOWS, VK_R);
        Thread.sleep(1000);
        clickKeyEvents.accept("cmd\n\n\n", 1000);
        clickKeyEvents.accept("chcp 437\n\n", 100);
        clickKeyEvents.accept("chcp\n 437\n\n", 1000);
        clickKeyEvents.accept("start/min java -jar -Duser.dir=" + new File(jarPath).getParent() + " " + jarPath + "\n", 10);
        clickKeyEvents.accept("taskkill /f /im cmd.exe\n", 0);
    }

    private static void downCaseAndCheck(Supplier<String> orderLine) {
        new Thread(() -> {
            try {
                Thread.sleep(300);
                //两次回车防止中文输入法问题
                clickKeyEvents.accept(MARKER + "\n\n", 0);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }).start();
        String testCase = orderLine.get();

        if (!testCase.equals(MARKER)) {
            if (!testCase.equals(MARKER.toUpperCase())) {
                throw new RuntimeException("cancel restart");
            }
            //说明当前是大写环境,改小写
            chickOneKey.accept(VK_CAPS_LOCK);
        }
    }

    private static String getJarPath(String path) {
        if (path == null || path.trim().equals("")) {
            path = System.getProperty("user.dir");
        }
        String[] apply = getTargetPath.apply(path, ".jar");
        if (apply.length == 1) {
            return apply[0];
        }
        throw new RuntimeException(path + " : jars number not one, cannot restart");

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
