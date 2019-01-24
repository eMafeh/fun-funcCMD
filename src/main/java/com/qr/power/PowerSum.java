package com.qr.power;

import java.math.BigInteger;

public class PowerSum {
    public static void main(String[] args) {
        int x = 10000;
        int power = 150;
        long l = System.currentTimeMillis();
        FormulaModel instance = FormulaModel.getInstance(power);
        System.out.println("prepare " + power + " in (" + (System.currentTimeMillis() - l) + "ms)");
        System.out.println(instance);
        l = System.currentTimeMillis();
        BigInteger computer = instance.computer(x);
        System.out.println("computer x=" + x + " (" + (System.currentTimeMillis() - l) + "ms)=> " + computer);
        l = System.currentTimeMillis();
        BigInteger result = BigInteger.ZERO;
        for (int i = 1; i <= x; i++) {
            result = result.add(BigInteger.valueOf(i).pow(power));
        }
        System.out.println((System.currentTimeMillis() - l) + "ms " + result);

    }


}
