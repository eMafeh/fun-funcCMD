package com.qr.solo.model;

import com.qr.solo.SoloValue;

import java.util.*;

import static com.qr.solo.SoloConst.d;

/**
 * @author QianRui
 * 2018/9/28
 */
public class Cell {
    public static final Map<Integer, Cell> MAP = new HashMap<>();

    private static final Cell[][] rows = new Cell[d * d][d * d];
    private static final Cell[][] columns = new Cell[d * d][d * d];
    private static final Cell[][] boxes = new Cell[d * d][d * d];

    static {
        System.out.println("begin " + System.currentTimeMillis());
        for (int i = 0; i < d * d; i++) {
            for (int j = 0; j < d * d; j++) {
                new Cell(i, j);
            }
        }
        System.out.println("newed " + System.currentTimeMillis());
    }


    private final int row;
    private final int column;
    private final int boxNo;
    private final int index;
    private final int i;

    private final Cell[] inRow;
    private final Cell[] inBox;
    private final Cell[] inColumn;
    private final List<Cell[]> inAll;

    private boolean right;
    private Set<SoloValue> posb;
    public SoloValue value;

    private Cell(int boxNo, int index) {
        this.boxNo = boxNo;
        this.index = index;
        this.row = (boxNo / d) * d + index / d;
        this.column = (boxNo % d) * d + index % d;
        this.i = row * d * d + column;

        rows[row][column] = this;
        columns[column][row] = this;
        boxes[boxNo][index] = this;
        this.inRow = rows[row];
        this.inColumn = columns[column];
        this.inBox = boxes[boxNo];
        this.inAll = new ArrayList<>(Arrays.asList(inRow, inColumn, inBox));

        MAP.put(i, this);
    }

    public static void init(String target) {
        for (Cell cell : MAP.values()) {
            cell.posb = SoloValue.allPosb(d);
        }
        String[] split = target.split(",");
        for (int boxNo = 0; boxNo < d * d; boxNo++) {
            String[] s = split[boxNo].split("");
            for (int index = 0; index < d * d; index++) {
                Cell cell = MAP.get(((boxNo / d) * d + index / d) * d * d + (boxNo % d) * d + index % d);
                cell.setValue(SoloValue.getValue(s[index]), null);
            }
        }
    }

    public int setValue(SoloValue value, List<Cell> changeList) {
        this.value = value;
        this.right = !SoloValue.N.equals(value);
        if (!right) return 0;

        int change = 0;
        System.out.println(this + "setValue: " + value);
        change += removePosb(inRow, value, changeList, "row(" + (row + 1) + ")");
        change += removePosb(inColumn, value, changeList, "column(" + (column + 1) + ")");
        change += removePosb(inBox, value, changeList, "boxNo(" + (boxNo + 1) + ")");
        return change;
    }

    public static int removePosb(Cell[] cells, SoloValue value, List<Cell> changeList, String title, Cell... excepts) {
        int change = 0;
        for (Cell cell : cells)
            if (removePosb(cell, value, title, excepts)) {
                change++;
                if (changeList != null) changeList.add(cell);
            }
        return change;
    }

    private static boolean removePosb(Cell cell, SoloValue value, String title, Cell... excepts) {
        if (!cell.right) {
            for (Cell except : excepts) if (except == cell) return false;
            if (cell.posb.remove(value)) {
                System.out.println(title + cell + "removePosb: " + value);
                return true;
            }
        }
        return false;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getBoxNo() {
        return boxNo;
    }

    public Cell[] getInRow() {
        return inRow;
    }

    public Cell[] getInBox() {
        return inBox;
    }

    public Cell[] getInColumn() {
        return inColumn;
    }

    public List<Cell[]> getInAll() {
        return inAll;
    }

    public boolean isRight() {
        return right;
    }

    public Set<SoloValue> getPosb() {
        return posb;
    }

    public static void viewShow() {
        for (int i = 0; i < d * d * d * d; i++) {
            System.out.print(MAP.get(i).value);
            if (i % d == d - 1) {
                System.out.print(" ");
            }
            if (i % (d * d) == d * d - 1) {
                System.out.println();
            }
        }
    }

    public static void detailShow() {
        for (int i = 0; i < d * d * d * d; i++) {
            if (i % (d * d) == 0) {
                System.out.println("第 " + (i / (d * d) + 1) + "行");
            }
            Cell cell = MAP.get(i);
            System.out.println(cell.value + (cell.right ? "" : "" + cell.posb));
        }
    }

    public static String lineShow() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < d * d * d * d; i++) {
            Cell cell = MAP.get(i);
            stringBuilder.append(cell.value);
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "cell(" + (boxNo + 1) + "." + (index + 1) + ")";
    }
}
