package com.qr.log;

import java.util.Arrays;

/**
 * @author qianrui
 */

public enum LogLevel {
    /**
     * 调试
     */
    DEBUG(1), /**
     * 正常信息
     */
    INFO(2), /**
     * 警告信息
     */
    WARN(3), /**
     * 异常信息
     */
    ERROR(4);
    private int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static LogLevel getLoggerLevel(String string) {
        if (string != null && !"".equals(string)) {
            try {
                return LogLevel.valueOf(string.toUpperCase());
            } catch (Exception e) {
                //去下面抛异常
            }
        }
        throw new NullPointerException(errorMessage());

    }

    private static String errorMessage() {
        StringBuilder result = new StringBuilder("support log level : ");
        Arrays.stream(LogLevel.values()).forEach(a -> result.append(a.name()).append(" "));
        return result.toString();
    }
}