package com.qr.solo;

import com.qr.solo.model.Cell;

import java.util.HashSet;
import java.util.Set;

/**
 * 暴力穷举
 *
 * @author QianRui
 * 2018/9/28
 */
public class Exhaustion {
    private static long time = 0;
    private static Set<String> count =new HashSet<>();

    static void compute(int i, int d) {
        if (i >= d * d * d * d) {
            count.add(Cell.lineShow());
            Cell.viewShow();
            showTime();
            System.out.println(count.size());
            return;
        }
        Cell cell = Cell.MAP.get(i);
        if (cell.getBoxNo() != 0) {
            compute(i + 1, d);
            return;
        }
        if (!cell.isRight()) {
            for (SoloValue value : cell.getPosb()) {
                if (trySetValue(cell, value)) {
                    compute(i + 1, d);
                }
                cell.value = SoloValue.N;
            }
        } else {
            compute(i + 1, d);
        }
    }

    private static boolean trySetValue(Cell cell, SoloValue value) {
        for (Cell[] cells : cell.getInAll()) {
            for (Cell bro : cells) {
                if (bro != cell && bro.value.equals(value)) {
                    return false;
                }
            }
        }
        time++;
        cell.value = value;
        return true;
    }

    private static void showTime() {
        System.out.println("churl try times :" + time);
    }
}
