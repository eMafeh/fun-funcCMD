package util;

import java.lang.reflect.Array;

public class ArraysUtil {
    @SuppressWarnings("unchecked")
    private static <A> A[] newInstance(A[] a, int length) {
        return (A[]) Array.newInstance(a.getClass()
                .getComponentType(), length);
    }

    public static <A> A[] copy(A[] a) {
        A[] merge = newInstance(a, a.length);
        System.arraycopy(a, 0, merge, 0, a.length);
        return merge;
    }

    public static <A> A[] copy(A[] a, A[] b) {
        A[] merge = newInstance(a, a.length + b.length);
        System.arraycopy(a, 0, merge, 0, a.length);
        System.arraycopy(b, 0, merge, a.length, b.length);
        return merge;
    }

    @SafeVarargs
    public static <A> A[] copy(A[]... as) {
        int length = 0;
        for (final A[] a : as) length += a.length;
        A[] merge = newInstance(as[0], length);
        length = 0;
        for (A[] a : as) {
            System.arraycopy(a, 0, merge, length, a.length);
            length += a.length;
        }
        return merge;
    }


    public static <A> A[] union(A[] a, A[] b) {
        boolean aLag = a.length > b.length;
        A[] lag = aLag ? a : b;
        A[] copy = copy(aLag ? b : a);
        int count = 0;
        for (int i = 0; i < lag.length && count < copy.length; i++)
            for (int j = count; j < copy.length; j++)
                if (lag[i].equals(copy[j])) {
                    A temp = copy[count];
                    copy[count++] = copy[j];
                    copy[j] = temp;
                    break;
                }
        if (count == copy.length) return copy;
        A[] result = newInstance(copy, count);
        System.arraycopy(copy, 0, result, 0, count);
        return result;
    }

    public static <A> A[] difference(A[] target, A[] remove) {
        A[] copy = copy(target);
        int count = 0;
        for (int i = 0; i < remove.length && count < copy.length; i++)
            for (int j = count; j < copy.length; j++)
                if (remove[i].equals(copy[j])) {
                    copy[j] = copy[count];
                    copy[count++] = null;
                    break;
                }
        A[] result = newInstance(copy, copy.length - count);
        if (count != copy.length)
            System.arraycopy(copy, count, result, 0, copy.length - count);
        return result;
    }

    public static <A> A[] sort(final A[] a) {
//        if (a.length > 0) Arrays.sort(a);
        return a;
    }
}
