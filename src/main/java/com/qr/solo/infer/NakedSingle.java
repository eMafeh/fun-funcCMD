package com.qr.solo.infer;

import com.qr.solo.model.Cell;

/**
 * 唯一余数
 * 某个格子只有唯一一个可能性,则填入
 *
 * @author QianRui
 * 2018/9/28
 */
public enum NakedSingle implements CellStrategy {
    SINGLE;

    @Override
    public void solo(Cell cell) {
        if (!cell.isRight() && cell.getPosb().size() == 1)
            setValue(cell, cell.getPosb().iterator().next());
    }
}
