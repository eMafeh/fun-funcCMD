package cn.qr.method;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author QianRui
 * 2018/12/25
 */
public class TestMethodHandles {
    public void target(String s) {

    }

    public static void target2(String s) {

    }

    static final MethodHandle target;
    static final MethodHandle target2;
    static final TestMethodHandles instance = new TestMethodHandles();

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class, String.class);
        try {
            target = lookup.findVirtual(TestMethodHandles.class, "target", methodType);
            target2 = lookup.findStatic(TestMethodHandles.class, "target2", methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Throwable {

        long l = System.currentTimeMillis();
        for (int i = 0; i < 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long now = System.currentTimeMillis();
                System.out.println(now - l);
                l = now;
            }
            target.invoke(instance, "1");
        }
        System.err.println("over");
        for (int i = 0; i < 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long now = System.currentTimeMillis();
                System.out.println(now - l);
                l = now;
            }
            target2.invoke("1");
        }
        System.err.println("over");
        for (int i = 0; i < 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long now = System.currentTimeMillis();
                System.out.println(now - l);
                l = now;
            }
            target2("1");
        }
    }
}
