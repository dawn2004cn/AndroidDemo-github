package com.noahedu.common.util;


import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
    

    /**
     * 获得相应的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"yyyy-MM-dd"的日期数据
     */
    public static String getDay(int weeks) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, weeks);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获得当天的月日
     *
     * @return 获取格式为"MM-dd"的日期数据
     */
    public static String getDayDD() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("MM-dd");
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获得当天的年月
     *
     * @return 获取格式为"yyyy年MM月"的日期数据
     */
    public static String getDateLi() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月");
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获取当前（北京时间）指定格式的日期
     *
     * @param format 格式 "yyyy-MM-dd　HH:mm" 获取年月日时分 HH:mm 获取时分 EEEE 获取星期几，如“星期四”
     * @return 指定格式的日期数据
     */
    public static String getDayss(String format) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat(format);
        sfd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获得相应的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"yyyy.MM.dd"的日期数据
     */
    public static String getDayUpdate(int weeks) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, weeks);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy.MM.dd");
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获得相应的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"MM/dd HH:mm"的日期数据
     */
    public static String getDayUpdate_new(int weeks) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, weeks);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("MM/dd HH:mm");
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获得当前月份
     *
     * @param weeks 周期偏差量
     * @return 月份日期数据
     */
    public static String getMonth(int weeks) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, weeks);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("MM");
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获得当前年份
     *
     * @param weeks 周期偏差量
     * @return 年份日期数据
     */
    public static String getYear(int weeks) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, weeks);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy");
        String preMonday = sfd.format(date);
        return preMonday;
    }

    /**
     * 获得相应星期星期一的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"yyyy年MM月dd日"的星期一日期数据
     */
    public static String getMonday(int weeks) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月dd日");
        String preMonday = sfd.format(monday);
        return preMonday;
    }

    /**
     * 获得相应星期星期一的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"yyyy-MM-dd"的星期一日期数据
     */
    public static String getMondays(int weeks) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = sfd.format(monday);
        return preMonday;
    }

    /**
     * 获得当前日期与本星期日相差的天数
     *
     * @return 相差的天数
     */
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一星期的第几天，星期日是第一天，星期二是第二天......
        // int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; //
        // 因为按中国礼拜一作为第一天所以这里减1
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK); // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获得相应星期的星期日的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"yyyy年MM月dd日"的星期日日期数据
     */
    public static String getSunday(int weeks) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月dd日");
        String preMonday = sfd.format(monday);
        return preMonday;
    }

    /**
     * 获得相应星期的星期日的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"yyyy-MM-dd"的星期日日期数据
     */
    public static String getSundays(int weeks) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = sfd.format(monday);
        return preMonday;
    }

    private static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    /**
     * 获得本年第一天的日期
     *
     * @return 获取格式为"yyyy-MM-dd"的本年第一天的日期数据
     */
    public static String getCurrentYearFirst() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        // DateFormat df = DateFormat.getDateInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String preYearDay = sdf.format(yearDay);
        return preYearDay;
    }

    /**
     * 获得本年第一天的日期
     *
     * @return 获取格式为"yyyy年MM月dd日"的本年第一天的日期数据
     */
    public static String getCurrentYearFirsts() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        // DateFormat df = DateFormat.getDateInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String preYearDay = sdf.format(yearDay);
        return preYearDay;
    }

    /**
     * 获得本年最后一天的日期
     *
     * @return 获取格式为"yyyy-MM-dd"的本年最后一天的日期数据
     */
    public static String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    /**
     * 获得本年的年份
     *
     * @return 本年的年份
     */
    public static String getCurrentYearDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years;
    }

    /**
     * 获得本年最后一天的日期
     *
     * @return 获取格式为"yyyy年MM月dd日"的本年最后一天的日期数据
     */
    public static String getCurrentYearEnds() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "年12月31日";
    }

    /**
     * 获取当月第一天
     *
     * @return 获取格式为"yyyy-MM-dd"的当月第一天的日期数据
     */
    public static String getFirstDayOfMonth() {
        String str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取相减周期偏差量的月份第一天日期
     *
     * @param date 周期偏差量
     * @return 获取格式为"yyyy-MM-dd"的相减周期偏差量月份的第一天日期数据
     */
    public static String getFirstDayOfRear(int date) {
        String str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, date);// 减一个月，变为下月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取相减周期偏差量的月份最后一天日期
     *
     * @param date 周期偏差量
     * @return 获取格式为"yyyy-MM-dd"的相减周期偏差量月份最后一天的日期数据
     */
    public static String getPreviousMonthEnd(int date) {
        String str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, date);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得上年第一天的日期
     *
     * @return 获取格式为"yyyy-MM-dd"的上年第一天的日期数据
     */
    public static String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-01-01";
    }

    /**
     * 获得上年最后一天的日期
     *
     * @return 获取格式为"yyyy-MM-dd"的上年最后一天的日期数据
     */
    public static String getPreviousYearEnd() {
        int weeks = -1;
        int maxYear = 0;
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + maxYear * weeks
                + (maxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        String[] splitOne = preYearDay.split("年");
        int leng = splitOne.length;
        for (int i = 0; i < leng; i++) {
        }
        if (leng > 1) {
            String preYearDayOne = splitOne[0].toString().trim() + "-";
            String[] splitTwo = splitOne[1].toString().trim().split("月");
            preYearDayOne += splitTwo[0].toString().trim() + "-";
            String[] splitThree = splitTwo[1].toString().trim().split("日");
            preYearDayOne += splitThree[0].toString().trim();
            Log.e("TimeUtil.getPreviousYearEnd()", "获取切割的网络时间" + preYearDayOne);
            return preYearDayOne;
        } else {
            Log.e("TimeUtil.getPreviousYearEnd()", "获取没没切割的网络时间" + preYearDay);
            return preYearDay;
        }
    }

    /**
     * 获取TimeUtil对象实例
     * @return TimeUtil对象实例
     */
//    public static TimeUtil getTimeUtil(){
//        return new TimeUtil();
//    }

    /**
     * 获取当月第一天
     *
     * @return 获取格式为"yyyy年MM月dd日"的当月第一天的日期数据
     */
    public static String getFirstDayOfMonths() {
        String str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 计算当月最后一天,返回字符串
     *
     * @return 获取格式为"yyyy-MM-dd"的当月最后一天的日期数据
     */
    public static String getDefaultDay() {
        String str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 计算当月最后一天,返回字符串
     *
     * @return 获取格式为"yyyy年MM月dd日"的当月最后一天的日期数据
     */
    public static String getDefaultDays() {
        String str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 根据索引值获取相应的星期
     *
     * @param str 0是星期日。1-6数值对应相应的星期。
     * @return 相应的星期
     */
    public static String getWeekForSqlite(int str) {
        String week = "传入数据错误";
        if (str == 6) {
            week = "星期六";
        } else if (str == 0) {
            week = "星期日";
        } else if (str == 1) {
            week = "星期一";
        } else if (str == 2) {
            week = "星期二";
        } else if (str == 3) {
            week = "星期三";
        } else if (str == 4) {
            week = "星期四";
        } else if (str == 5) {
            week = "星期五";
        }
        return week;
    }

//	public static String getDateSubDay(int day) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date now = new Date();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(now);
//		cal.add(Calendar.DATE, day);
//		return sdf.format(cal.getTime());
//	}

    /**
     * 根据日期来返回相应的索引值
     *
     * @param week 星期日是0.星期一到星期六对应相应的1-6数值。
     * @return 相应的索引值。-1是传输错误
     */
    public static int getIndexWeek(String week) {
        int index;
        if ("星期一".equals(week)) {
            index = 1;
        } else if ("星期二".equals(week)) {
            index = 2;
        } else if ("星期三".equals(week)) {
            index = 3;
        } else if ("星期四".equals(week)) {
            index = 4;
        } else if ("星期五".equals(week)) {
            index = 5;
        } else if ("星期六".equals(week)) {
            index = 6;
        } else if ("星期日".equals(week)) {
            index = 0;
        } else {
            index = -1;
        }
        return index;
    }

    /**
     * 根据毫秒值来转换日期
     *
     * @param longH  毫秒值
     * @param format 转换格式（如："yyyy-MM-dd HH:mm:ss"）
     * @return 转换好的日期
     */
    @SuppressLint("SimpleDateFormat")
    public static String longgetStringDate(long longH, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(longH);
        return sdf.format(date);
    }

    /**
     * 根据要切割的要求来切割获取毫秒值，然后再根据毫秒值来转换日期
     *
     * @param cdrsq       要切割的内容
     * @param incisionOne 按什么来切割第一个
     * @param incisionTwo 按什么来切割第二个
     * @return 转换好的日期，格式为"yyyy-MM-dd HH:mm"。
     */
    @SuppressLint("SimpleDateFormat")
    public static String getIncisionText(String cdrsq, String incisionOne,
                                         String incisionTwo) {
        // 切割出毫秒值来转换日期
        String dateEnd = cdrsq.split(incisionOne)[1].split(incisionTwo)[0];
        long longHT = Long.valueOf(dateEnd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(longHT);
        sdf.format(date);
        return sdf.format(date);
    }

    /**
     * 返回指定日期相加指定天数之后的日期
     *
     * @param days    需要在指定的日期要加的天数
     * @param strings 数组长度大于2。 strings[0]是日期指定格式'yyyy-MM-dd' ，strings[1]是需要传过来的日期。
     * @return 相加之后的日期
     */
    public static String addOneDateContent(int days, String[] strings){
        if (strings.length >= 2) {
            try {
                SimpleDateFormat sfd = new SimpleDateFormat(strings[0]);
                Date dt = sfd.parse(strings[1]);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                rightNow.add(Calendar.DAY_OF_YEAR, days);// 日期加1天
                return sfd.format(rightNow.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 返回指定日期相差的秒值
     *
     * @param format                日期格式,传入的时间解析格式。样例时间格式：‘yyyy-MM-dd HH:mm:ss’
     * @param maxTime               最大日期时间
     * @param minimumTime           最小日期时间
     * @param timeHMSTransitionEnum 获取相差时间单位值
     * @return 最大-最小的相差值。默认返回毫秒值
     * @see TimeHMSTransitionEnum
     */
    public static long dateDiscrepancy(String format, String maxTime, String minimumTime, TimeHMSTransitionEnum timeHMSTransitionEnum) {
        if (!TextUtils.isEmpty(format) && !TextUtils.isEmpty(maxTime) && !TextUtils.isEmpty(minimumTime)) {
            try {
                DateFormat df = new SimpleDateFormat(format);
                Date d1 = df.parse(maxTime);
                Date d2 = df.parse(minimumTime);
                long between = d1.getTime() - d2.getTime();
//此处注释是按天时分秒关联计算相差值。
//                long day = between / (24 * 60 * 60 * 1000);// 根据毫秒求出天数
//
//                long hour = (between / (60 * 60 * 1000) - day * 24);// 根据毫秒求出时
//
//                long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);// 根据毫秒求出分
//
//                long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60); // 根据毫秒求出秒
//
//                long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60
//                        * 1000 - min * 60 * 1000 - s * 1000);// 根据毫秒求出毫秒

                long ss = between / 1000;// 得到毫秒转秒的数值

                long min1 = (between / 1000) / 60;// 得到毫秒转分的数值

                long hour1 = ((between / 1000) / 60) / 60;// 得到毫秒转时的数值

                long day1 = (((between / 1000) / 60) / 60) / 24;// 得到毫秒转天的数值

                Log.e("TimeUtil.dateDiscrepancy()", "计算：" + day1 + "天" + hour1 + "小时" + min1 + "分" + ss + "秒"
                        + between + "毫秒");
                if (null != timeHMSTransitionEnum) {
                    switch (timeHMSTransitionEnum) {
                        //天
                        case DAY_TYPE:
                            between = day1;
                            break;
                        //时
                        case TIME_TYPE:
                            between = hour1;
                            break;
                        //分
                        case MINUTE_TYPE:
                            between = min1;
                            break;
                        //秒
                        case SECOND_TYPE:
                            between = ss;
                            break;
                        //毫秒
                        case MILLISECOND_TYPE:
                            break;
                    }
                }
                return between;
            } catch (ParseException e) {
                e.printStackTrace();
                return 0l;
            }
        } else {
            return 0l;
        }
    }

    /**
     * 根据日期获取毫秒值
     *
     * @param format format[0]日期格式‘yyyy-MM-dd HH:mm’，format[1]要转换的日期
     * @return 转换好的毫秒值
     */
    public static long dateConversionTime(String... format) {
        try {
            DateFormat df = new SimpleDateFormat(format[0]);
            Date date = df.parse(format[1]);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 日期格式互转
     *
     * @param dateFormat dateFormat[0]转换之前的日期。如：2018-01-01
     *                   dateFormat[1]转换之前的日期格式。如："yyyy-MM-dd"
     *                   dateFormat[2] 要转换之后的日期格式。如："yyyy年MM月dd日"
     * @return 转换好的日期数据
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateFormatConvert(String... dateFormat) {
        SimpleDateFormat sdfThis = new SimpleDateFormat(dateFormat[1]);
        SimpleDateFormat sdfTo = new SimpleDateFormat(dateFormat[2]);
        String dateFormatOk = "";
        try {
            dateFormatOk = sdfTo.format(sdfThis.parse(dateFormat[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormatOk;
    }

    /**
     * 获得相应的日期
     *
     * @param weeks 周期偏差量
     * @return 获取格式为"yyyy年MM月dd日"的日期数据
     */
    public String getDayss(int weeks) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, weeks);
        date = calendar.getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy年MM月dd日");
        String preMonday = sfd.format(date);
        return preMonday;
    }
    
    /**
     * format seconds to HH:mm:ss String
     *
     * @param seconds seconds
     * @return String of formatted in HH:mm:ss
     */
    public static String seconds2HH_mm_ss(long seconds) {

        long h = 0;
        long m = 0;
        long s = 0;
        long temp = seconds % 3600;

        if (seconds > 3600) {
            h = seconds / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    m = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            m = seconds / 60;
            if (seconds % 60 != 0) {
                s = seconds % 60;
            }
        }

        String dh = h < 10 ? "0" + h : h + "";
        String dm = m < 10 ? "0" + m : m + "";
        String ds = s < 10 ? "0" + s : s + "";

        return dh + ":" + dm + ":" + ds;
    }
    /**
     * 当地时间 ---> UTC时间
     * @return
    */
/*    public static String Local2UTC() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = sdf.format(new Date());
        return gmtTime;
    }*/
    /**
     * UTC时间 ---> 当地时间
     * @param utcTime   UTC时间
     * @return
     */
 /*   public static String utc2Local(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }*/


    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_2hour = 2 * 60 * 60;
    private static final int seconds_of_3hour = 3 * 60 * 60;

    private static final String YMDHMS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String search_DateFormat = "MM/dd/yyyy HH:mm:ss";
    private static final String TIME_ZERO = "00:00";
    private static final String TIME_MAX = "23:59:59";

    public static Date stringConvertDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date data = null;
        try {
            data = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static String getUSDateTimeFormat(long timeStamp) {
        SimpleDateFormat usSdf = new SimpleDateFormat("HH:mm, MMMM dd, yyyy", Locale.US);
        return usSdf.format(new Date(timeStamp));
    }

    public static String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * local ---> UTC
     *
     * @return
     */
    public static String Local2UTC() {
        SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String gmtTime = sdf.format(new Date());
        return gmtTime;
    }

    /**
     * UTC --->local
     *
     * @param utcTime UTC
     * @return
     */
    public static String utc2Local(String utcTime) {
        try {
            if (TextUtils.isEmpty(utcTime)) {
                return "";
            }
            SimpleDateFormat utcFormater = new SimpleDateFormat(YMDHMS_FORMAT);
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date gpsUTCDate = null;
            try {
                gpsUTCDate = utcFormater.parse(utcTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat localFormater = new SimpleDateFormat(YMDHMS_FORMAT);
            localFormater.setTimeZone(TimeZone.getDefault());
            String localTime = localFormater.format(gpsUTCDate.getTime());
            return localTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @return
     */
    public static String timeStamp2Date(long seconds, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    public static String longToString(long longNum, String dateFormat) {
        if (TextUtils.isEmpty(dateFormat)) {
            dateFormat = YMDHMS_FORMAT;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = new Date(longNum);
        return format.format(date);
    }

    public static String secondsToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return TIME_ZERO;
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 23)
                    return TIME_MAX;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        try {
            if (i >= 0 && i < 10)
                retStr = "0" + Integer.toString(i);
            else
                retStr = "" + i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }

    public static long searchTimeToLong(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0L;
        }
        try {
            String[] split = time.split(" ");
            String tempTime = split[0] + " " + split[1];
            int diff = 0;
            if ("pm".equals(split[2])) {
                diff = 1000 * 12 * 60 * 60;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(search_DateFormat);
            sdf.setTimeZone(TimeZone.getDefault());
            Date startTime = null;
            startTime = sdf.parse(tempTime);
            return (startTime.getTime() + diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
