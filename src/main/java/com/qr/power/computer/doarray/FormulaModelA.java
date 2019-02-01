package com.qr.power.computer.doarray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.qr.power.function.ArrangeFunctionObject.A;
import static com.qr.power.computer.doarray.CoefficientA.f;
import static com.qr.power.computer.doarray.SimpleCoefficientA.sf;
import static com.qr.power.function.TopFunctionObject.T;

public class FormulaModelA {
    private static SimpleCoefficientA[] getPascalTriangle(int power) {
        return IntStream.range(0, power + 1)
                .mapToObj(i -> sf(T(i, power)).divide(A(i)))
//                .mapToObj(i -> sf(A(power)).divide(A(power - i)).divide(A(i)))
                .toArray(SimpleCoefficientA[]::new);
    }

    private static final List<FormulaModelA> allModel = new ArrayList<>();

    public static FormulaModelA getInstance(int power) {
        if (power < 0) power = 0;
        return power < allModel.size() ? allModel.get(power) : new FormulaModelA(power);
    }

    //下标为 x 的次幂,值为 x 的系数
    private CoefficientA[] coefficientAS;

    private FormulaModelA(int power) {
        //确保下层类准备好
        if (power > allModel.size()) {
            new FormulaModelA(power - 1);
        }
        allModel.add(power, this);

        //需要高一阶的帕斯卡三角
        SimpleCoefficientA[] pascalTriangle = getPascalTriangle(power + 1);
        //左右需要除去的除数
        SimpleCoefficientA divide = pascalTriangle[power];


        //左侧构建
        coefficientAS = new CoefficientA[pascalTriangle.length];
        IntStream.range(0, pascalTriangle.length)
//                .parallel()
                .forEach(i -> coefficientAS[i] = f(pascalTriangle[i].divide(divide)));
        coefficientAS[0] = coefficientAS[0].subtract(coefficientAS[0]);


        //右侧多列表骨架构建
        CoefficientA[][] rightColumns = new CoefficientA[power][];
        IntStream.range(0, power)
//                .parallel()
                .forEach(i -> {
                    CoefficientA[] column = allModel.get(i).coefficientAS;
                    SimpleCoefficientA multiply = pascalTriangle[i].divide(divide);
                    CoefficientA[] r_coefficientAS = rightColumns[i] = new CoefficientA[column.length];
                    IntStream.range(0, column.length)
//                            .parallel()
                            .forEach(j -> r_coefficientAS[j] = column[j].multiply(multiply));
                });

        //一一去除右侧列表,这步采用减法,不能并行处理
        for (int i = 0; i < power; i++) {
            CoefficientA[] column = rightColumns[i];
            //减除待减列表
            for (int a = 0; a < column.length; a++) {
                coefficientAS[a] = coefficientAS[a].subtract(column[a]);
            }
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(coefficientAS);
    }
}