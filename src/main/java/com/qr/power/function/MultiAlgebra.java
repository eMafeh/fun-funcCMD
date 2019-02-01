package com.qr.power.function;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class MultiAlgebra implements Algebra {
    final TreeMap<Algebra, Integer> count = new TreeMap<>();

    private int id;

    MultiAlgebra(Map<Algebra, Integer> count) {
        this.count.putAll(count);
    }

    @Override
    public int id() {

        return hashCode();
    }

    private RationalNumber values;

    @Override
    public RationalNumber rationalValue() {
        if (values == null) {
            BigInteger multiply = BigInteger.ONE;
            BigInteger divide = BigInteger.ONE;

            for (Map.Entry<Algebra, Integer> entry : count.entrySet()) {
                Integer v = entry.getValue();
                if (v == 0) continue;
                Algebra key = entry.getKey();
                RationalNumber ki = key.rationalValue();
                if (v > 0) {
                    multiply = multiply.multiply(ki.getNumber()
                            .pow(v));
                    divide = divide.multiply(ki.getDivide()
                            .pow(v));
                } else {
                    multiply = multiply.multiply(ki.getDivide()
                            .pow(-v));
                    divide = divide.multiply(ki.getNumber()
                            .pow(-v));
                }
            }
            values = RationalNumber.rn(multiply, divide);
        }
        return values;
    }

    @Override
    public String toString() {
        return rationalValue().toString();
    }

}
