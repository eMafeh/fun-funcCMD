package util;

import socket.script.RobotMouse;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author kelaite
 * 2018/2/9
 */
public class IntelligentLoggerLevel {
    /**
     * 记录一个类的日志级别
     * 初始值默认为0
     */
    private static final Map<Class<? extends IntelligentLogger>, Integer> LEVEL_CACHE = new ConcurrentSkipListMap<>();

    private static int ROOT_LEVEL = 0;
    private static final int DEFAULT_LEVEL = 0;

    /**
     * 提供一个函数方法，该方法描述指定类的级别是否足够
     */
    public static Function<Class<? extends IntelligentLogger>, Boolean> enoughLevel() {
        return a -> LEVEL_CACHE.computeIfAbsent(a, c -> DEFAULT_LEVEL) >= ROOT_LEVEL;
    }

    /**
     * 提供一个消费方法，该方法设定指定类的级别值
     */
    public static BiConsumer<Class<? extends IntelligentLogger>, Integer> changeLevel() {
        return (a, b) -> LEVEL_CACHE.computeIfAbsent(a, c -> b);
    }

    /**
     * 设置全局的最低级别
     */
    public static void setRootLevel(int rootLevel) {
        ROOT_LEVEL = rootLevel;
    }

    public static int getRootLevel() {
        return ROOT_LEVEL;
    }

    public static void main(String[] args) {
        final RobotMouse instance = RobotMouse.INSTANCE;
        instance.moveROBET();
    }
}
