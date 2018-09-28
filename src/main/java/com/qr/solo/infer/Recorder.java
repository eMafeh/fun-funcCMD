package com.qr.solo.infer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author QianRui
 * 2018/9/28
 */
public class Recorder {
    private static final Map<Class<? extends CellStrategy>, long[]> COUNTER = new HashMap<>();

    static void addFix(CellStrategy cellStrategy) {
        System.out.println(cellStrategy.getClass().getSimpleName() + " god job! " + 1 + " fix!");
        COUNTER.computeIfAbsent(cellStrategy.getClass(), a -> new long[2])[0] += 1;
    }

    static void reducePosb(CellStrategy cellStrategy, int num) {
        if (num <= 0) return;
        System.out.println(cellStrategy.getClass().getSimpleName() + " not bad! " + num + " reduce!");
        COUNTER.computeIfAbsent(cellStrategy.getClass(), a -> new long[2])[1] += num;
    }

    public static void show() {
        for (Map.Entry<Class<? extends CellStrategy>, long[]> entry : COUNTER.entrySet()) {
            System.out.println(entry.getKey().getSimpleName() + " fix: " + entry.getValue()[0] + " | reduce: " + entry.getValue()[1]);
        }
    }
}
