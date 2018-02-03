import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by snb on 2017/9/30  15:35
 */
public class Scalss extends Fcalss {
    static String[] s = {"�ֳ���                                  ", "������                                  ", "��־��                                  ", "���                                    ", "default                                 "};
    static String[] c = {"GBK", "UTF-8", "GB2312", "ISO-8859-1"};

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println( count());
//        System.out.println("123,321~321".split("~").length);
        String s = "￥ﾼﾠ￥ﾜﾣ￤ﾺﾑ";
//        s = new String(s.getBytes("UTF-8"),"iso-8859-1");
        reEncodingMessage(s);
        //        shw();
//        String read = read("E:\\9m\\edss\\snb-des-web\\src\\main\\java\\com\\snb\\des\\controller\\extinf\\ExternalServeManager.java", "UTF-8");
//        System.out.println(new String(read.getBytes(c[0]),c[1]));
//        System.out.println(getEncoding(
//                "0你好0000495RP11115200012288882016062715092303181600SUCCESS                       1zzc                 default                                 134019399652QYMC-1                                                                                                                          030263123498989898706789012345678932010000013967452001-01-020000163000002002-07-072017-07-1800012345678900000196000000000196    0000000012000000000012002014-12-2400000052254000000000300000000010000039200000000000000000200000"
//        ));
//        System.out.println(isNumberFormat("  123123.12  "));
//        Object l = Long.parseLong("12111111111111111111111111111111111111111312311111");
//        System.out.println(l);

    }

    public static boolean isNumberFormat(String value) {
        String reg = "^\\s*\\d+(\\.\\d+)?\\s*$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public void show() {
        super.show();
    }

    static String read(String path, String encode) {
        byte[] bytes = new byte[2 << 20];
        try (FileInputStream inputStream = new FileInputStream(new File(path))) {
            int read = inputStream.read(bytes);

            String s = new String(bytes, 0, read, encode);
            System.out.println(s);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void shw() {
        String str = "-26,-98,-105,-26,-100,-99,-26,-106,-121";
        String[] split = str.split(",");
        List<Byte> collect = Arrays.stream(split).map(a -> (byte) Integer.parseInt(a)).collect(Collectors.toList());
        byte[] bytes1 = new byte[collect.size()];
        for (int i = 0; i < collect.size(); i++) {
            bytes1[i] = collect.get(i);

        }
        try {
            System.out.println(new String(bytes1, "GB2312"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static void reEncodingMessage(String s) {
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c.length; j++) {
                String s1 = null;
                try {
                    s1 = new String(s.getBytes(c[i]), c[j]);
                } catch (UnsupportedEncodingException e) {
                    //never
                }
                System.out.println(s1 + "       "+c[i] +":"+ c[j]);
            }
        }
    }

    static long count(){
        String s =
                "1294\n" +
                        "858\n" +
                        "858\n" +
                        "1294\n" +
                        "1108\n" +
                        "827\n" +
                        "1108\n" +
                        "796\n" +
                        "796\n" +
                        "1310\n" +
                        "796\n" +
                        "780\n" +
                        "764\n" +
                        "1778\n" +
                        "1762\n" +
                        "1794\n" +
                        "1888\n" +
                        "1809\n" +
                        "1840\n" +
                        "1747\n" +
                        "1840\n" +
                        "1731\n" +
                        "1716\n" +
                        "1684\n" +
                        "1653\n" +
                        "1653\n" +
                        "1638\n" +
                        "1638\n" +
                        "920\n" +
                        "1856\n" +
                        "1606\n" +
                        "842\n" +
                        "1872\n" +
                        "873\n" +
                        "1887\n" +
                        "889\n" +
                        "1887\n" +
                        "1918\n" +
                        "998\n" +
                        "1591\n" +
                        "1232\n" +
                        "1654\n" +
                        "1154\n" +
                        "1170\n" +
                        "1170\n" +
                        "1060\n" +
                        "1934\n" +
                        "1060\n" +
                        "1950\n" +
                        "1950\n" +
                        "1950\n" +
                        "1139\n" +
                        "1981\n" +
                        "1497\n" +
                        "1981\n" +
                        "1981\n" +
                        "1981\n" +
                        "1466\n" +
                        "1996\n" +
                        "2012\n" +
                        "1372\n" +
                        "1435\n" +
                        "1404\n" +
                        "2028\n" +
                        "2028\n" +
                        "1029\n" +
                        "2043\n" +
                        "2059\n" +
                        "1404\n" +
                        "2074\n" +
                        "1372\n" +
                        "2074\n" +
                        "2090\n" +
                        "2106\n" +
                        "2106\n" +
                        "2121\n" +
                        "2137\n" +
                        "2137\n" +
                        "2152\n" +
                        "2152\n" +
                        "2152\n" +
                        "2168\n" +
                        "2184\n" +
                        "2262\n" +
                        "484\n" +
                        "562\n" +
                        "670\n" +
                        "640\n" +
                        "702\n" +
                        "749\n" +
                        "2184\n" +
                        "2184\n" +
                        "2199\n" +
                        "2309\n" +
                        "2215\n" +
                        "2309\n" +
                        "2324\n" +
                        "2340\n" +
                        "2340\n" +
                        "2356"
                ;
        String[] split = s.split("\\n");
        int result=0;
        for (String s1 : split) {
            result+=Integer.parseInt(s1);
        }
        return result;
    }
}
