package com.qr.power;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PascalTriangle {
    private static final List<PascalTriangle> triangles = new ArrayList<>();
    BigInteger[] coefficient;

    private PascalTriangle(PascalTriangle pascalTriangle) {

        int length = pascalTriangle.coefficient.length;
        this.coefficient = new BigInteger[length];
        System.arraycopy(pascalTriangle.coefficient, 0, this.coefficient, 0, length);
    }

    private PascalTriangle(int power) {
        if (power > triangles.size()) {
            new PascalTriangle(power - 1);
        }
        triangles.add(power, this);
        this.coefficient = new BigInteger[power + 1];
        this.coefficient[0] = BigInteger.ONE;
        if (power > 0) {
            BigInteger[] previous = triangles.get(power - 1).coefficient;
            for (int i = 0; i <= previous.length; i++) {
                this.coefficient[i] = getValue(previous, i - 1).add(getValue(previous, i));
            }
        }
    }

    static PascalTriangle newInstance(int index) {
        if (index < 0) index = 0;
        PascalTriangle triangle = index < triangles.size() ? triangles.get(index) : new PascalTriangle(index);
        return new PascalTriangle(triangle);
    }

    private BigInteger getValue(final BigInteger[] previous, final int i) {
        return i >= 0 && i < previous.length ? previous[i] : BigInteger.ZERO;
    }

    @Override
    public String toString() {
        return "PascalTriangle:   " + (coefficient.length - 1) + Arrays.toString(coefficient);
    }

}