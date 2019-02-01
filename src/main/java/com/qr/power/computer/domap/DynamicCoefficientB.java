/*
package com.qr.power.computer.domap;

import com.qr.power.function.DynamicMultiAlgebra;
import com.qr.power.function.MultiAlgebra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qr.power.function.Algebra.nTo0;


public class DynamicCoefficientB {
    private DynamicCoefficientB() {
        this.count = new HashMap<>();
    }

    private final Map<DynamicMultiAlgebra, Integer> count;

    public CoefficientB beFinal() {
        List<DynamicMultiAlgebra> remove = new ArrayList<>();
        count.forEach((key, value) -> {
            if (value == 0) remove.add(key);
        });
        for (DynamicMultiAlgebra algebra : remove)
            count.remove(algebra);
        return null;
    }

    public static DynamicCoefficientB c() {
        return new DynamicCoefficientB();
    }

    private DynamicCoefficientB instance() {
        return this;
    }

    public DynamicCoefficientB add(CoefficientB dynamicCoefficientB) {
        DynamicCoefficientB instance = instance();
        dynamicCoefficientB.count.forEach(instance::add);
        return instance;
    }

    public DynamicCoefficientB subtract(DynamicCoefficientB dynamicCoefficientB) {
        DynamicCoefficientB instance = instance();
        dynamicCoefficientB.count.forEach(instance::subtract);
        return instance;
    }

    public DynamicCoefficientB subtract(CoefficientB dynamicCoefficientB) {
        DynamicCoefficientB instance = instance();
        dynamicCoefficientB.count.forEach(instance::subtract);
        return instance;
    }

    public DynamicCoefficientB add(DynamicMultiAlgebra coefficient) {
        return instance().add(coefficient, 1);
    }

    public DynamicCoefficientB subtract(DynamicMultiAlgebra coefficient) {
        return instance().subtract(coefficient, 1);
    }

    public DynamicCoefficientB multiply(DynamicMultiAlgebra coefficient) {
        DynamicCoefficientB newInstance = c();
        for (Map.Entry<DynamicMultiAlgebra, Integer> entry : count.entrySet()) {
            DynamicMultiAlgebra key = entry.getKey();
            Integer v = entry.getValue();
            newInstance.count.put(key.multiply(coefficient), v);
        }
        count.clear();
        count.putAll(newInstance.count);
        return this;
    }

    private DynamicCoefficientB add(MultiAlgebra that, int value) {
        count.compute(that, (a, b) -> nTo0(b) + value);
        return this;
    }

    private DynamicCoefficientB add(DynamicMultiAlgebra that, int value) {
        count.compute(that, (a, b) -> nTo0(b) + value);
        return this;
    }

    private DynamicCoefficientB subtract(MultiAlgebra that, int value) {
        count.compute(that, (a, b) -> nTo0(b) - value);
        return this;
    }

    private DynamicCoefficientB subtract(DynamicMultiAlgebra that, int value) {
        count.compute(that, (a, b) -> nTo0(b) - value);
        return this;
    }
}
*/
