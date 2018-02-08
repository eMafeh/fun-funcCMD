package com.qr.order;

import com.qr.core.CmdBoot;
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
    private RobotMouse instance = RobotMouse.INSTANCE;
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
        instance.setLogger(CmdBoot.LOGGER);
        instance.print(() -> TITLE + "功能" + "\n" + "开启\n");
        install();
    }

    private void install() {
        AllThreadUtil.stop(key);
        instance.print(() -> "当前每 " + period / 1000 + " 秒，点击一次");
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
            case "clock":
                setClock(value);
                return true;
            case "time":
                setTime(value);
                return true;
            case "show":
                setShow(value);
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

    private void setClock(String value) {
        Boolean result = StringValueUtil.caseTrueFalse(value);
        if (result != null) {
            instance.clock = result;
        }
    }

    private void setTime(String value) {
        period = Integer.parseInt(value) * 1000;
        install();
    }

    public void setShow(String value) {
        Boolean result = StringValueUtil.caseTrueFalse(value);
        if (result != null) {
            instance.setLogger(result ? CmdBoot.LOGGER : null);
        }
    }

    @Override
    public boolean isStart() {
        return key != null && key.isRun();
    }
}
