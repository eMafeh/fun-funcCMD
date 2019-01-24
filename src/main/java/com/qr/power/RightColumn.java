package com.qr.power;

import java.math.BigInteger;

public class RightColumn {
    private BigInteger[] coefficient;
    private BigInteger divide;
    private BigInteger multiply;

    public RightColumn(FormulaModel model, final BigInteger multiply) {
        this.coefficient = model.coefficient;
        BigInteger gcd = model.divide.gcd(multiply);
        this.divide = model.divide.divide(gcd);
        this.multiply = multiply.divide(gcd);
    }

    public BigInteger getCoefficient(int i) {
        return coefficient[i];
    }

    public int getCoefficientLenght() {
        return coefficient.length;
    }

    public BigInteger getDivide() {
        return divide;
    }

    public BigInteger getMultiply() {
        return multiply;
    }
}
