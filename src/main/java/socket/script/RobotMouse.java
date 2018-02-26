package socket.script;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/8
 */
public enum RobotMouse {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    public Consumer<Supplier<String>> log = a -> System.out.println(a.get());
    /**
     * 默认不点击
     */
    public boolean click = false;

    /**
     * 获取当前鼠标位置和目标位置
     * 然后点击避免锁屏
     */
    public void moveROBET() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        Point target = clickPoint();
        log.accept(() -> "RandomMouse x:" + target.x + "   y:" + target.y);
        notLock(location, target);
    }

    /**
     * 原位点击来避免锁屏
     */
    public void lazyROBET() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        log.accept(() -> "lazyMouse x:" + location.x + "   y:" + location.y);
        robotClick();
    }

    /**
     * 机器人
     */
    public Robot robot;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException();
        }
    }

    /**
     * center of the windows
     */
    private final Point CENTER_POINT;
    /**
     * last point position
     */
    private Point lastPoint;

    /**
     * 鼠标可以点击的范围
     */
    private final int X_MIN;
    private final int Y_MIN;
    private final int X_MAX;
    private final int Y_MAX;

    {
        //获取当前鼠标
        lastPoint = MouseInfo.getPointerInfo().

                getLocation();

        //获取鼠标最大位移
        robot.mouseMove(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point location = MouseInfo.getPointerInfo().getLocation();

        //初始点为屏幕中心
        CENTER_POINT = new

                Point(location.x / 2, location.y / 2);

        //鼠标移回原位
        robot.mouseMove(lastPoint.x, lastPoint.y);

        //鼠标可以点击的范围
        X_MIN = CENTER_POINT.x - 200;
        Y_MIN = CENTER_POINT.y - 200;
        X_MAX = CENTER_POINT.x + 100;
        Y_MAX = CENTER_POINT.y + 100;
    }

    private static final Random RANDOM = new Random();

    /**
     * 每次变动量范围
     */
    private static final int RM = 10;


    /**
     * 移至目标位置
     * 点击
     * 移回原位
     */
    private void notLock(Point location, Point target) {
        robot.mouseMove(target.x, target.y);
        robotClick();
        robot.mouseMove(location.x, location.y);
    }

    /**
     * 获取合适的点击点
     */
    private Point clickPoint() {
        Point target = new Point();
        lastPoint.x = target.x = computerX();
        lastPoint.y = target.y = computerY();
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
    private int computerX() {
        int tx;
        return (tx = lastPoint.x + randomxy()) < X_MAX && tx > X_MIN ? tx : CENTER_POINT.x;
    }

    /**
     * 获取合适的点击点y坐标
     * 逻辑同上
     */
    private int computerY() {
        int ty;
        return (ty = lastPoint.y + randomxy()) < Y_MAX && ty > Y_MIN ? ty : CENTER_POINT.y;
    }

    /**
     * 鼠标点左键，鼠标松开左键
     */
    private void robotClick() {
        if (click) {
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            log.accept(() -> "click");
        } else {
            log.accept(() -> "noClick");
        }
    }

    /**
     * 根据设定的偏移范围，获取随机偏移的量
     */
    private int randomxy() {
        return RANDOM.nextInt(RM) - RM / 2;
    }

    public void showMouse() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        log.accept(() -> robot.getPixelColor(location.x, location.y).toString());
    }

}
