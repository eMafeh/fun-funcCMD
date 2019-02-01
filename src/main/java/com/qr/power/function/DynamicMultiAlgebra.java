package com.qr.power.function;

import java.util.Map;
import java.util.TreeMap;

import static com.qr.power.function.Algebra.nTo0;
import static com.qr.power.function.Algebra.otoN;

public class DynamicMultiAlgebra {
    private final Map<Algebra, Integer> count = new TreeMap<>();

    private DynamicMultiAlgebra() {
    }

    public static DynamicMultiAlgebra ma() {
        return new DynamicMultiAlgebra();
    }

    public DynamicMultiAlgebra multiply(DynamicMultiAlgebra algebra) {
        algebra.count.forEach(this::multiply);
        return this;
    }

    public DynamicMultiAlgebra divide(DynamicMultiAlgebra algebra) {
        algebra.count.forEach(this::divide);
        return this;
    }

    public DynamicMultiAlgebra multiply(Algebra algebra) {
        return multiply(algebra, 1);
    }

    public DynamicMultiAlgebra divide(Algebra algebra) {
        return divide(algebra, 1);
    }

    private DynamicMultiAlgebra multiply(Algebra that, int value) {
        count.compute(that, (a, b) -> otoN(nTo0(b) + value));
        return this;
    }


    private DynamicMultiAlgebra divide(Algebra that, int value) {
        count.compute(that, (a, b) -> otoN(nTo0(b) - value));
        return this;
    }


}
