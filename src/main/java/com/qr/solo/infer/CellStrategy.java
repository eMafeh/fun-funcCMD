package com.qr.solo.infer;

import com.qr.solo.SoloValue;
import com.qr.solo.model.Cell;

import java.util.*;

/**
 * @author QianRui
 * 2018/9/28
 */
public interface CellStrategy {
    Stack<Cell> CHANGE_CELL = new Stack<>();
    List<CellStrategy> ALL_STRATEGY = new ArrayList<>();

    /**
     * 处理一个未填数字的格子
     *
     * @param cell 为填数的格子
     */
    void solo(Cell cell);

    static void doInfer() {
        CellStrategy.ALL_STRATEGY.add(HiddenSingle.SINGLE);
        CellStrategy.ALL_STRATEGY.add(NakedSingle.SINGLE);
        CellStrategy.ALL_STRATEGY.add(HiddenPair.SINGLE);
        CellStrategy.ALL_STRATEGY.add(NakedPair.SINGLE);
        CellStrategy.ALL_STRATEGY.add(LockedCandidates.SINGLE);

        CHANGE_CELL.addAll(Cell.MAP.values());
        int maxStack = 0;
        while (!CHANGE_CELL.empty()) {
            maxStack = Math.max(CHANGE_CELL.size(), maxStack);
            Cell pop = CHANGE_CELL.pop();
            if (!pop.isRight()) {
                CellStrategy.ALL_STRATEGY.forEach(a -> a.solo(pop));
            }
        }
        Cell.viewShow();
        System.out.println();
        System.out.println("strategy(maxStack: " + maxStack + ") " + System.currentTimeMillis());
        Recorder.show();
        Cell.detailShow();
    }

    default void setValue(Cell cell, SoloValue value) {
        int change = cell.setValue(value, CHANGE_CELL);

        Recorder.addFix(this);
        Recorder.reducePosb(this, change);
    }

    default void removePosb(Cell[] cells, SoloValue value, String title, Cell... excepts) {
        int oldSize = CHANGE_CELL.size();
        Cell.removePosb(cells, value, CHANGE_CELL, title, excepts);
        Recorder.reducePosb(this, CHANGE_CELL.size() - oldSize);
    }

    default void onlyPosb(Cell cell, String title, SoloValue... values) {
        Set<SoloValue> posb = cell.getPosb();
        int size = posb.size();
        if (cell.isRight() || size == values.length) {
            return;
        }
        posb.clear();
        Collections.addAll(posb, values);
        System.out.println(title + this + "onlyPosb: " + posb);
        Recorder.reducePosb(this, size - values.length);
    }
}


