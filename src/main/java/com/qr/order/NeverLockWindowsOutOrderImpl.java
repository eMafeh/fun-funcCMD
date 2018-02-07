package com.qr.order;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class NeverLockWindowsOutOrderImpl implements CmdOutOrder{
    /**
     * 当前鼠标指到屏幕左上方（开始按钮）
     * 那么该锁定程序自动退出
     * 防止瞎比乱点点到不该点的
     */
    private static void safeOut() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        if (location.x == 0 && location.y == 0) {
            System.err.println("防锁定程序退出");
            System.exit(0);
        }
    }

    private static final Random random = new Random();

    private static final Robot robot;//机器人

    private static int period = 1000;//点击间隔基数
    private static boolean clock;//是否点击

    private static Runnable order;//机器人的操作逻辑
    private static final int rm = 10;//每次变动量范围

    private static final Point fpoint;//初始点
    private static Point lpoint;//上一次点击点

    //鼠标可以点击的范围
    private static final int Xmin;
    private static final int Ymin;
    private static final int Xmax;
    private static final int Ymax;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.exit(1);
            throw new RuntimeException();
        }
        //获取当前鼠标
        lpoint = MouseInfo.getPointerInfo().getLocation();

        //获取鼠标最大位移
        robot.mouseMove(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point location = MouseInfo.getPointerInfo().getLocation();

        //初始点为屏幕中心
        fpoint = new Point(location.x / 2, location.y / 2);

        //鼠标移回原位
        robot.mouseMove(lpoint.x, lpoint.y);

        //鼠标可以点击的范围
        Xmin = fpoint.x - 200;
        Ymin = fpoint.y - 200;
        Xmax = fpoint.x + 100;
        Ymax = fpoint.y + 100;

    }

    /**
     * 主函数入口
     * 通过循环逻辑和机器人点击逻辑
     * 循环点击屏幕
     * 避免锁屏
     */
    public static void main(String[] args) {
        useParam(args);
        show();
        loopTask(order);
    }

    /**
     * 尝试根据启动参数设置运行间隔和运行模式
     */
    private static void useParam(String[] args) {
        useParam0(args);
        useParam1(args);
        useParam2(args);
    }

    /**
     * 尝试根据启动参数设置运行间隔
     */
    private static void useParam0(String[] args) {
        try {
            period *= Integer.parseInt(args[0]);
        } catch (Exception e) {
            period *= 180;
        }
    }

    /**
     * 尝试根据启动参数设置运行模式
     */
    private static void useParam1(String[] args) {
        try {
            order = "R".equalsIgnoreCase(args[1]) ? NeverLockWindowsOutOrderImpl::ROBET : NeverLockWindowsOutOrderImpl::lazyROBET;
        } catch (Exception e) {
            order = NeverLockWindowsOutOrderImpl::lazyROBET;
        }
    }

    /**
     * 尝试根据启动参数设置点击模式
     */
    private static void useParam2(String[] args) {
        try {
            clock = !"N".equalsIgnoreCase(args[2]);
        } catch (Exception e) {
            clock = true;
        }
        if (!clock) order = NeverLockWindowsOutOrderImpl::ROBET;
    }

    /**
     * 获取当前鼠标位置和目标位置
     * 然后点击避免锁屏
     */
    private static void ROBET() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        Point target = clickPoint();
        System.out.println("RandomMouse x:" + target.x + "   y:" + target.y);
        notLock(location, target);
    }

    /**
     * 原位点击来避免锁屏
     */
    private static void lazyROBET() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        System.out.println("lazyMouse x:" + location.x + "   y:" + location.y);
        robotClick();
    }

    /**
     * 循环执行
     * 使用的是jdk的Timer类和TimerTask
     * 调用后立刻执行第一次任务
     * 退出逻辑由safeOut方法支持
     */
    private static void loopTask(Runnable runnable) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                safeOut();
                runnable.run();
            }
        }, 0, period);
    }

    /**
     * 移至目标位置
     * 点击
     * 移回原位
     */
    private static void notLock(Point location, Point target) {
        robot.mouseMove(target.x, target.y);
        robotClick();
        robot.mouseMove(location.x, location.y);
    }

    /**
     * 获取合适的点击点
     */
    private static Point clickPoint() {
        Point target = new Point();
        lpoint.x = target.x = computerX();
        lpoint.y = target.y = computerY();
        return target;
    }

    /**
     * 获取合适的点击x坐标
     * 随机生成位移量
     * 然后将上一次的点偏移至新位置
     * 如果新位置满足x的范围
     * 返回新位置
     * 否则
     * 返回初始x坐标位置
     */
    private static int computerX() {
        int tx;
        return (tx = lpoint.x + randomxy()) < Xmax && tx > Xmin ? tx : fpoint.x;
    }

    /**
     * 获取合适的点击点y坐标
     * 逻辑同上
     */
    private static int computerY() {
        int ty;
        return (ty = lpoint.y + randomxy()) < Ymax && ty > Ymin ? ty : fpoint.y;
    }

    /**
     * 鼠标点左键，鼠标松开左键
     */
    private static void robotClick() {
        if (clock) {
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            System.out.println("click");
        } else System.out.println("noClick");
    }


    /**
     * 根据设定的偏移范围，获取随机偏移的量
     */
    private static int randomxy() {
        return random.nextInt(rm) - rm / 2;
    }

    private static void show() {
        System.out.println(
                "____    \\       |___  \\ | /       _____\\_____       \n" +
                        "|  / --------  /       \\|/       / ________ /\n" +
                        "| /    |__    /_____ |-----|          |   \n" +
                        "| \\    |  |  /   |   |  |  |       |  |____  \n" +
                        "|  |  /   |   ---|-- |  |  |       |  |   \n" +
                        "|_/  /    |      |     / \\        / \\ |  \n" +
                        "|   /    _|      |/  _/   \\_     /   \\|_____     " +
                        "\n" +
                        "程序" +
                        "\n" +
                        "开启\n" +
                        "\n" +
                        "(检查时，鼠标在左上角本程序会安全退出)");
        System.out.println("当前每 " + period / 1000 + " 秒，点击一次");
    }

    @Override
    public String getNameSpace() {
        return "neverLock";
    }

    @Override
    public void useOrder(String order) throws Throwable {

    }

    @Override
    public void install(Function<String, String> getLine) throws Throwable {

    }

    @Override
    public void shutDown() {

    }
}
