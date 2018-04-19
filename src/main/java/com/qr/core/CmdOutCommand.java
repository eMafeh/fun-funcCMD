package com.qr.core;

import com.qr.log.IntelligentLogger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 2017/9/12  10:24
 *
 * @author qianrui
 */
public interface CmdOutCommand extends IntelligentLogger {
    @Resource
    List<BiFunction<String, Integer, String>> addSpacingToLength = new ArrayList<>();
    @Resource
    List<BiFunction<String, Integer, String[]>> maxSplitWords = new ArrayList<>();
    @Resource
    List<Function<Throwable, String>> deepMessage = new ArrayList<>();
    @Resource
    List<BiFunction<String, Integer, String>> nextWord = new ArrayList<>();
    @Resource
    List<Supplier<String>> orderLine = new ArrayList<>();
    @Resource
    List<Function<String, Boolean>> caseTrueFalse = new ArrayList<>();

    String getNameSpace();

    boolean useCommand(String order) throws Throwable;

    boolean isStart();

    void install(Supplier<String> getLine) throws Throwable;

    void shutDown();

    default String getDescription() {
        return "";
    }

}
