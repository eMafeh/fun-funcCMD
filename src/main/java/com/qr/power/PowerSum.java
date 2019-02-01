package com.qr.power;

import com.qr.power.computer.donumber.FormulaModel;
import com.qr.power.function.ArrangeFunctionObject;
import com.qr.power.function.TopFunctionObject;
import util.StringSplitUtil;

import java.math.BigInteger;
import java.util.Scanner;

import static com.qr.power.function.Algebra.power;

public class PowerSum {
    public static void main(String[] args) {
//        TEST();
//        FUNCTION();
        FIRST();
    }

    private static void TEST() {

        for (int i = 0; i < power; i++) {
            ArrangeFunctionObject.A(i);
            for (int j = 0; j < i; j++) {
                TopFunctionObject.T(j, i);
            }
        }
//        PascalTriangle.newInstance(5000);
    }

    public static void FUNCTION() {
      /*  for (int i = 0; i < power; i++) {
            FormulaModelB instance = FormulaModelB.getInstance(i);
//            System.out.println(i);
            System.out.println(instance);
        }*/

    }

    public static void FIRST() {
        Scanner sc = new Scanner(System.in);
        long l = System.currentTimeMillis();
        FormulaModel instance = FormulaModel.getInstance(power);
        System.out.println("prepare " + power + " in (" + (System.currentTimeMillis() - l) + "ms)");
        l = System.currentTimeMillis();
        BigInteger computer = instance.computer(power);
        System.out.println("computer x=" + power + " (" + (System.currentTimeMillis() - l) + "ms)=> " + computer);
        l = System.currentTimeMillis();
        BigInteger result = BigInteger.ZERO;
        for (int i = 1; i <= power; i++) {
            result = result.add(BigInteger.valueOf(i)
                    .pow(power));
        }
        System.out.println((System.currentTimeMillis() - l) + "ms " + result);
        while (true) {
            String s = sc.nextLine();
            String[] s1 = StringSplitUtil.maxSplitWords(s, 2);
            FormulaModel formulaModel = FormulaModel.getInstance(Integer.parseInt(s1[0]));
            if ("1".equals(s1[1]))
                System.out.println(formulaModel.toPrimeString());
            else if ("2".equals(s1[1])) {
                System.out.println(formulaModel.toGcdString());
            } else {
                System.out.println(formulaModel);
            }
        }
    }

}
