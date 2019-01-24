package com.qr.power;

import com.qr.math.PrimeNumber;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FormulaModel {
    private static final List<FormulaModel> allModel = new ArrayList<>();

    public static FormulaModel getInstance(int power) {
        if (power < 0) power = 0;
        return power < allModel.size() ? allModel.get(power) : new FormulaModel(power);
    }

    //下标为 x 的次幂,值为 x 的系数
    BigInteger[] coefficient;
    //系数表总体除数
    BigInteger divide;

    private FormulaModel(int power) {
        //确保下层类准备好
        if (power > allModel.size()) {
            new FormulaModel(power - 1);
        }
        allModel.add(power, this);

        //需要的新帕斯卡三角
        BigInteger[] pascalTriangle = PascalTriangle.newInstance(power + 1).coefficient;
        //右侧多列表骨架构建
        List<RightColumn> rightColumns = IntStream.range(0, power)
                .parallel()
                .mapToObj(i -> new RightColumn(allModel.get(i), pascalTriangle[i]))
                .collect(Collectors.toList());
        //获取右侧总除数,即所有除数的最小公倍数
        BigInteger rightDivide = rightColumns.parallelStream()
                .map(RightColumn::getDivide)
                .reduce(BigInteger.ONE, (a, b) -> a.multiply(b)
                        .divide(a.gcd(b)));
        //最终总除数
        divide = rightDivide.multiply(pascalTriangle[power]);

        //左侧方程骨架,复用已经使用完毕的杨辉三角
        pascalTriangle[0] = pascalTriangle[0].subtract(BigInteger.ONE);
        coefficient = pascalTriangle;

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
        gcdField();
        System.out.println(this);
    }

    private void gcdField() {
        BigInteger a = divide;
        for (BigInteger c : coefficient) {
            a = a.gcd(c);
            if (a.equals(BigInteger.ONE)) {
                return;
            }
        }
//        System.out.println("gcdField " + (coefficient.length - 2) + " : " + a);
        for (int i = 0; i < coefficient.length; i++) {
            coefficient[i] = coefficient[i].divide(a);
        }
        divide = divide.divide(a);
    }


    public BigInteger computer(int x) {
        BigInteger result = BigInteger.ZERO;
        BigInteger bigx = BigInteger.valueOf(x);
        for (int i = 0; i < coefficient.length; i++) {
            result = result.add(coefficient[i].multiply(bigx.pow(i)));
        }
        return result.divide(divide);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder().append("PowerSum:         ")
                .append(coefficient.length - 2)
                .append("[");
        boolean begin = true;
        for (BigInteger integer : coefficient) {
            if (!begin) sb.append(",");
            else begin = false;
            PrimeNumber.toPrimeString(sb, integer);
        }
        sb.append("]/");
        PrimeNumber.toPrimeString(sb, divide);
        return sb.toString();
    }
}