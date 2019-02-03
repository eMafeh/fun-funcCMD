package com.qr.power.computer.donumber;

import com.qr.math.PrimeNumber;
import com.qr.power.function.RationalNumber;
import util.StringBuilderUtil;
import util.StringBuilderUtil.Splitter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static com.qr.power.function.RationalNumber.rn;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class FormulaModel {
    private static final List<FormulaModel> allModel = new ArrayList<>();
    //下标为 x 的次幂,值为 x 的系数
    BigInteger[] coefficient;
    //系数表除数
    BigInteger divide;

    public static FormulaModel getInstance(int power) {
        if (power < allModel.size()) {
            return allModel.get(power);
        }
        FormulaModel formulaModel = new FormulaModel(power);
        allModel.add(power, formulaModel);
        return formulaModel;
    }


    private FormulaModel(int power) {
        //需要的新帕斯卡三角
        PascalTriangle pascalTriangle = PascalTriangle.getInstance(power + 1);
        //右侧多列表骨架构建
        List<RightColumn> rightColumns = new ArrayList<>();
        for (int i = 0; i < power; i++) {
            rightColumns.add(new RightColumn(getInstance(i), pascalTriangle.v(i)));
        }
        //获取右侧总除数,即所有除数的最小公倍数
        BigInteger acc = ONE;
        for (RightColumn rightColumn : rightColumns) {
            BigInteger rightColumnDivide = rightColumn.getDivide();
            acc = acc.multiply(rightColumnDivide)
                    .divide(acc.gcd(rightColumnDivide));
        }
        BigInteger rightDivide = acc;
        //最终总除数
        divide = rightDivide.multiply(pascalTriangle.v(power));

        //左侧方程骨架
        coefficient = pascalTriangle.getCoefficients();
        coefficient[0] = coefficient[0].subtract(BigInteger.ONE);
        //自身扩大至总除数倍数

        for (int i = 0; i < coefficient.length; i++) {
            coefficient[i] = coefficient[i].multiply(rightDivide);
        }

        //一一去除右侧列表,这步采用减法,不能并行处理
        for (int i = 0; i < power; i++) {
            RightColumn column = rightColumns.get(i);
            //待减列表需要整体扩大运算的倍数
            BigInteger multiply = rightDivide.divide(column.getDivide())
                    .multiply(column.getMultiply());
            //减除待减列表
            for (int a = 0; a < column.getCoefficientLenght(); a++) {
                coefficient[a] = coefficient[a].subtract(column.getCoefficient(a)
                        .multiply(multiply));
            }
        }
        //约去所有系数的最大公约数
        divide = PrimeNumber.divideGcd(coefficient, divide);
    }

    public BigInteger computer(int x) {
        BigInteger bigx = BigInteger.valueOf(x);
        BigInteger result = coefficient[coefficient.length - 1];
        for (int i = coefficient.length - 1; i > 0; i--) {
            result = coefficient[i - 1].add(bigx.multiply(result));
        }
        return result.divide(divide);
    }

    public void analyze() {
        BigInteger[] coefficient = this.coefficient;
        List<RationalNumber> hold = new ArrayList<>();
        coefficient = analyze0(coefficient, hold);
        coefficient = analyze1(coefficient, hold);

        coefficient = analyze2(coefficient, hold);
        System.out.println(hold);
        System.out.println(Arrays.toString(coefficient));
        for (BigInteger integer : coefficient) {
            System.out.print(PrimeNumber.toPrimeList(integer));
        }
        System.out.println();
//        System.out.println(hold);
    }


    /**
     * 去除单x项
     */
    private static BigInteger[] analyze0(BigInteger[] coefficient, List<RationalNumber> hold) {
        if (coefficient.length == 0 || !coefficient[0].equals(ZERO)) {
            return coefficient;
        }
        hold.add(rn(ZERO));
        BigInteger[] bigIntegers = new BigInteger[coefficient.length - 1];
        System.arraycopy(coefficient, 1, bigIntegers, 0, bigIntegers.length);
        return analyze0(bigIntegers, hold);
    }

    /**
     * 去除单(x+1)项
     */
    private static BigInteger[] analyze1(BigInteger[] coefficient, List<RationalNumber> hold) {
        if (coefficient.length == 0) {
            return coefficient;
        }
        BigInteger[] test = new BigInteger[coefficient.length - 1];
        BigInteger temp = ZERO;
        for (int i = 0; i < coefficient.length - 1; i++) {
            temp = test[i] = coefficient[i].subtract(temp);
        }
        if (temp.equals(coefficient[coefficient.length - 1])) {
            hold.add(rn(ONE));
            return analyze1(test, hold);
        }
        return coefficient;
    }

    private BigInteger two = BigInteger.valueOf(2);

    private BigInteger[] analyze2(final BigInteger[] coefficient, final List<RationalNumber> hold) {
        if (coefficient.length == 0) {
            return coefficient;
        }

        BigInteger[] test = new BigInteger[coefficient.length - 1];
        BigInteger temp = ZERO;
        for (int i = coefficient.length - 1; i > 0; i--) {
            //上一项化约后 这一项需要减去的 这一项实际待除数
            BigInteger that = coefficient[i].subtract(temp);
            //带约数除法
            BigInteger[] computer = that.divideAndRemainder(two);
            //存在约数说明这次尝试不合适
            if (!computer[1].equals(ZERO)) {
                return coefficient;
            }
            temp = test[i - 1] = computer[0];
        }
        //最后一项正好
        if (temp.equals(coefficient[0])) {
            hold.add(rn(two));
            return analyze2(test, hold);
        }
        return coefficient;
    }

    public String toPrimeString() {
        return toString(PrimeNumber::toPrimeString);
    }

    public String toGcdString() {
        Splitter<Character> splitter = toStringHead();
        BigInteger[] integers = new BigInteger[1];
        for (BigInteger integer : coefficient) {
            StringBuilder sb = splitter.split();
            if (integer.equals(ZERO)) {
                sb.append("0");
            } else {
                integers[0] = integer;
                BigInteger divideGcd = PrimeNumber.divideGcd(integers, divide);
                PrimeNumber.toPrimeString(sb, integers[0]);
                if (!divideGcd.equals(ONE)) {
                    PrimeNumber.toPrimeString(sb.append("/"), divideGcd);
                }
            }
        }
        return splitter.getSb()
                .append("]")
                .toString();
    }

    private String toString(BiConsumer<StringBuilder, BigInteger> adder) {
        Splitter<Character> splitter = toStringHead();
        for (BigInteger integer : coefficient) {
            adder.accept(splitter.split(), integer);
        }
        adder.accept(splitter.getSb()
                .append("]/"), divide);
        return splitter.getSb()
                .toString();
    }

    private Splitter<Character> toStringHead() {
        StringBuilder sb = new StringBuilder().append("PowerSum:         ")
                .append(coefficient.length - 2)
                .append("[");
        return StringBuilderUtil.getSplitter(sb, ',');
    }

    @Override
    public String toString() {
        return toString(StringBuilder::append);
    }
}