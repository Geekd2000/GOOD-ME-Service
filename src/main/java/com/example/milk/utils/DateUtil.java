package com.example.milk.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : JiangJunYe
 * @version : 1.0
 * @date : 2022/9/14 21:51
 */
public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String FORMAT_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_T_HHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_T_HHMMSSZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String FORMAT_YYYY_MM_DD_T_HHMMSS_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public DateUtil() {
    }

    public static Integer daysBetween(Date smdate, Date bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long betweenDays = (time2 - time1) / 86400000L;
            return Integer.parseInt(String.valueOf(betweenDays));
        } catch (ParseException var10) {
            return null;
        }
    }

    public static Long hoursBetween(Date smdate, Date bdate) {
        long time1 = smdate.getTime();
        long time2 = bdate.getTime();
        long betweenSeconds = (time2 - time1) / 1000L / 3600L;
        return betweenSeconds;
    }

    public static Date parse(String strDate) {
        return parse(strDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String strDate, String pattern) {
        return parse(strDate, pattern, new ParsePosition(0));
    }

    public static Date parse(String strDate, String pattern, ParsePosition position) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            Date date = formatter.parse(strDate, position);
            return date;
        }
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Long timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            Date date = new Date(timestamp);
            return format(date, "yyyy-MM-dd HH:mm:ss");
        }
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(date);
        }
    }

    public static String dateTransformBetweenTimeZone(String sourceDate) {
        try {
            return dateTransformBetweenTimeZone(parse(sourceDate, "yyyy-MM-dd'T'HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception var2) {
            log.warn("日期转换错误.sourceDate:{}, format:{}", sourceDate, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            return null;
        }
    }

    public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter) {
        return dateTransformBetweenTimeZone(sourceDate, formatter, TimeZone.getTimeZone("GMT"), TimeZone.getDefault());
    }

    public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter, TimeZone sourceTimeZone, TimeZone targetTimeZone) {
        Long targetTime = sourceDate.getTime() - (long) sourceTimeZone.getRawOffset() + (long) targetTimeZone.getRawOffset();
        return getTime(new Date(targetTime), formatter);
    }

    public static String getTime(Date date, DateFormat formatter) {
        return formatter.format(date);
    }

    public static String getStartTime(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return sdf.format(calendar.getTime());
    }

    public static String getEndTime(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 999);
        return sdf.format(calendar.getTime());
    }

    public static Date getEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 999);
        return calendar.getTime();
    }

    public static Long getTimeStamp(String strTime, String pattern) {
        if (StringUtils.isBlank(strTime)) {
            return null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);

            Date date;
            try {
                date = formatter.parse(strTime);
            } catch (Exception var5) {
                log.warn("getTimeStamp 日期转换错误.strTime:{}, pattern:{}", strTime, pattern);
                throw new RuntimeException("日期转换错误!");
            }

            return date.getTime();
        }
    }

    public static String convertDate2String(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date convertString2Date(String format, String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
            Date date = simpleDateFormat.parse(dateStr);
            return date;
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static List<TimePeriod> getIntervalTimeList(String start, String end, int interval, int timeUnit) {
        Date startDate = convertString2Date("yyyy-MM-dd HH:mm:ss", start);
        Date endDate = convertString2Date("yyyy-MM-dd HH:mm:ss", end);

        ArrayList list;
        Calendar calendar;
        for (list = new ArrayList(); startDate.getTime() <= endDate.getTime(); startDate = calendar.getTime()) {
            TimePeriod timePeriod = new TimePeriod();
            timePeriod.setStartTime(convertDate2String("yyyy-MM-dd HH:mm:ss", startDate));
            calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(timeUnit, interval);
            if (calendar.getTime().getTime() >= endDate.getTime()) {
                timePeriod.setEndTime(convertDate2String("yyyy-MM-dd HH:mm:ss", endDate));
            } else {
                timePeriod.setEndTime(convertDate2String("yyyy-MM-dd HH:mm:ss", calendar.getTime()));
            }

            list.add(timePeriod);
        }

        return list;
    }

    public static TimePeriod getBelongTimePerod(String dateTime, List<TimePeriod> timePeriodList) {
        Iterator var2 = timePeriodList.iterator();

        TimePeriod timePeriod;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            timePeriod = (TimePeriod) var2.next();
        } while (dateTime.compareTo(timePeriod.getStartTime()) < 0 || dateTime.compareTo(timePeriod.getEndTime()) > 0);

        return timePeriod;
    }

    public static TimePeriod getBelongTimePerod(String dateTime, String start, String end, int interval, int timeUnit) {
        List<TimePeriod> intervalTimeList = getIntervalTimeList(start, end, interval, timeUnit);
        return CollectionUtils.isEmpty(intervalTimeList) ? null : getBelongTimePerod(dateTime, intervalTimeList);
    }

    public static void main(String[] args) {
        List<TimePeriod> intervalTimeList = getIntervalTimeList("2022-03-19 11:53:16", "2022-03-19 16:54:16", 5, 12);
        TimePeriod belongTimePerod = getBelongTimePerod("2022-03-19 12:03:17", intervalTimeList);
        System.out.println(belongTimePerod);
        Date date1 = new Date(1646064000000L);
        Date date2 = new Date(1646064000001L);
        System.out.println(isSameMonth(date1, date2));
        Date date3 = new Date(1646063999999L);
        Date date4 = new Date(1646064000000L);
        System.out.println(isSameMonth(date3, date4));
        System.out.println(biggerThanByMonth(date2, date1, 1));
        Date date5 = new Date(1646063999999L);
        Date date6 = new Date(1646064000000L);
        System.out.println(biggerThanByMonth(date6, date5, 1));
    }

    public static Boolean sameCycle(Date d1, Date d2, int type) {
        CycleEnum cycle = DateUtil.CycleEnum.getCycle(type);
        if (cycle.equals(DateUtil.CycleEnum.YEAR)) {
            return isSameYear(d1, d2);
        } else if (cycle.equals(DateUtil.CycleEnum.MONTH)) {
            return isSameMonth(d1, d2);
        } else if (cycle.equals(DateUtil.CycleEnum.WEEK)) {
            return isSameWeek(d1, d2);
        } else {
            return cycle.equals(DateUtil.CycleEnum.DAY) ? isSameDate(d1, d2) : false;
        }
    }

    public static Boolean isSameYear(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        boolean isSameYear = cal1.get(1) == cal2.get(1);
        return isSameYear;
    }

    public static Boolean isSameMonth(Date date1, Date date2) {
        if (!isSameYear(date1, date2)) {
            return false;
        } else {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            boolean isSameMonth = cal1.get(2) == cal2.get(2);
            return isSameMonth;
        }
    }

    public static Boolean biggerThanByMonth(Date date1, Date date2, int n) {
        if (!isSameYear(date1, date2)) {
            date2 = DateUtils.addMonths(date2, n);
            return !isSameYear(date1, date2) ? true : get(date1, date2, n);
        } else {
            return get(date1, date2, n);
        }
    }

    private static Boolean get(Date date1, Date date2, int n) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int result = cal1.get(2) - cal2.get(2);
        return result >= n;
    }

    public static Boolean isSameWeek(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setFirstDayOfWeek(2);
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(3) == cal2.get(3) ? true : false;
    }

    public static Boolean isSameDate(Date date1, Date date2) {
        if (!isSameMonth(date1, date2)) {
            return false;
        } else {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            boolean isSameDate = cal1.get(5) == cal2.get(5);
            return isSameDate;
        }
    }

    public static class TimePeriod {
        private String startTime;
        private String endTime;

        public String getStartTime() {
            return this.startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String toString() {
            return "TimePeriod{startTime='" + this.startTime + '\'' + ", endTime='" + this.endTime + '\'' + '}';
        }

        public TimePeriod() {
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof TimePeriod)) {
                return false;
            } else {
                TimePeriod other = (TimePeriod) o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$startTime = this.getStartTime();
                    Object other$startTime = other.getStartTime();
                    if (this$startTime == null) {
                        if (other$startTime != null) {
                            return false;
                        }
                    } else if (!this$startTime.equals(other$startTime)) {
                        return false;
                    }

                    Object this$endTime = this.getEndTime();
                    Object other$endTime = other.getEndTime();
                    if (this$endTime == null) {
                        if (other$endTime != null) {
                            return false;
                        }
                    } else if (!this$endTime.equals(other$endTime)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof TimePeriod;
        }

        public int hashCode() {
            boolean PRIME = true;
            int result = 1;
            Object $startTime = this.getStartTime();
            result = result * 59 + ($startTime == null ? 43 : $startTime.hashCode());
            Object $endTime = this.getEndTime();
            result = result * 59 + ($endTime == null ? 43 : $endTime.hashCode());
            return result;
        }
    }

    public static enum CycleEnum {
        DAY(1, "每天"),
        WEEK(2, "每天"),
        MONTH(3, "月"),
        SEASON(4, "季"),
        YEAR(5, "年");

        private int codeId;
        private String message;

        public static CycleEnum getCycle(int code) {
            CycleEnum[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                CycleEnum c = var1[var3];
                if (c.codeId == code) {
                    return c;
                }
            }

            return null;
        }

        private CycleEnum(int codeId, String message) {
            this.codeId = codeId;
            this.message = message;
        }

        public int getCodeId() {
            return this.codeId;
        }

        public String getMessage() {
            return this.message;
        }
    }
}