package com.example.anubis9000.saleandorid.utils;

/**
 * Created by anubis9000 on 2018/1/19.
 */

import org.apache.commons.codec.binary.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author pinke
 * @since 2015-12-29.
 */
public class DateUtils {
    private static final Map<String, SimpleDateFormat> DATE_FORMAT_MAP = new HashMap<String, SimpleDateFormat>();

    public static Date parse(String dateStr, String format) throws ParseException {
        return getFormatter(format).parse(dateStr);
    }

    private static DateFormat getFormatter(String format) {
        if (true)
            return new SimpleDateFormat(format);
        SimpleDateFormat date = DATE_FORMAT_MAP.get(format);
        if (date != null)
            return date;
        synchronized (DATE_FORMAT_MAP) {
            date = DATE_FORMAT_MAP.get(format);
            if (date == null) {
                date = new SimpleDateFormat(format);
                DATE_FORMAT_MAP.put(format, date);
            }
        }
        return date;
    }

    public static String format(Date date, String format) {
        return getFormatter(format).format(date);
    }

    public static int date2Int(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(date);
        return Integer.parseInt(dateStr);
    }

    public static String dateToString(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        long times = System.currentTimeMillis();
        Date date = new Date(times);
        return formatter.format(date);
    }

    /*
     * 日期yyyy-mm-dd转成yyyy年mm月dd日
     */
    public static String dateStringToChineseString(String dateString) {
        StringBuffer sbf = new StringBuffer();
        String[] dataSs = dateString.split( "-");
        sbf.append(dataSs[0] + "年");
        sbf.append(dataSs[1] + "月");
        sbf.append(dataSs[2] + "日");
        return sbf.toString();
    }

    public static String cstToNormal(String cst) throws Exception {
        DateFormat formateNormal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formateCst = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = formateCst.parse(cst);
        String dateString = formateNormal.format(date);
        return dateString;
    }

    public static String getFileName(String cst) throws Exception {
        DateFormat formateNormal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formateCst = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = formateCst.parse(cst);
        String dateString = formateNormal.format(date);
        return dateString;
    }

    /**
     * 判断字符串是否是日期类型
     *
     * @param str
     * @return true or false
     */
    public static boolean isValidDate(String str) {
        if (str!=null&&!"".equals(str)) {
            return false;
        }
        if (str.length() == 10) {
            str = str + " 00:00:00";
        }

        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    public static Date addMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param one
     * @param two
     * @return
     * @throws Exception
     */
    public static long getDistanceDays(Date one, Date two) {
        return getDistanceDays(one, two, true);
    }

    public static long getDistanceDays(Date one, Date two, boolean abs) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(one);
        calOne.set(Calendar.HOUR, 0);
        calOne.set(Calendar.MINUTE, 0);
        calOne.set(Calendar.SECOND, 0);
        long timeOne = calOne.getTimeInMillis();

        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(two);
        calTwo.set(Calendar.HOUR, 0);
        calTwo.set(Calendar.MINUTE, 0);
        calTwo.set(Calendar.SECOND, 0);
        long timeTwo = calTwo.getTimeInMillis();

        long diff = timeOne - timeTwo;
        if (abs)
            diff = Math.abs(diff);
        long days = diff / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * 两个时间之间相差距离多少小时
     *
     * @param one
     * @param two
     * @return
     * @throws Exception
     */
    public static long getDistanceHours(Date one, Date two) {
        return getDistanceHours(one, two, true);
    }

    public static long getDistanceHours(Date one, Date two, boolean abs) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(one);
        calOne.set(Calendar.HOUR, 0);
        calOne.set(Calendar.MINUTE, 0);
        calOne.set(Calendar.SECOND, 0);
        long timeOne = calOne.getTimeInMillis();

        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(two);
        calTwo.set(Calendar.HOUR, 0);
        calTwo.set(Calendar.MINUTE, 0);
        calTwo.set(Calendar.SECOND, 0);
        long timeTwo = calTwo.getTimeInMillis();

        long diff = timeOne - timeTwo;
        if (abs)
            diff = Math.abs(diff);
        long hours = diff / (1000 * 60 * 60);
        return hours;
    }

    /**
     * 两个时间之间相差距离多少秒
     *
     * @param one
     * @param two
     * @return
     * @throws Exception
     */
    public static long getDistanceSecond(Date one, Date two) {
        long seconds = 0;
        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        seconds = diff / 1000;
        return seconds;
    }

    /**
     * 将uct时间转换为本地时间
     *
     * @param utcTime
     * @param utcTimePatten
     * @param localTimePatten
     * @return
     */
    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));// 时区定义并进行时间获取
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    /**
     * 在给定时间区间内随机生成一个时间
     *
     * @param from 开始时间
     * @param to 结束时间
     * @return 新的时间
     */
    public static Date randomDateBetween(Date from, Date to) {
        final long fromTime = from.getTime();
        final long toTime = to.getTime();
        long v = Math.abs(toTime - fromTime);
        v = (long) (Math.random() * v);
        return new Date(Math.min(toTime, fromTime) + v);
    }

    public static void main(String[] args) {
    /*
     * String time = utc2Local("2017-06-06 02:02:56","yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
     * System.out.println(time);
     */

        // System.out.println(dateStringToChineseString("2017-06-06"));

        System.out.println(format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        for (int i = 0; i < 10; i++) {
            System.out.println(format(randomDateBetween(new Date(new Date().getTime() + 60 * 60 * 1000),
                    new Date(new Date().getTime() + 24 * 60 * 60 * 1000)), "yyyy-MM-dd HH:mm:ss"));
        }
    }

}
