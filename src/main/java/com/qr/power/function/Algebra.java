package com.qr.power.function;

import java.util.Objects;

public interface Algebra extends Comparable<Algebra> {
    int power = 1000;

    int id();

    RationalNumber rationalValue();

    @Override
    default int compareTo(Algebra o) {
        Objects.requireNonNull(o);
        if (o == this) return 0;
        Class<? extends Algebra> a = o.getClass();
        Class<? extends Algebra> b = this.getClass();
        return a == b ? this.id() - o.id() : a.hashCode() - b.hashCode();
    }

    static Integer otoN(int i) {
        return i == 0 ? null : i;
    }

    static int nTo0(Integer i) {
        return i == null ? 0 : i;
    }
}
