package com.qr.power.function;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class ArrangeFunctionObject implements Algebra {
    private final int subscript;
    private RationalNumber values;
    private static ArrangeFunctionObject[] CACHE = new ArrangeFunctionObject[power + 1];

    static {
        IntStream.rangeClosed(0, power)
                .parallel()
                .forEach(i -> CACHE[i] = new ArrangeFunctionObject(i));
        CACHE[0].values = RationalNumber.rn(null);
    }

    private ArrangeFunctionObject(int subscript) {
        this.subscript = subscript;
    }

    public static ArrangeFunctionObject A(int subscript) {
        return CACHE[subscript];
    }

    @Override
    public int id() {
        return subscript;
    }

    @Override
    public RationalNumber rationalValue() {
        if (values == null)
            values = RationalNumber.rn(BigInteger.valueOf(subscript)
                    .multiply(CACHE[subscript - 1].rationalValue()
                            .getNumber()));
        return values;
    }

    @Override
    public String toString() {
        return "A" + subscript;
    }
}
