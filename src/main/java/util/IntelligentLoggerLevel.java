package util;

import socket.script.RobotMouse;

import java.util.function.Function;
import java.util.function.IntConsumer;

/**
 * @author kelaite
 * 2018/2/9
 */
public class IntelligentLoggerLevel {
    public static Function<Class<? extends IntelligentLogger>, Boolean> enoughLevel() {
        return a -> true;
    }

    public static IntConsumer changeLevel() {
        return a -> {
        };
    }

    public static void main(String[] args) {
        final RobotMouse instance = RobotMouse.INSTANCE;
        instance.moveROBET();
    }
}
