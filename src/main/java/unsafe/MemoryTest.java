package unsafe;

import sun.misc.Unsafe;
import test.AgentMain;
import test.VMtest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;

public class MemoryTest {
    static Unsafe unsafe;

    static {
        VMtest.init();
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        show("123");
        show("1243");
        show(new HashMap<>());
        HashMap<Object, Object> obj = new HashMap<>();
        synchronized (obj){
        obj.put(1,2);
        obj.put(2,2);
        show(obj);}
        show(new String("4321"));

    }

    public static void show(Object obj) {
        long l = AgentMain.sizeOf(obj);
        for (int i = 0; i < l; i++) {
            byte aLong = unsafe.getByte(obj, (long) i);
            System.out.print(fix(aLong));
        }
        System.out.println();
    }

    public static String fix(byte b) {
//        String s = (char)b+"";
        String s = new BigDecimal(b).toString();
        while (s.length() < 4) {
            s = " " + s;
        }
        return s;
    }
}
