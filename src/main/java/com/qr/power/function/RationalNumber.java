package com.qr.power.function;

import java.math.BigInteger;

public class RationalNumber {
    private final BigInteger number;
    private final BigInteger divide;

    private RationalNumber(BigInteger number, BigInteger divide) {
        this.number = number;
        this.divide = divide;
    }

    public static RationalNumber rn(BigInteger number, BigInteger divide) {
        if (divide == null || divide.signum() == 0) {
            throw new RuntimeException("divide can not be 0");
        }
        if (number == null) {
            return new RationalNumber(BigInteger.ZERO, BigInteger.ONE);
        }
        BigInteger gcd = divide.signum() < 0 ? number.gcd(divide) : number.gcd(divide)
                .negate();
        return new RationalNumber(number.divide(gcd), divide.divide(gcd));
    }

    public static RationalNumber rn(BigInteger number) {
        return rn(number, BigInteger.ONE);
    }

    public BigInteger getNumber() {
        return number;
    }

    public BigInteger getDivide() {
        return divide;
    }

    @Override
    public String toString() {
        if (divide.equals(BigInteger.ONE)) return number + "";
        return number + "/" + divide;
    }
}
