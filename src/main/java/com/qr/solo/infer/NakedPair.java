package com.qr.solo.infer;

import com.qr.solo.model.Cell;

/**
 * 显性数对
 * 某限制组,两个格子都只能填两个数,且两个数相同,则限制组其他格子不能再填这两个数
 *
 * @author QianRui
 * 2018/9/28
 */
public enum NakedPair implements CellStrategy {
    SINGLE;

    @Override
    public void solo(Cell cell) {
        if (!cell.isRight() && cell.getPosb().size() == 2) {
            nakedPair(cell.getInRow(), cell, "NakedPair:row(" + (cell.getRow() + 1) + ")");
            nakedPair(cell.getInColumn(), cell, "NakedPair:column(" + (cell.getColumn() + 1) + ")");
            nakedPair(cell.getInBox(), cell, "NakedPair:boxNo(" + (cell.getBoxNo() + 1) + ")");
        }
    }

    private void nakedPair(Cell[] cells, Cell cell, String title) {
        for (Cell bro : cells) {
            if (bro != cell && !bro.isRight() && bro.getPosb().equals(cell.getPosb())) {
                cell.getPosb().forEach(value -> removePosb(cells, value, title, cell, bro));
                return;
            }
        }
    }
}
