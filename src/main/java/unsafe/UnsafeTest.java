package unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public enum UnsafeTest {
    ;
    private final int a = 1;
    private static final int B = 1;

    UnsafeTest() {
        throw new RuntimeException();
    }

    public static void main(String[] args) throws Exception {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        UnsafeTest instance = (UnsafeTest) unsafe.allocateInstance(UnsafeTest.class);
        String name = instance.name();

        Field a = instance.getClass().getDeclaredField("a");
        long la = unsafe.objectFieldOffset(a);
        int oldVa = unsafe.getInt(instance, la);
        unsafe.putInt(instance, la, 2);
        int newVa = unsafe.getInt(instance, la);
        Object relVa = a.get(instance);

        Field b = instance.getClass().getDeclaredField("B");
        long lb = unsafe.staticFieldOffset(b);
        int oldVb = unsafe.getInt(instance, lb);
        unsafe.putInt(instance, lb, 2);
        int newVb = unsafe.getInt(instance, lb);
        Object relVb = a.get(instance);

        unsafe.putObjectVolatile(instance, unsafe.objectFieldOffset(instance.getClass().getSuperclass().getDeclaredField("name")), "TEST");
        instance.show();
        UnsafeTest.values();
        Map<String,UnsafeTest> map = new HashMap<>();
        map.put("TEST",instance);
        Field enumConstantDirectory = UnsafeTest.class.getClass().getDeclaredField("enumConstantDirectory");
        long l = unsafe.objectFieldOffset(enumConstantDirectory);
        unsafe.putObject(UnsafeTest.class,l,map);
        long aLong = unsafe.getLong(UnsafeTest.class, l);
        UnsafeTest test = UnsafeTest.valueOf("TEST");
        int ordinal = test.ordinal();
    }

    private void show() {
        System.out.println(this.name());
    }
}
