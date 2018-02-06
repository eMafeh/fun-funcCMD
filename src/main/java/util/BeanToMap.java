package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.ASMJavaBeanSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2017/9/14  10:55
 *
 * @author QianRui
 */
public class BeanToMap {

    public static <E> List<Map<String, Object>> getMapList(List<E> list) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        if (list == null) {
            return result;
        }
        for (E e : list) {
            result.add(getMap(e));
        }
        return result;
    }

    public static <E> Map<String, Object> getMap(E e) {
        if (e == null) {
            return new HashMap<>(5);
        }
        try {
            if (e instanceof String) {
                return JSONObject.parseObject((String) e);
            }
            return JSONObject.parseObject(JSONObject.toJSONString(e));
        } catch (Exception e1) {
            return new HashMap<>(5);
        }
    }

    public static <E> List<Map<String, Object>> getMap(List<E> list) {
        return getMapList(list);
    }

    /**
     * fastJson的key名称固定首字母小写，传入得对象如果对大小写有需求，请添加jsonfield注解到getset方法上
     */
    public static <E> E getBean(Object obj, Class<E> e) {
        try {
            if (obj instanceof String) {
                return JSON.parseObject((String) obj, e);
            }
            return JSON.parseObject(JSON.toJSONString(obj), e);
        } catch (Exception e1) {
            return null;
        }
    }

    private static final SerializeConfig SERIALIZE_CONFIG = SerializeConfig.getGlobalInstance();
    private static final ParserConfig PARSER_CONFIG = ParserConfig.getGlobalInstance();

    private static JavaBeanSerializer getJavaBeanSerializer(final Class<?> currentClass) {
        JavaBeanSerializer beanSerializer = null;
        {
            ObjectSerializer serializer = SERIALIZE_CONFIG.getObjectWriter(currentClass);
            if (serializer instanceof JavaBeanSerializer) {
                beanSerializer = (JavaBeanSerializer) serializer;
            } else if (serializer instanceof ASMJavaBeanSerializer) {
                beanSerializer = ((ASMJavaBeanSerializer) serializer).getJavaBeanSerializer();
            }
        }
        return beanSerializer;
    }

    /**
     * 比正常get方法的速度慢，一亿次需要1400毫秒，正常一亿次为3-4毫秒(其中两毫秒是循环时间。。)
     */
    public static <T> T getFieldValue(Object object, String field) throws Exception {
        if (object == null || field == null || field.length() == 0) {
            return null;
        }
        JavaBeanSerializer serializer = getJavaBeanSerializer(object.getClass());
        @SuppressWarnings({"unchecked"}) T value = (T) serializer.getFieldValue(object, field);
        return value;
    }

    /**
     * 比正常set方法的速度慢，一亿次需要1800毫秒
     */
    public static void setValue(Object bean, String field, Object value) throws Exception {
        if (bean == null || field == null || field.length() == 0) {
            return;
        }
        PARSER_CONFIG.getFieldDeserializers(bean.getClass()).get(field).setValue(bean, value);
    }


//反射对比
//    public static Object getF(Object object, String name)throws Exception{
// //       反射的主要性能亏损在第一步，一亿次约为7600毫秒
//        Field declaredField = object.getClass().getDeclaredField(name);
    // //       第二步也性能损耗较小，一亿次约为30毫秒
//        declaredField.setAccessible(true);
    // //       第三步也性能损耗稍大，一亿次约为150毫秒
//        return declaredField.get(object);
    // //       采用反射get方法的方式，反射需要12000毫秒，第二步相同，但第三步需要100毫秒，比直接操作属性快
    // // 性能角度来说，建议采用（不用）如上fastJson的方法，避免隐性bug，仅需1400。。。但仍然可能有很多异常
    // // 总的来说，还是自己get set最安全，最快。
//    }
}
