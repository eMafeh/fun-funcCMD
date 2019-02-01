package com.qr.power.computer.donumber;

import com.qr.math.PrimeNumber;
import util.StringBuilderUtil;
import util.StringBuilderUtil.Splitter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class FormulaModel {
    private static final List<FormulaModel> allModel = new ArrayList<>();
    //下标为 x 的次幂,值为 x 的系数
    BigInteger[] coefficient;
    //系数表总体除数
    BigInteger divide;
    public static FormulaModel getInstance(int power) {
        if (power < 0) power = 0;
        return power < allModel.size() ? allModel.get(power) : new FormulaModel(power);
    }



    private FormulaModel(int power) {
        //确保下层类准备好
        if (power > allModel.size()) {
            new FormulaModel(power - 1);
        }
        allModel.add(power, this);

        //需要的新帕斯卡三角
        PascalTriangle pascalTriangle = PascalTriangle.getInstance(power + 1);
        //右侧多列表骨架构建
        List<RightColumn> rightColumns = IntStream.range(0, power)
                .parallel()
                .mapToObj(i -> new RightColumn(allModel.get(i), pascalTriangle.getValue(i)))
                .collect(Collectors.toList());
        //获取右侧总除数,即所有除数的最小公倍数
        BigInteger rightDivide = rightColumns.parallelStream()
                .map(RightColumn::getDivide)
                .reduce(BigInteger.ONE, (a, b) -> a.multiply(b)
                        .divide(a.gcd(b)));
        //最终总除数
        divide = rightDivide.multiply(pascalTriangle.getValue(power));

        //左侧方程骨架
        coefficient = pascalTriangle.getCoefficients();
        coefficient[0] = coefficient[0].subtract(BigInteger.ONE);
        //自身扩大至总除数倍数
        IntStream.range(0, coefficient.length)
                .parallel()
                .forEach(i -> coefficient[i] = coefficient[i].multiply(rightDivide));

        //一一去除右侧列表,这步采用减法,不能并行处理
        for (int i = 0; i < power; i++) {
            RightColumn column = rightColumns.get(i);
            //待减列表需要整体扩大运算的倍数
            BigInteger multiply = rightDivide.divide(column.getDivide())
                    .multiply(column.getMultiply());
            //减除待减列表
            IntStream.range(0, column.getCoefficientLenght())
                    .parallel()
                    .forEach(a -> coefficient[a] = coefficient[a].subtract(column.getCoefficient(a)
                            .multiply(multiply)));
        }
        //约去所有系数的最大公约数
        divide = PrimeNumber.divideGcd(coefficient, divide);
    }

    public BigInteger computer(int x) {
        BigInteger result = ZERO;
        BigInteger bigx = BigInteger.valueOf(x);
        for (int i = 0; i < coefficient.length; i++) {
            result = result.add(coefficient[i].multiply(bigx.pow(i)));
        }
        return result.divide(divide);
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