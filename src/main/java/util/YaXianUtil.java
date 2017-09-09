package util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class YaXianUtil {

	private static String nextline = "\n";

	static void setNextline(boolean b) {
		nextline = b ? nextline : "";
	}

	public static String getStreamMessage(InputStream inputStream, String Encoding) {
		byte[] b = new byte[1 << 20];
		StringBuilder str = new StringBuilder();
		int read;
		try {
			while ((read = inputStream.read(b)) > -1)
				str.append(new String(b, 0, read, Encoding));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}


	private static <E> StringBuilder buildXML(E e) {
		StringBuilder xml = new StringBuilder();
		if (e == null)
			throw new IllegalArgumentException("json��Ϣ��ʧ,��������null");
		Field[] fields = e.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			Object value = null;
			try {
				value = field.get(e);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
			}
			if(value==null)throw new IllegalArgumentException("json��Ϣ��ʧ,�ֶ�"+field.getName()+"�ֶ�����" + field.getType().getName());
			xml.append("<").append(name).append(">").append(value).append("</").append(name).append(">")
					.append(nextline);
		}
		return xml;
	}

	private static <E> E beanSetX(Class<E> e, String def) throws Exception {
		E o = e.newInstance();
		Field[] fields = e.getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isFinal(field.getModifiers())) {
				field.setAccessible(true);
				if (field.getType().equals(String.class))
					field.set(o, def);
				else
					field.set(o, beanSetX(field.getType(), def));

			}
		}
		return o;
	}
}
