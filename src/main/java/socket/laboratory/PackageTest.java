package socket.laboratory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author kelaite
 * 2018/2/9
 */
public class PackageTest {
    public static void main(String[] args) {
        Map map = new ConcurrentSkipListMap();
        map.put("1","1t");
        map.put("2","2t");
        map.put("3","3t");
        map.put("4","4t");
        for (Object o : map.keySet()) {
            map.put(o,map.get(o)+"i");
            map.put("5","i"+map.get("5"));
        }
        System.out.println(map);
    }
}
