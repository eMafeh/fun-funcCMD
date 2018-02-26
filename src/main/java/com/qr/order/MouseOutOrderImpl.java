package com.qr.order;

import socket.script.RobotMouse;
import util.AllThreadUtil;
import util.StringSplitUtil;
import util.StringValueUtil;

import java.util.function.Function;

/**
 * @author QianRui
 */
public enum MouseOutOrderImpl implements CmdOutOrder {
    /**
     * 全局唯一实例
     */
    INSTANCE;

    @Override
    public String getDescription() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nmouse is a robot\n");
        builder.append("it have a loop time\n");
        builder.append("when time over\n");
        builder.append("mouse can move and click\n");
        builder.append("use this trick to make windows never lock.\n");
        builder.append("and mouse can get pointer color.\n\n");
        builder.append(StringValueUtil.addSpacingToLength("'" + getNameSpace() + " move  true/false'", 40)).append("when time up,mouse move to the windows centre and back,or never move\n");
        builder.append(StringValueUtil.addSpacingToLength("'" + getNameSpace() + " click true/false'", 40)).append("when time up,mouse click left key once ,or not click\n");
        builder.append(StringValueUtil.addSpacingToLength("'" + getNameSpace() + " time  [integer number]'", 40)).append("set time loops length\n");
        builder.append(StringValueUtil.addSpacingToLength("'" + getNameSpace() + " detail'", 40)).append("show mouse point color\n");
        return builder.toString();
    }

    private RobotMouse instance = RobotMouse.INSTANCE;

    {
        instance.log = this::print;
    }

    /**
     * 默认移动鼠标
     */
    private Runnable order = instance::moveROBET;
    /**
     * 默认三分钟间隔
     */
    private int period = 180000;
    private AllThreadUtil.Key key;
    private static final String TITLE = "____    \\       |___  \\ | /       _____\\_____       \n" + "|  / --------  /       \\|/       / ________ /\n" + "| /    |__    /_____ |-----|          |   \n" + "| \\    |  |  /   |   |  |  |       |  |____  \n" + "|  |  /   |   ---|-- |  |  |       |  |   \n" + "|_/  /    |      |     / \\        / \\ |  \n" + "|   /    _|      |/  _/   \\_     /   \\|_____     " + "\n";

    @Override
    public String getNameSpace() {
        return "mouse";
    }

    @Override
    public void install(Function<String, String> getLine) throws Throwable {
        print(() -> TITLE + "功能" + "\n" + "开启\n");
        install();
    }

    private void install() {
        AllThreadUtil.stop(key);
        print(() -> "当前每 " + period / 1000 + " 秒，点击一次");
        key = AllThreadUtil.whileTrueThread(() -> {
            order.run();
            return true;
        }, period);

    }

    @Override
    public void shutDown() {
        AllThreadUtil.stop(key);
    }

    @Override
    public boolean useOrder(String order) throws Throwable {
        String[] strings = StringSplitUtil.maxSplitWords(order, 3);
        return userOrder(strings[0], strings[1]);
    }

    private boolean userOrder(String first, String value) {
        switch (first) {
            case "move":
                setMove(value);
                return true;
            case "click":
                setClick(value);
                return true;
            case "time":
                setTime(value);
                return true;
            case "detail":
                instance.showMouse();
                return true;
            default:
                return false;
        }
    }

    private void setMove(String value) {
        Boolean result = StringValueUtil.caseTrueFalse(value);
        if (result != null) {
            order = result ? instance::moveROBET : instance::lazyROBET;
            install();
        }
    }

    private void setClick(String value) {
        Boolean result = StringValueUtil.caseTrueFalse(value);
        if (result != null) {
            instance.click = result;
        }
    }

    private void setTime(String value) {
        period = Integer.parseInt(value) * 1000;
        install();
    }

    @Override
    public boolean isStart() {
        return key != null && key.isRun();
    }

}
