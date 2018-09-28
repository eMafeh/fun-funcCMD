package com.qr.solo.infer;

import com.qr.solo.SoloValue;
import com.qr.solo.model.Cell;

import java.util.HashSet;

/**
 * 排除
 * 某个限制组内,可以填的某个数只有这个格子可以填
 *
 * @author QianRui
 * 2018/9/28
 */
public enum HiddenSingle implements CellStrategy {
    SINGLE;

    @Override
    public void solo(Cell cell) {
        hiddenSingle(cell.getInRow(), cell);
        hiddenSingle(cell.getInColumn(), cell);
        hiddenSingle(cell.getInBox(), cell);
    }

    private void hiddenSingle(Cell[] bros, Cell cell) {
        if (cell.isRight()) {
            return;
        }
        HashSet<SoloValue> posb = new HashSet<>(cell.getPosb());
        for (Cell bro : bros) {
            if (cell != bro && !bro.isRight()) {
                posb.removeAll(bro.getPosb());
            }
            if (posb.size() == 0) {
                return;
            }
        }
        setValue(cell, posb.iterator().next());
    }
}
