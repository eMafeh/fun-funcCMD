package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
    private static final Map<String, ThreadLocal<SimpleDateFormat>> formatFactory = new HashMap<>();

    public static boolean isDateFormat(String date, String format) {
        SimpleDateFormat dateFormat = getSimpledateformat(format).get();
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getFormatNow(String format) {
        return getFormatTime(format, System.currentTimeMillis());
    }

    public static String getFormatTime(String format, long date) {
        return getSimpledateformat(format).get().format(date);
    }

    public static ThreadLocal<SimpleDateFormat> getSimpledateformat(String format) {
        //1.8的写法,不支持
//        return formatFactory.computeIfAbsent(format, a -> ThreadLocal.withInitial(() -> new SimpleDateFormat(a)));

        //非1.8
        ThreadLocal<SimpleDateFormat> localFormat = formatFactory.get(format);
        if (localFormat != null) return localFormat;
        synchronized (formatFactory) {
            localFormat = formatFactory.get(format);
            if (localFormat != null) return localFormat;
            localFormat = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(format);
                }
            };
            formatFactory.put(format, localFormat);
        }
        return localFormat;
    }

    /**
     * 计算两天之间相差的天数
     *
     * @param smdate
     * @param bdate
     * @return
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = getSimpledateformat("yyyyMMdd").get();
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        long time1 = smdate.getTime();
        long time2 = bdate.getTime();
        long l = (time2 - time1) / (1000 * 3600 * 24);
        return (int) l;
    }


}
