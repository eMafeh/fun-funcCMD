package com.qr.power.computer.doarray;

import util.ArraysUtil;
import util.StringBuilderUtil;

public class CoefficientA {
    private static final SimpleCoefficientA[] EMPTY_SIMPLE_COEFFICIENT = new SimpleCoefficientA[0];
    private final SimpleCoefficientA[] add;
    private final SimpleCoefficientA[] subtract;

    private CoefficientA(SimpleCoefficientA[] add, SimpleCoefficientA[] subtract) {
        this.add = ArraysUtil.sort(add);
        this.subtract = ArraysUtil.sort(subtract);
    }

    public static CoefficientA f(SimpleCoefficientA coefficient) {
        return new CoefficientA(coefficient == null ? EMPTY_SIMPLE_COEFFICIENT : new SimpleCoefficientA[]{coefficient}, EMPTY_SIMPLE_COEFFICIENT);
    }

    private static CoefficientA computer(SimpleCoefficientA[] addList1, SimpleCoefficientA[] addList2, SimpleCoefficientA[] subtractList1, SimpleCoefficientA[] subtractList2) {
        SimpleCoefficientA[] adds = ArraysUtil.copy(addList1, addList2);
        SimpleCoefficientA[] subtracts = ArraysUtil.copy(subtractList1, subtractList2);
//        SimpleCoefficientA[] union = ArraysUtil.union(adds, subtracts);
//        SimpleCoefficientA[] add = ArraysUtil.difference(adds, union);
//        SimpleCoefficientA[] subtract = ArraysUtil.difference(subtracts, union);
//        return new CoefficientA(add, subtract);
        return new CoefficientA(adds, subtracts);
    }


    public CoefficientA add(CoefficientA coefficientA) {
        return computer(add, coefficientA.add, subtract, coefficientA.subtract);
    }

    public CoefficientA subtract(CoefficientA coefficientA) {
        return computer(add, coefficientA.subtract, subtract, coefficientA.add);
    }

    public CoefficientA multiply(SimpleCoefficientA coefficient) {
        SimpleCoefficientA[] newAdd = new SimpleCoefficientA[add.length];
        SimpleCoefficientA[] newSubtract = new SimpleCoefficientA[subtract.length];
        for (int i = 0; i < add.length; i++) {
            newAdd[i] = add[i].multiply(coefficient);
        }
        for (int i = 0; i < subtract.length; i++) {
            newSubtract[i] = subtract[i].multiply(coefficient);
        }
        return new CoefficientA(newAdd, newSubtract);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(add.length)
                .append(":")
                .append(subtract.length);
        sb.append("[");
        StringBuilderUtil.Splitter<String> splitter = StringBuilderUtil.getSplitter(sb, " + ");
        for (SimpleCoefficientA coefficient : add)
            splitter.split()
                    .append(coefficient);
        for (SimpleCoefficientA coefficient : subtract)
            sb.append(" - ")
                    .append(coefficient);
        return sb.append(']')
                .toString();
    }
}
