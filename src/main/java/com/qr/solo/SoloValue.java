package com.qr.solo;

import com.qr.solo.model.Cell;

import java.util.*;

/**
 * @author QianRui
 * 2018/9/28
 */
public enum SoloValue {
    /**
     * 空
     */
    N(" "),

    /**
     * 1-9
     */
    _1("1"), _2("2"), _3("3"), _4("4"), _5("5"), _6("6"), _7("7"), _8("8"), _9("9"),

    /**
     * 10-16
     */
    a("a"), b("b"), c("c"), d("d"), e("e"), f("f"), g("g"),
    ;


    private String value;

    SoloValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SoloValue getValue(String achar) {
        switch (achar) {
            case "1":
                return _1;
            case "2":
                return _2;
            case "3":
                return _3;
            case "4":
                return _4;
            case "5":
                return _5;
            case "6":
                return _6;
            case "7":
                return _7;
            case "8":
                return _8;
            case "9":
                return _9;
            case "a":
                return a;
            case "b":
                return b;
            case "c":
                return c;
            case "d":
                return d;
            case "e":
                return e;
            case "f":
                return f;
            case "g":
                return g;
            default:
                return N;
        }
    }

    public static Map<SoloValue, List<Cell>> toPosbMap(Cell[] cells) {
        Map<SoloValue, List<Cell>> map = new HashMap<>();
        for (Cell cell : cells) {
            if (!cell.isRight()) {
                for (SoloValue soloValue : cell.getPosb()) {
                    map.computeIfAbsent(soloValue, a -> new ArrayList<>()).add(cell);
                }
            }
        }
        return map;
    }

    public static Set<SoloValue> allPosb(int dimension) {
        switch (dimension) {
            case 3:
                return new HashSet<>(Arrays.asList(_1, _2, _3, _4, _5, _6, _7, _8, _9));
            case 4:
                return new HashSet<>(Arrays.asList(_1, _2, _3, _4, _5, _6, _7, _8, _9, a, b, c, d, e, f, g));
            default:
                throw new RuntimeException("not support this dimension" + dimension);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
