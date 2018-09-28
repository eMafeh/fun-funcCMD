package com.qr.solo.infer;

import com.qr.solo.SoloValue;
import com.qr.solo.model.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 隐性数对
 * 某限制区,两个数只有两个格子可以填,则这两个格子的其他填数都不可能
 *
 * @author QianRui
 * 2018/9/28
 */
public enum HiddenPair implements CellStrategy {
    SINGLE;

    @Override
    public void solo(Cell cell) {
        if (!cell.isRight()) {
            hiddenPair(cell.getInRow(), cell, "row(" + (cell.getRow() + 1) + ")");
            hiddenPair(cell.getInColumn(), cell, "column(" + (cell.getColumn() + 1) + ")");
            hiddenPair(cell.getInBox(), cell, "boxNo(" + (cell.getBoxNo() + 1) + ")");
        }
    }

    private void hiddenPair(Cell[] column, Cell cell, String title) {
        Map<SoloValue, List<Cell>> map = SoloValue.toPosbMap(column);
        Map<SoloValue, List<Cell>> pair = new HashMap<>();
        for (Map.Entry<SoloValue, List<Cell>> entry : map.entrySet()) {
            List<Cell> candidate = entry.getValue();
            SoloValue value = entry.getKey();
            int size = candidate.size();
            if (size == 1) {
                setValue(candidate.get(0), value);
            } else if (size == 2 && candidate.contains(cell)) {
                pair.put(value, candidate);
            }
        }
        if (pair.size() > 1) {
            for (Map.Entry<SoloValue, List<Cell>> entry : pair.entrySet()) {
                List<Cell> cells = entry.getValue();
                for (Map.Entry<SoloValue, List<Cell>> other : pair.entrySet()) {
                    List<Cell> others = other.getValue();
                    if (cells != others && cells.equals(others)) {
                        for (Cell c : cells) {
                            onlyPosb(c, title, entry.getKey(), other.getKey());
                        }
                    }
                }
            }
        }

    }


}
