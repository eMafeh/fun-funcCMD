package util;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.awt.event.KeyEvent.*;

public class Phoenix {
    private static final Runtime RUNTIME = Runtime.getRuntime();
    public static final String BOOT_PATH = System.getProperty("user.dir");

    public static void run() throws IOException, AWTException {

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

    public static void robotRun() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robot.keyPress(VK_WINDOWS);
        robot.keyPress(VK_R);
        robot.keyRelease(VK_WINDOWS);
        robot.keyRelease(VK_R);

        //打开控制台java
        check(1000, robot, VK_C, VK_M, VK_D, VK_ENTER, VK_ENTER);

        //选择英文输入法
        check(1000, robot, VK_C, VK_H, VK_C, VK_P, VK_SPACE, VK_4, VK_3, VK_7, VK_ENTER);
        check(1000, robot, VK_C, VK_H, VK_C, VK_P, VK_ENTER, VK_SPACE, VK_4, VK_3, VK_7, VK_ENTER);

        //切换目录 D:\\XqlDownload\\test\\out\\artifacts\\test_jar
        sleep(1000);
        robot.keyPress(VK_D);
        robot.keyRelease(VK_D);
        sleep(150);
        robot.keyPress(VK_SHIFT);
        robot.keyPress(VK_SEMICOLON);
        robot.keyRelease(VK_SHIFT);
        robot.keyRelease(VK_SEMICOLON);
        sleep(150);
        robot.keyPress(VK_ENTER);
        robot.keyRelease(VK_ENTER);

        check(150, robot, VK_C, VK_D, VK_SPACE, VK_X, VK_Q, VK_L, VK_D, VK_O, VK_W, VK_N, VK_L, VK_O, VK_A, VK_D, VK_SLASH, VK_T, VK_E, VK_S, VK_T, VK_SLASH, VK_O, VK_U, VK_T, VK_SLASH, VK_A, VK_R, VK_T, VK_I, VK_F, VK_A, VK_C, VK_T, VK_S, VK_SLASH, VK_T, VK_E, VK_S, VK_T);

        sleep(150);
        robot.keyPress(VK_SHIFT);
        robot.keyPress(VK_MINUS);
        robot.keyRelease(VK_SHIFT);
        robot.keyRelease(VK_MINUS);
        check(150, robot, VK_J, VK_A, VK_R, VK_ENTER);

        //运行java程序
        check(150, robot, VK_S, VK_T, VK_A, VK_R, VK_T, VK_SLASH, VK_M, VK_I, VK_N, VK_SPACE);

        check(10, robot, VK_J, VK_A, VK_V, VK_A, VK_SPACE, VK_MINUS, VK_J, VK_A, VK_R, VK_SPACE, VK_T, VK_E, VK_S, VK_T, VK_PERIOD, VK_J, VK_A, VK_R, VK_ENTER);
        check(10, robot, VK_CAPS_LOCK);

        check(150, robot, VK_S, VK_T, VK_A, VK_R, VK_T, VK_SLASH, VK_M, VK_I, VK_N, VK_SPACE);

        check(10, robot, VK_J, VK_A, VK_V, VK_A, VK_SPACE, VK_MINUS, VK_J, VK_A, VK_R, VK_SPACE, VK_T, VK_E, VK_S, VK_T, VK_PERIOD, VK_J, VK_A, VK_R, VK_ENTER);

        //关闭多余的控制台
        check(1000,robot,VK_T,VK_A,VK_S,VK_K,VK_K,VK_I,VK_L,VK_L,VK_SPACE,VK_SLASH,VK_F,VK_SPACE,VK_SLASH,VK_I,VK_M,VK_SPACE,VK_C,VK_M,VK_D,VK_PERIOD,VK_E,VK_X,VK_E,VK_ENTER);
    }

    public static void main(String[] args) {
            robotRun();

    }

    private static void check(long time, Robot robot, int... keys) {
        sleep(time);
        for (int key : keys) {
            robot.keyPress(key);
            robot.keyRelease(key);
        }
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
