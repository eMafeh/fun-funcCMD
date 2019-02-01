package com.qr.power.function;

import java.util.stream.IntStream;

import static com.qr.power.function.ArrangeFunctionObject.A;

public class TopFunctionObject implements Algebra {
    private final int superscript;
    private final int subscript;
    private final int id;
    private RationalNumber values;
    private static TopFunctionObject[][] CACHE = new TopFunctionObject[power + 1][];

    static {
        IntStream.rangeClosed(0, power)
                .parallel()
                .forEach(i -> {
                    TopFunctionObject[] objects = CACHE[i] = new TopFunctionObject[i + 1];
                    IntStream.rangeClosed(0, i)
                            .parallel()
                            .forEach(j -> objects[j] = new TopFunctionObject(j, i));
                });
    }

    private TopFunctionObject(final int superscript, final int subscript) {
        this.superscript = superscript;
        this.subscript = subscript;
        this.id = subscript * subscript + superscript;
    }

    public static TopFunctionObject T(int i1, int i2) {
        return CACHE[i2][i1];
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public RationalNumber rationalValue() {
        if (values == null)
            values = RationalNumber.rn(A(subscript).rationalValue()
                    .getNumber()
                    .divide(A(subscript - superscript).rationalValue()
                            .getNumber()));
        return values;
    }

    @Override
    public String toString() {
        return "T" + superscript + "_" + subscript;
    }

    public static void main(String[] args) {
        System.out.println(T(3, 4).values);
        System.out.println(T(2, 4).values);
        System.out.println(T(3, 5).values);
        System.out.println(T(0, 5).values);
        System.out.println(T(1, 5).values);
        System.out.println(T(5, 5).values);
        System.out.println(T(0, 0).values);

        System.out.println(A(0).rationalValue());
        System.out.println(A(1).rationalValue());
        System.out.println(A(2).rationalValue());
        System.out.println(A(4).rationalValue());
    }
}
