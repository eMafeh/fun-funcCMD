package com.qr.math;

import util.StringBuilderUtil;
import util.StringBuilderUtil.Splitter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class PrimeNumber {
    private static final int MAX = 1_000_000;
    private static final boolean[] is = new boolean[MAX + 1];
    private static final List<BigInteger> PRIME_NUMBER;

    static {
        is[2] = true;
        for (int i = 3; i <= MAX; i += 2)
            is[i] = is0(i);
        PRIME_NUMBER = IntStream.range(2, MAX)
                .filter(PrimeNumber::is)
                .mapToObj(BigInteger::valueOf)
                .collect(Collectors.toList());
    }

    public static boolean is(int i) {
        return is[i];
    }

    public static BigInteger divideGcd(BigInteger[] integers, BigInteger first) {
        BigInteger gcd = first = first == null ? ZERO : first;
        for (BigInteger integer : integers) {
            gcd = gcd.gcd(integer);
            if (gcd.equals(ONE)) return first;
        }
        for (int i = 0; i < integers.length; i++) {
            integers[i] = integers[i].divide(gcd);
        }
        return first.divide(gcd);
    }

    public static StringBuilder toPrimeString(StringBuilder sb, BigInteger integer) {
        int n = integer.compareTo(ZERO);
        if (n == 0) {
            return sb.append("0");
        }
        if (n < 0) {
            sb.append("-");
            integer = integer.abs();
        }
        List<BigInteger> primeList = toPrimeList(integer);
        if (primeList.size() == 1) {
            return sb.append(integer);
        }
        Splitter<Character> splitter = StringBuilderUtil.getSplitter(sb, '*');
        for (BigInteger prime : primeList) {
            splitter.split()
                    .append(prime);
        }
        return sb;
    }

    private static boolean is0(int i) {
        for (int j = 3; is[j] && j * j <= i; j += 2)
            if (i % j == 0) return false;
        return true;
    }

    public static List<BigInteger> toPrimeList(BigInteger integer) {
        boolean negative = false;
        if (integer.signum() < 0) {
            integer = integer.abs();
            negative = true;
        }
        List<BigInteger> result = new ArrayList<>();
        for (BigInteger prime : PRIME_NUMBER) {
            if (integer.compareTo(prime.pow(2)) < 0) break;
            for (BigInteger[] dna = integer.divideAndRemainder(prime); dna[1].equals(ZERO); ) {
                result.add(prime);
                integer = dna[0];
                dna = integer.divideAndRemainder(prime);
            }
        }
        if (!negative) {
            if (!integer.equals(ONE))
                result.add(integer);
        } else result.add(integer.negate());
        return result;
    }
}
