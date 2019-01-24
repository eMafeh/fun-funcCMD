package com.qr.math;

import java.math.BigInteger;

public class PrimeNumber {
    private static final int MAX = 1_000_000;//is a PrimeNumber
    private static final boolean[] is = new boolean[MAX + 1];
    private static final BigInteger TWO = BigInteger.valueOf(2);

    static {
        is[2] = true;
        for (int i = 3; i <= MAX; i += 2)
            is[i] = is0(i);
    }

    public static boolean is(int i) {
        return is[i];
    }

    public static StringBuilder toPrimeString(StringBuilder sb, BigInteger integer) {
        int n = integer.compareTo(BigInteger.ZERO);
        if (n == 0) {
            return sb.append("0");
        }
        if (n < 0) {
            sb.append("-");
            integer = integer.abs();
        }
        boolean begin = true;
        while (true) {
            BigInteger[] bigIntegers = integer.divideAndRemainder(TWO);
            if (bigIntegers[1].equals(BigInteger.ZERO)) {
                if (!begin) sb.append("*");
                else begin = false;
                sb.append("2");
                integer = bigIntegers[0];
            } else {
                break;
            }
        }
        for (int i = 3; i <= MAX && is[i]; i += 2) {
            BigInteger prime = BigInteger.valueOf(i);
//            if (integer.compareTo(prime.multiply(prime)) > 0) {
//                break;
//            }
            while (true) {
                BigInteger[] bigIntegers = integer.divideAndRemainder(prime);
                if (bigIntegers[1].equals(BigInteger.ZERO)) {
                    if (!begin) sb.append("*");
                    else begin = false;
                    sb.append(i);
                    integer = bigIntegers[0];
                } else {
                    break;
                }
            }
        }
        return begin ? sb.append(integer) : integer.equals(BigInteger.ONE) ? sb : sb.append("*")
                .append(integer);
    }

    private static boolean is0(int i) {
        for (int j = 3; is[j] && j * j <= i; j += 2)
            if (i % j == 0) return false;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(toPrimeString(new StringBuilder(), BigInteger.valueOf(11108669)));
    }
}
