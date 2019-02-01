/*
package com.qr.power.computer.domap;

import com.qr.power.function.MultiAlgebra;
import com.qr.power.function.RationalNumber;
import util.StringBuilderUtil;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class CoefficientB {
    final Map<MultiAlgebra, Integer> count;

    private String toString;

    CoefficientB() {
        this.count = new HashMap<>();
    }

    public String stringAlgebra() {
        if (toString != null) return toString;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        StringBuilderUtil.Splitter<Character> splitter = StringBuilderUtil.getSplitter(sb, '+');
        for (Map.Entry<MultiAlgebra, Integer> entry : count.entrySet()) {
            Integer value = entry.getValue();
            if (value > 0) {
                splitter.split();
                if (value != 1)
                    sb.append(value);
                sb.append(entry.getKey());
            }
        }
        for (Map.Entry<MultiAlgebra, Integer> entry : count.entrySet()) {
            Integer value = entry.getValue();
            if (value < 0) {
                sb.append('-');
                value = -value;
                if (value != 1)
                    sb.append(value);
                sb.append(entry.getKey());
            }
        }
        sb.append("]");
        String result = sb.toString();
        toString = result;
        return result;
    }

    private BigInteger[] value;

    public BigInteger[] bigIntegerValue() {
        if (value != null) return value;
        BigInteger bigDivide = count.keySet()
                .parallelStream()
                .map(a -> a.rationalValue()
                        .getDivide())
                .reduce(BigInteger.ONE, (a, b) -> a.multiply(b)
                        .divide(a.gcd(b)));

        BigInteger bigMultiply = BigInteger.ZERO;
        for (Map.Entry<MultiAlgebra, Integer> entry : count.entrySet()) {
            Integer v = entry.getValue();
            if (v == 0) continue;
            MultiAlgebra key = entry.getKey();
            RationalNumber kv = key.rationalValue();
            BigInteger val = BigInteger.valueOf(v);
            bigMultiply = bigMultiply.add(kv.getNumber()
                    .multiply(val)
                    .multiply(bigDivide.divide(kv.getDivide())));
        }
        BigInteger gcd = bigMultiply.gcd(bigDivide);
        bigMultiply = bigMultiply.divide(gcd);
        bigDivide = bigDivide.divide(gcd);
        BigInteger[] result = new BigInteger[]{bigMultiply, bigDivide};
        value = result;
        return result;
    }

    @Override
    public String toString() {
//        return stringAlgebra();
        BigInteger[] integers = bigIntegerValue();
        if (integers[0].equals(BigInteger.ZERO)) return "0";
        return integers[0] + "/" + integers[1];
    }
}
*/
