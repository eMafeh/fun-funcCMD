/*
package com.qr.power.computer.domap;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.qr.power.computer.domap.DynamicCoefficientB.c;
import static com.qr.power.function.ArrangeFunctionObject.A;
import static com.qr.power.function.TopFunctionObject.T;

public class FormulaModelB {
    private static SimpleCoefficientB[] getPascalTriangle(int power) {
        return IntStream.range(0, power + 1)
                .mapToObj(i -> sc().multiply(T(i, power))
                        .divide(A(i)))
//                .mapToObj(i -> sc().multiply(A(power)).divide(A(power - i)).divide(A(i)))
                .toArray(SimpleCoefficientB[]::new);
    }

    private static final List<FormulaModelB> allModel = new ArrayList<>();

    public static FormulaModelB getInstance(int power) {
        if (power < 0) power = 0;
        return power < allModel.size() ? allModel.get(power) : new FormulaModelB(power);
    }

    //下标为 x 的次幂,值为 x 的系数
    private DynamicCoefficientB[] dynamicCoefficientBS;

    private FormulaModelB(int power) {
        //确保下层类准备好
        if (power > allModel.size()) {
            new FormulaModelB(power - 1);
        }
        allModel.add(power, this);

        //需要高一阶的帕斯卡三角
        SimpleCoefficientB[] pascalTriangle = getPascalTriangle(power + 1);
        //左右需要除去的除数
        // 该参数静态化
        SimpleCoefficientB divide = pascalTriangle[power];//.beFinal() TODO
        //左侧构建
        dynamicCoefficientBS = new DynamicCoefficientB[power + 2];
        for (int i = 0; i < power + 2; i++) {
            //整个帕斯卡三角除以结果项系数
            pascalTriangle[i] = pascalTriangle[i].divide(divide);
            //使用copy构建左侧骨架
            dynamicCoefficientBS[i] = c().add(pascalTriangle[i]);
        }
        //第一项减去自身
        dynamicCoefficientBS[0] = c();

        for (int i = 0; i < power; i++) {
            DynamicCoefficientB[] column = allModel.get(i).dynamicCoefficientBS;
            SimpleCoefficientB coefficient = pascalTriangle[i];
            for (int j = 0; j < column.length; j++) {
                //左侧对应项减去该值
                DynamicCoefficientB multiply = column[j].multiply(coefficient);
                dynamicCoefficientBS[j] = dynamicCoefficientBS[j].subtract(multiply);
            }
        }
        //结果集静态化
        for (int i = 0; i < power + 2; i++) dynamicCoefficientBS[i].beFinal();
    }

    @Override
    public String toString() {
        return (dynamicCoefficientBS.length - 2) + Arrays.toString(dynamicCoefficientBS);
    }
}*/
