package com.study.utils;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil extends PropertyEditorSupport {
    // 各种时间格式
    public static final SimpleDateFormat uk_format = new SimpleDateFormat("MMMdd,yyyy", Locale.UK);
    public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
    // 各种时间格式
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat yyyyMMdd2 = new SimpleDateFormat("yyyy/MM/dd");
    // 各种时间格式
    public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat datetimeCompactFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
    // 以毫秒表示的时间
    private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
    private static final long HOUR_IN_MILLIS = 3600 * 1000;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;
    private static final long SECOND_IN_MILLIS = 1000;

    // 指定模式的时间格式
    private static SimpleDateFormat getSDFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 当前日历，这里用中国时间表示
     *
     * @return 以当地时区表示的系统当前日历
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Date getDayFromDateStr(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }
        dateStr = dateStr.split("on")[1].trim();
        Date commentDate = DateUtil.str2Date(dateStr,DateUtil.uk_format);
        return commentDate;
    }

    /**
     * 指定毫秒数表示的日历
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日历
     */
    public static Calendar getCalendar(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millis));
        return cal;
    }

    /**
     * 获取指定年月的第一天
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return cal.getTime();
    }


    /**
     * 获取指定年月的最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 时间转字符串
     */
    public static String date2SStr() {
        Date date = getDate();
        if (null == date) {
            return null;
        }
        return yyyyMMdd.format(date);
    }

    /**
     * 当前日期
     *
     * @return 系统当前时间
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 指定毫秒数表示的日期
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日期
     */
    public static Date getDate(long millis) {
        return new Date(millis);
    }

    /**
     * 时间戳转换为字符串
     */
    public static String timestamptoStr(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(datetimeFormat);
    }

    /**
     * 字符串转换时间戳
     */
    public static Timestamp str2Timestamp(String str) {
        Date date = str2Date(str, date_sdf);
        return new Timestamp(date.getTime());
    }

    /**
     * 字符串转换成日期
     */
    public static Date str2Date(String str, SimpleDateFormat sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换为字符串
     *
     * @param date_sdf 日期格式
     * @return 字符串
     */
    public static String date2Str(SimpleDateFormat date_sdf) {
        Date date = getDate();
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    /**
     * 格式化时间
     */
    public static String dataformat(String data, String format) {
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sformat.parse(data);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sformat.format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param date     日期
     * @param date_sdf 日期格式
     * @return 字符串
     */
    public static String date2Str(Date date, SimpleDateFormat date_sdf) {
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param format 日期格式
     * @return 字符串
     */
    public static String getDate(String format) {
        Date date = new Date();
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 指定毫秒数的时间戳
     *
     * @param millis 毫秒数
     * @return 指定毫秒数的时间戳
     */
    public static Timestamp getTimestamp(long millis) {
        return new Timestamp(millis);
    }

    /**
     * 以字符形式表示的时间戳
     *
     * @param time 毫秒数
     * @return 以字符形式表示的时间戳
     */
    public static Timestamp getTimestamp(String time) {
        return new Timestamp(Long.parseLong(time));
    }

    /**
     * 系统当前的时间戳
     *
     * @return 系统当前的时间戳
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 指定日期的时间戳
     *
     * @param date 指定日期
     * @return 指定日期的时间戳
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 指定日历的时间戳
     *
     * @param cal 指定日历
     * @return 指定日历的时间戳
     */
    public static Timestamp getCalendarTimestamp(Calendar cal) {
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp gettimestamp() {
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(dt);
        return Timestamp.valueOf(nowTime);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // getMillis
    // 各种方式获取的Millis
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 系统时间的毫秒数
     *
     * @return 系统时间的毫秒数
     */
    public static long getMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 指定日历的毫秒数
     *
     * @param cal 指定日历
     * @return 指定日历的毫秒数
     */
    public static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }

    /**
     * 指定日期的毫秒数
     *
     * @param date 指定日期
     * @return 指定日期的毫秒数
     */
    public static long getMillis(Date date) {
        return date.getTime();
    }

    /**
     * 指定时间戳的毫秒数
     *
     * @param ts 指定时间戳
     * @return 指定时间戳的毫秒数
     */
    public static long getMillis(Timestamp ts) {
        return ts.getTime();
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatDate
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期，具体格式：年-月-日
     *
     * @return 默认日期按“年-月-日“格式显示
     */
    public static String formatDate() {
        return date_sdf_wz.format(getCalendar().getTime());
    }

    /**
     * 获取时间字符串
     */
    public static String getDataString(SimpleDateFormat formatstr) {
        return formatstr.format(getCalendar().getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Calendar cal) {
        return date_sdf_wz.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Date date) {
        return date_sdf_wz.format(date);
    }

    /**
     * 指定毫秒数表示日期的默认显示，具体格式：年-月-日
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日“格式显示
     */
    public static String formatDate(long millis) {
        return date_sdf_wz.format(new Date(millis));
    }

    /**
     * 默认日期按指定格式显示
     *
     * @param pattern 指定的格式
     * @return 默认日期按指定格式显示
     */
    public static String formatDate(String pattern) {
        return getSDFormat(pattern).format(getCalendar().getTime());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param cal     指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Calendar cal, String pattern) {
        return getSDFormat(pattern).format(cal.getTime());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param date    指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Date date, String pattern) {
        return getSDFormat(pattern).format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期，具体格式：年-月-日 时：分
     *
     * @return 默认日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime() {
        return time_sdf.format(getCalendar().getTime());
    }

    /**
     * 指定毫秒数表示日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(long millis) {
        return time_sdf.format(new Date(millis));
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Calendar cal) {
        return time_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Date date) {
        return time_sdf.format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatShortTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期，具体格式：时：分
     *
     * @return 默认日期按“时：分“格式显示
     */
    public static String formatShortTime() {
        return short_time_sdf.format(getCalendar().getTime());
    }

    /**
     * 指定毫秒数表示日期的默认显示，具体格式：时：分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“时：分“格式显示
     */
    public static String formatShortTime(long millis) {
        return short_time_sdf.format(new Date(millis));
    }

    /**
     * 指定日期的默认显示，具体格式：时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“时：分“格式显示
     */
    public static String formatShortTime(Calendar cal) {
        return short_time_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“时：分“格式显示
     */
    public static String formatShortTime(Date date) {
        return short_time_sdf.format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // parseDate
    // parseCalendar
    // parseTimestamp
    // 将字符串按照一定的格式转化为日期或时间
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     */
    public static Date parseDate(String src, String pattern) throws ParseException {
        return getSDFormat(pattern).parse(src);

    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), "yyyy-MM-dd HH:mm:ss");
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     */
    public static Calendar parseCalendar(String src, String pattern) throws ParseException {

        Date date = parseDate(src, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatAddDate(String src, String pattern, int amount) throws ParseException {
        Calendar cal;
        cal = parseCalendar(src, pattern);
        cal.add(Calendar.DATE, amount);
        return formatDate(cal);
    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的时间戳
     */
    public static Timestamp parseTimestamp(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        return new Timestamp(date.getTime());
    }

    // ////////////////////////////////////////////////////////////////////////////
    // dateDiff
    // 计算两个日期之间的差值
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 计算两个时间之间的差值，根据标志的不同而不同
     *
     * @param flag   计算标志，表示按照年/月/日/时/分/秒等计算
     * @param calSrc 减数
     * @param calDes 被减数
     * @return 两个日期之间的差值
     */
    public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {

        long millisDiff = getMillis(calSrc) - getMillis(calDes);

        if (flag == 'y') {
            return (calSrc.get(calSrc.YEAR) - calDes.get(calDes.YEAR));
        }

        if (flag == 'd') {
            return (int) (millisDiff / DAY_IN_MILLIS);
        }

        if (flag == 'h') {
            return (int) (millisDiff / HOUR_IN_MILLIS);
        }

        if (flag == 'm') {
            return (int) (millisDiff / MINUTE_IN_MILLIS);
        }

        if (flag == 's') {
            return (int) (millisDiff / SECOND_IN_MILLIS);
        }

        return 0;
    }

    /**
     * 时间之间的秒数
     *
     * @param smallDate 小时间
     * @param bigDate   大时间
     */
    public static long dateDiff(Date smallDate, Date bigDate) {
        long time = 0;
        try {
            time = (bigDate.getTime() - smallDate.getTime()) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            time = 0;
        }
        return time;
    }


    /**
     * 获取日周月时间段
     *
     * @param type 1日2周3月
     */
    public static String[] getTimeSlot(int type) {
        return getTimeSlot(new Date(), type);
    }

    /**
     * 获取日周月时间段
     *
     * @param date 时间基点
     * @param type 1日2周3月
     */
    public static String[] getTimeSlot(Date date, int type) {
        Date start = date, end = date;
        switch (type) {
            case 1:
                start = date;
                break;
            case 2:
                start = add(date, -6);
                break;
            case 3:
                start = add(date, -30);
                break;
            default:
                break;
        }
        String time1 = date_sdf.format(start).toString() + " 00:00:00";
        String time2 = date_sdf.format(end).toString() + " 23:59:59";
        String[] times = new String[]{time1, time2};
        return times;
    }


    /**
     * 获取未来日周月时间段
     *
     * @param type 1日2周3月
     */
    public static String[] getTimeSlotFuture(int type) {
        return getTimeSlotFuture(new Date(), type);
    }

    /**
     * 获取未来日周月时间段
     *
     * @param date 时间基点
     * @param type 1日2周3月
     */
    public static String[] getTimeSlotFuture(Date date, int type) {
        Date start = date, end = date;
        switch (type) {
            case 1:
                end = date;
                break;
            case 2:
                end = add(date, 6);
                break;
            case 3:
                end = add(date, 30);
                break;
            default:
                break;
        }
        String time1 = date_sdf.format(start).toString() + " 00:00:00";
        String time2 = date_sdf.format(end).toString() + " 23:59:59";
        String[] times = new String[]{time1, time2};
        return times;
    }

    public static Date add(Date d1, int add) {
        if (d1 == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        c.add(Calendar.DAY_OF_MONTH, add);// +add天
        Date d2 = c.getTime();
        return d2;
    }



    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(Calendar.YEAR);
    }

    public static void main(String[] args) {
        String s1 = "20200326020202";
        String s2 = "20200326020200";
        try {
            Date d1 = yyyymmddhhmmss.parse(s1);
            Date d2 = yyyymmddhhmmss.parse(s2);
            System.out.println(dateDiff(d1, d2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static ZoneId defaultZone = ZoneId.systemDefault();

    /**
     * 日期转字符串，格式：yyyy-MM-dd HH:mm:ss（jdk1.8）
     */
    public static String DateToString(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(defaultZone).toLocalDateTime();
        String dateStr = localDateTime.format(format);
        return dateStr;
    }

    /**
     * 时间戳按照30秒间隔转换
     */
    public static Long timeMillisTrans30S(Long currentTimeMillis) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date(currentTimeMillis);
        String currStr = datetimeFormat.format(currentDate);
        Integer ss = Integer.valueOf(currStr.substring(currStr.length() - 2));
        if (ss >= 30) {
            currStr = currStr.substring(0, currStr.length() - 2) + "30";
        } else {
            currStr = currStr.substring(0, currStr.length() - 2) + "00";
        }
        try {
            currentTimeMillis = datetimeFormat.parse(currStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTimeMillis;
    }


}
