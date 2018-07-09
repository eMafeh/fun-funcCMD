package cn.qr.cryptozoic.clazz;

import cn.qr.cryptozoic.function.FunctionWorkshop;

import java.lang.reflect.Type;
import java.util.Map;

public class FindClassUtilsTest {

    public static void main(String[] args) {

        final Class<?>[] systemClasses = FindClassUtils.SINGLETON.getSystemClasses();

        String s2 = "系统类总数" + systemClasses.length;
        final long l2 = System.currentTimeMillis();
        FunctionWorkshop.addFunction(systemClasses);
        String s3 = "函数构建耗时" + (l2 - System.currentTimeMillis());
//
        Type[] type = new Type[1];
        Map[] map = new Map[1];
        int[] count = new int[1];
        FunctionWorkshop.getFUNCTIONS().forEach((a, b) -> System.out.println(b));
        FunctionWorkshop.getFUNCTIONS().forEach((a, b) -> {
            count[0] += b.size();
            System.out.println(a + "     " + b.size());
            if (map[0] == null || b.size() > map[0].size()) {
                map[0] = b;
                type[0] = a;
            }
        });
        System.err.println("1");
//        map[0].forEach((a, v) -> System.out.println(a + "  " + v));

        System.out.println(s2);
        System.out.println(s3);
        System.out.println("函数总数是" + count[0]);
        System.out.println("函数最多的类型是" + type[0] + "    " + map[0].size());
        System.out.println("不同的类型有" + FunctionWorkshop.getFUNCTIONS().size());
    }

}