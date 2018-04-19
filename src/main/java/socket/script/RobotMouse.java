package socket.script;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.ThreadLocalRandom;
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
    public Consumer<Supplier<String>> log;
    /**
     * 默认不点击
     */
    public boolean click = false;

    /**
     * 默认不移动
     */
    public boolean move = false;

    /**
     * 获取当前鼠标位置和目标位置
     * 然后点击避免锁屏
     */
    public void robotMoveAndDo() {
        robotMoveAndDo(this::robotClick);
    }

    /**
     * 机器人
     */
    @Resource
    private Supplier<Robot> robot;


    /**
     * last point position
     */
    private Point lastPoint = MouseInfo.getPointerInfo().getLocation();

    /**
     * 每次变动量范围
     */
    private static final int RM = 10;


    /**
     * 移至目标位置
     * 点击
     * 移回原位
     */
    private void robotMoveAndDo(Runnable doSome) {
        Point location = MouseInfo.getPointerInfo().getLocation();
        if (move) {
            Point target = clickPoint();
            robot.get().mouseMove(target.x, target.y);
            doSome.run();
            robot.get().mouseMove(location.x, location.y);
        } else {
            doSome.run();
        }
        log.accept(() -> (move ? "Move" : "lazy") + "Mouse x:" + location.x + "   y:" + location.y);

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
        return (tx = lastPoint.x + randomxy()) < Windows.X_MAX && tx > Windows.X_MIN ? tx : Windows.CENTER_POINT.x;
    }

    /**
     * 获取合适的点击点y坐标
     * 逻辑同上
     */
    private int computerY() {
        int ty;
        return (ty = lastPoint.y + randomxy()) < Windows.Y_MAX && ty > Windows.Y_MIN ? ty : Windows.CENTER_POINT.y;
    }

    /**
     * 鼠标点左键，鼠标松开左键
     */
    private void robotClick() {
        if (click) {
            robot.get().mousePress(InputEvent.BUTTON1_MASK);
            robot.get().mouseRelease(InputEvent.BUTTON1_MASK);
        }
        log.accept(() -> click ? "click" : "noClick");
    }

    /**
     * 根据设定的偏移范围，获取随机偏移的量
     */
    private int randomxy() {
        return ThreadLocalRandom.current().nextInt(RM) - RM / 2;
    }

    public void showMouse() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        log.accept(() -> "(" + location.x + "," + location.y + ")" + robot.get().getPixelColor(location.x, location.y).toString());
    }


    private static class Windows {
        /**
         * center of the windows
         */
        private static final Point CENTER_POINT;
        /**
         * 鼠标可以点击的范围
         */
        private static final int X_MIN;
        private static final int Y_MIN;
        private static final int X_MAX;
        private static final int Y_MAX;

        static {
            //获取鼠标最大位移
            INSTANCE.robot.get().mouseMove(Integer.MAX_VALUE, Integer.MAX_VALUE);
            Point location = MouseInfo.getPointerInfo().getLocation();

            //初始点为屏幕中心
            CENTER_POINT = new Point(location.x / 2, location.y / 2);

            //鼠标移回原位
            INSTANCE.robot.get().mouseMove(INSTANCE.lastPoint.x, INSTANCE.lastPoint.y);

            //鼠标可以点击的范围
            X_MIN = CENTER_POINT.x - 200;
            Y_MIN = CENTER_POINT.y - 200;
            X_MAX = CENTER_POINT.x + 100;
            Y_MAX = CENTER_POINT.y + 100;
        }

    }
}
