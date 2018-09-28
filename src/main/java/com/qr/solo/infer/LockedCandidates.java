package com.qr.solo.infer;

import com.qr.solo.SoloValue;
import com.qr.solo.model.Cell;

import java.util.List;
import java.util.Map;

/**
 * 区块排除
 * 某个限制组的某个数必在两个格子,使这两个格子所在的其他限制组的其他格子不能填这个数
 *
 * @author QianRui
 * 2018/9/28
 */
public enum LockedCandidates implements CellStrategy {
    SINGLE;

    @Override
    public void solo(Cell cell) {
        lockedCandidates(cell.getInRow(), false);
        lockedCandidates(cell.getInColumn(), false);
        lockedCandidates(cell.getInBox(), true);
    }

    private void lockedCandidates(Cell[] cells, boolean box) {
        Map<SoloValue, List<Cell>> map = SoloValue.toPosbMap(cells);
        for (Map.Entry<SoloValue, List<Cell>> entry : map.entrySet()) {
            List<Cell> candidate = entry.getValue();
            SoloValue value = entry.getKey();
            int size = candidate.size();
            if (size == 1) {
                setValue(candidate.get(0), value);
            } else if (size == 2) {
                Cell c1 = candidate.get(0);
                Cell c2 = candidate.get(1);
                if (box) {
                    //行去除
                    if (c1.getRow() == c2.getRow()) {
                        removePosb(c1.getInRow(), value, "lockedCandidates:row(" + (c1.getRow() + 1) + ")", c1, c2);
                    }
                    //列去除
                    if (c1.getColumn() == c2.getColumn()) {
                        removePosb(c1.getInColumn(), value, "lockedCandidates:column(" + (c1.getColumn() + 1) + ")", c1, c2);
                    }
                } else {
                    //宫去除
                    if (c1.getBoxNo() == c2.getBoxNo()) {
                        removePosb(c1.getInBox(), value, "lockedCandidates:boxNo(" + (c1.getBoxNo() + 1) + ")", c1, c2);
                    }
                }
            }
        }
    }
}
