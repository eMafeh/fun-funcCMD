package com.qr.order;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class NeverLockWindowsOutOrderImpl implements CmdOutOrder{
    /**
     * ��ǰ���ָ����Ļ���Ϸ�����ʼ��ť��
     * ��ô�����������Զ��˳�
     * ��ֹϹ���ҵ�㵽���õ��
     */
    private static void safeOut() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        if (location.x == 0 && location.y == 0) {
            System.err.println("�����������˳�");
            System.exit(0);
        }
    }

    private static final Random random = new Random();

    private static final Robot robot;//������

    private static int period = 1000;//����������
    private static boolean clock;//�Ƿ���

    private static Runnable order;//�����˵Ĳ����߼�
    private static final int rm = 10;//ÿ�α䶯����Χ

    private static final Point fpoint;//��ʼ��
    private static Point lpoint;//��һ�ε����

    //�����Ե���ķ�Χ
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
        //��ȡ��ǰ���
        lpoint = MouseInfo.getPointerInfo().getLocation();

        //��ȡ������λ��
        robot.mouseMove(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point location = MouseInfo.getPointerInfo().getLocation();

        //��ʼ��Ϊ��Ļ����
        fpoint = new Point(location.x / 2, location.y / 2);

        //����ƻ�ԭλ
        robot.mouseMove(lpoint.x, lpoint.y);

        //�����Ե���ķ�Χ
        Xmin = fpoint.x - 200;
        Ymin = fpoint.y - 200;
        Xmax = fpoint.x + 100;
        Ymax = fpoint.y + 100;

    }

    /**
     * ���������
     * ͨ��ѭ���߼��ͻ����˵���߼�
     * ѭ�������Ļ
     * ��������
     */
    public static void main(String[] args) {
        useParam(args);
        show();
        loopTask(order);
    }

    /**
     * ���Ը������������������м��������ģʽ
     */
    private static void useParam(String[] args) {
        useParam0(args);
        useParam1(args);
        useParam2(args);
    }

    /**
     * ���Ը������������������м��
     */
    private static void useParam0(String[] args) {
        try {
            period *= Integer.parseInt(args[0]);
        } catch (Exception e) {
            period *= 180;
        }
    }

    /**
     * ���Ը�������������������ģʽ
     */
    private static void useParam1(String[] args) {
        try {
            order = "R".equalsIgnoreCase(args[1]) ? NeverLockWindowsOutOrderImpl::ROBET : NeverLockWindowsOutOrderImpl::lazyROBET;
        } catch (Exception e) {
            order = NeverLockWindowsOutOrderImpl::lazyROBET;
        }
    }

    /**
     * ���Ը��������������õ��ģʽ
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
     * ��ȡ��ǰ���λ�ú�Ŀ��λ��
     * Ȼ������������
     */
    private static void ROBET() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        Point target = clickPoint();
        System.out.println("RandomMouse x:" + target.x + "   y:" + target.y);
        notLock(location, target);
    }

    /**
     * ԭλ�������������
     */
    private static void lazyROBET() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        System.out.println("lazyMouse x:" + location.x + "   y:" + location.y);
        robotClick();
    }

    /**
     * ѭ��ִ��
     * ʹ�õ���jdk��Timer���TimerTask
     * ���ú�����ִ�е�һ������
     * �˳��߼���safeOut����֧��
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
     * ����Ŀ��λ��
     * ���
     * �ƻ�ԭλ
     */
    private static void notLock(Point location, Point target) {
        robot.mouseMove(target.x, target.y);
        robotClick();
        robot.mouseMove(location.x, location.y);
    }

    /**
     * ��ȡ���ʵĵ����
     */
    private static Point clickPoint() {
        Point target = new Point();
        lpoint.x = target.x = computerX();
        lpoint.y = target.y = computerY();
        return target;
    }

    /**
     * ��ȡ���ʵĵ��x����
     * �������λ����
     * Ȼ����һ�εĵ�ƫ������λ��
     * �����λ������x�ķ�Χ
     * ������λ��
     * ����
     * ���س�ʼx����λ��
     */
    private static int computerX() {
        int tx;
        return (tx = lpoint.x + randomxy()) < Xmax && tx > Xmin ? tx : fpoint.x;
    }

    /**
     * ��ȡ���ʵĵ����y����
     * �߼�ͬ��
     */
    private static int computerY() {
        int ty;
        return (ty = lpoint.y + randomxy()) < Ymax && ty > Ymin ? ty : fpoint.y;
    }

    /**
     * �������������ɿ����
     */
    private static void robotClick() {
        if (clock) {
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            System.out.println("click");
        } else System.out.println("noClick");
    }


    /**
     * �����趨��ƫ�Ʒ�Χ����ȡ���ƫ�Ƶ���
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
                        "����" +
                        "\n" +
                        "����\n" +
                        "\n" +
                        "(���ʱ����������ϽǱ�����ᰲȫ�˳�)");
        System.out.println("��ǰÿ " + period / 1000 + " �룬���һ��");
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
