package com.qr.power.computer.doarray;

import com.qr.power.function.Algebra;
import com.qr.power.function.ArrangeFunctionObject;
import util.ArraysUtil;
import util.StringBuilderUtil;

public class SimpleCoefficientA {

    private static final Algebra[] EMPTY_ALGEBRA = new Algebra[0];
    private final Algebra[] multiply;
    private final Algebra[] divide;


    private SimpleCoefficientA(Algebra[] multiply, Algebra[] divide) {
        this.multiply = ArraysUtil.sort(multiply);
        this.divide = ArraysUtil.sort(divide);
    }

    public static SimpleCoefficientA GCD(SimpleCoefficientA b) {
        return null;
    }

    public static SimpleCoefficientA sf(Algebra multiply) {
        return new SimpleCoefficientA(multiply != null ? new Algebra[]{multiply} : EMPTY_ALGEBRA, EMPTY_ALGEBRA);
    }

    public SimpleCoefficientA multiply(SimpleCoefficientA b) {
        return computer(multiply, b.multiply, divide, b.divide);
    }

    public SimpleCoefficientA divide(SimpleCoefficientA b) {
        return computer(multiply, b.divide, divide, b.multiply);
    }

    public SimpleCoefficientA multiply(Algebra b) {
        return computer(multiply, new Algebra[]{b}, divide, EMPTY_ALGEBRA);
    }

    public SimpleCoefficientA divide(Algebra b) {
        return computer(multiply, EMPTY_ALGEBRA, divide, new Algebra[]{b});
    }

    //直接根据 代数组 构建系数 消去分子分母相同项 剔除 null 项
    private static SimpleCoefficientA computer(Algebra[] multiply1, Algebra[] multiply2, Algebra[] divide1, Algebra[] divide2) {
        Algebra[] multiplies = ArraysUtil.copy(multiply1, multiply2);
        Algebra[] divides = ArraysUtil.copy(divide1, divide2);
//        Algebra[] union = ArraysUtil.union(multiplies, divides);
//        Algebra[] multiply = ArraysUtil.difference(multiplies, union);
//        Algebra[] divide = ArraysUtil.difference(divides, union);
//        return new SimpleCoefficientA(multiply, divide);
        return new SimpleCoefficientA(multiplies, divides);
    }

    static ArrangeFunctionObject A0 = ArrangeFunctionObject.A(0);
    static ArrangeFunctionObject A1 = ArrangeFunctionObject.A(1);

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SimpleCoefficientA)) return false;
        SimpleCoefficientA that = (SimpleCoefficientA) obj;
        if (multiply.length != that.multiply.length) return false;
        if (divide.length != that.divide.length) return false;
        for (int i = 0; i < multiply.length; i++) {
            if (that.multiply[i] != multiply[i]) return false;
        }
        for (int i = 0; i < divide.length; i++) {
            if (that.divide[i] != divide[i]) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (multiply.length > 0) {
            StringBuilderUtil.Splitter<Character> splitter = StringBuilderUtil.getSplitter(sb, '*');
            for (Algebra algebra : multiply)
                splitter.split()
                        .append(algebra);
        } else sb.append('1');
        if (divide.length > 0)
            for (Algebra algebra : divide)
                sb.append("/")
                        .append(algebra);
        return sb.toString();
    }
}