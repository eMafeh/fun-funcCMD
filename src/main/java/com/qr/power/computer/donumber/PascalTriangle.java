package com.qr.power.computer.donumber;

import util.ArraysUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class PascalTriangle {
    private static final List<PascalTriangle> triangles = new ArrayList<>();
    private BigInteger[] coefficients;

    private PascalTriangle(int power) {
        if (power > triangles.size()) {
            new PascalTriangle(power - 1);
        }
        triangles.add(power, this);

        coefficients = new BigInteger[power + 1];
        if (power > 0) {
            PascalTriangle previous = triangles.get(power - 1);
            IntStream.rangeClosed(0, previous.coefficients.length)
                    .parallel()
                    .forEach(i -> coefficients[i] = previous.v(i - 1)
                            .add(previous.v(i)));
        } else {
            coefficients[0] = BigInteger.ONE;
        }
    }

    static PascalTriangle getInstance(int index) {
        return index < triangles.size() ? triangles.get(index) : new PascalTriangle(index);
    }

    BigInteger[] getCoefficients() {
        return ArraysUtil.copy(coefficients);
    }

    BigInteger v(int i) {
        return i >= 0 && i < coefficients.length ? coefficients[i] : BigInteger.ZERO;
    }

    @Override
    public String toString() {
        return "PascalTriangle:   " + (coefficients.length - 1) + Arrays.toString(coefficients);
    }

}