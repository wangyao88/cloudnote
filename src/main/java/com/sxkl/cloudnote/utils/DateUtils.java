package com.sxkl.cloudnote.utils;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static final String DATE_PATTON_1 = "yyyy-MM-dd";
	public static final String DATE_PATTON_2 = "yyyy/MM/dd";
	public static final String DATE_PATTON_3 = "yyyyMMdd";
	public static final String DATE_PATTON_4 = "MM-dd";
	public static final String DATE_TIME_PATTON_1 = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_TIME_PATTON_2 = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_TIME_PATTON_3 = "yyyyMMddHHmmss";
	public static final String DATE_TIME_PATTON_4 = "yyyyMMddHHmmssSSS";
	public static final int C_ONE_SECOND = 1000;
	public static final int C_ONE_MINUTE = 60000;
	public static final long C_ONE_HOUR = 3600000L;
	public static final long C_ONE_DAY = 86400000L;

	public static int findMaxDayInMonth() {
		return findMaxDayInMonth(0, 0);
	}

	public static int findMaxDayInMonth(java.util.Date date) {
		if (date == null) {
			return 0;
		}
		return findMaxDayInMonth(date2Calendar(date));
	}

	public static int findMaxDayInMonth(Calendar calendar) {
		if (calendar == null) {
			return 0;
		}

		return calendar.getActualMaximum(5);
	}

	public static int findMaxDayInMonth(int month) {
		return findMaxDayInMonth(0, month);
	}

	public static int findMaxDayInMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		if (year > 0) {
			calendar.set(1, year);
		}

		if (month > 0) {
			calendar.set(2, month - 1);
		}

		return findMaxDayInMonth(calendar);
	}

	public static java.util.Date calendar2Date(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.getTime();
	}

	public static Calendar date2Calendar(java.util.Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static SimpleDateFormat getSimpleDateFormat() {
		return getSimpleDateFormat(null);
	}

	public static SimpleDateFormat getSimpleDateFormat(String format) {
		SimpleDateFormat sdf;
		if (format == null)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else {
			sdf = new SimpleDateFormat(format);
		}

		return sdf;
	}

	public static java.sql.Date getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(5, 1);
		return new java.sql.Date(cal.getTime().getTime());
	}

	public static String getFirstDayOfMonthAsString(String format) {
		java.sql.Date date = getFirstDayOfMonth();
		if ((format != null) && (format.length() > 0)) {
			return formatDate2Str(date, format);
		}
		return formatDate2Str(date);
	}

	public static java.sql.Date getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(5, 1);
		cal.add(2, 1);
		cal.add(5, -1);
		return new java.sql.Date(cal.getTime().getTime());
	}

	public static String getLastDayOfMonthAsString(String format) {
		java.sql.Date date = getLastDayOfMonth();
		if ((format != null) && (format.length() > 0)) {
			return formatDate2Str(date, format);
		}
		return formatDate2Str(date);
	}

	public static String formatDate2Str() {
		return formatDate2Str(new java.util.Date());
	}

	public static String formatDate2Str(java.util.Date date) {
		return formatDate2Str(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDate2Str(String format) {
		return formatDate2Str(new java.util.Date(), format);
	}

	public static String formatDate2Str(java.util.Date date, String format) {
		if (date == null) {
			return null;
		}

		if ((format == null) || (format.equals(""))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = getSimpleDateFormat(format);
		return sdf.format(date);
	}

	public static java.util.Date formatStr2Date(String dateStr) {
		return formatStr2Date(dateStr, null);
	}

	public static java.util.Date formatStr2Date(String dateStr, String format) {
		if (dateStr == null) {
			return null;
		}

		if ((format == null) || (format.equals(""))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = getSimpleDateFormat(format);
		return sdf.parse(dateStr, new ParsePosition(0));
	}

	public static String formatDefault2Define(String dateStr, String defineFormat) {
		return formatSource2Target(dateStr, "yyyy-MM-dd HH:mm:ss", defineFormat);
	}

	public static String formatSource2Target(String dateStr, String sourceFormat, String targetFormat) {
		java.util.Date date = formatStr2Date(dateStr, sourceFormat);
		return formatDate2Str(date, targetFormat);
	}

	public static int findWeeksNoInYear() {
		return findWeeksNoInYear(new java.util.Date());
	}

	public static int findWeeksNoInYear(java.util.Date date) {
		if (date == null) {
			return 0;
		}
		return findWeeksNoInYear(date2Calendar(date));
	}

	public static int findWeeksNoInYear(Calendar calendar) {
		if (calendar == null) {
			return 0;
		}
		return calendar.get(3);
	}

	public static java.util.Date findDateInWeekOnYear(int year, int weekInYear, int dayInWeek) {
		Calendar calendar = Calendar.getInstance();
		if (year > 0) {
			calendar.set(1, year);
		}

		calendar.set(3, weekInYear);
		calendar.set(7, dayInWeek);

		return calendar.getTime();
	}

	public static java.util.Date dayBefore2Date(int days) {
		java.util.Date date = new java.util.Date();
		return (java.util.Date) dayBefore2Object(days, null, date);
	}

	public static String dayBefore2Str(int days) {
		String string = formatDate2Str();
		return (String) dayBefore2Object(days, null, string);
	}

	public static <T> T dayBefore2Object(int days, String format, T instance) {
		Calendar calendar = Calendar.getInstance();
		if (days == 0) {
			return null;
		}

		if ((format == null) || (format.equals(""))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		calendar.add(5, days);
		if ((instance instanceof java.util.Date))
			return (T) calendar.getTime();
		if ((instance instanceof String)) {
			return (T) formatDate2Str(calendar2Date(calendar), format);
		}
		return null;
	}

	public static java.util.Date defineDayBefore2Date(java.util.Date date, int days) {
		java.util.Date dateInstance = new java.util.Date();
		return (java.util.Date) defineDayBefore2Object(date, days, null, dateInstance);
	}

	public static String defineDayBefore2Str(java.util.Date date, int days) {
		String stringInstance = formatDate2Str();
		return (String) defineDayBefore2Object(date, days, null, stringInstance);
	}

	public static <T> T defineDayBefore2Object(java.util.Date date, int days, String format, T instance) {
		if ((date == null) || (days == 0)) {
			return null;
		}

		if ((format == null) || (format.equals(""))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		Calendar calendar = date2Calendar(date);
		calendar.add(5, days);
		if ((instance instanceof java.util.Date))
			return (T) calendar.getTime();
		if ((instance instanceof String)) {
			return (T) formatDate2Str(calendar2Date(calendar), format);
		}
		return null;
	}

	public static java.util.Date monthBefore2Date(int months) {
		java.util.Date date = new java.util.Date();
		return (java.util.Date) monthBefore2Object(months, null, date);
	}

	public static String monthBefore2Str(int months) {
		String string = formatDate2Str();
		return (String) monthBefore2Object(months, null, string);
	}

	public static <T> T monthBefore2Object(int months, String format, T instance) {
		if (months == 0) {
			return null;
		}

		if ((format == null) || (format.equals(""))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(2, months);

		if ((instance instanceof java.util.Date))
			return (T) calendar.getTime();
		if ((instance instanceof String)) {
			return (T) formatDate2Str(calendar2Date(calendar), format);
		}

		return null;
	}

	public static java.util.Date defineMonthBefore2Date(java.util.Date date, int months) {
		java.util.Date dateInstance = new java.util.Date();
		return (java.util.Date) defineMonthBefore2Object(date, months, null, dateInstance);
	}

	public static String defineMonthBefore2Str(java.util.Date date, int months) {
		String stringInstance = formatDate2Str();
		return (String) defineMonthBefore2Object(date, months, null, stringInstance);
	}

	public static <T> T defineMonthBefore2Object(java.util.Date date, int months, String format, T instance) {
		if (months == 0) {
			return null;
		}

		if ((format == null) || (format.equals(""))) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		Calendar calendar = date2Calendar(date);
		calendar.add(2, months);

		if ((instance instanceof java.util.Date))
			return (T) calendar.getTime();
		if ((instance instanceof String)) {
			return (T) formatDate2Str(calendar2Date(calendar), format);
		}

		return null;
	}

	public static int caculate2Days(java.util.Date firstDate, java.util.Date secondDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDate);
		int dayNum1 = calendar.get(6);
		calendar.setTime(secondDate);
		int dayNum2 = calendar.get(6);
		return Math.abs(dayNum1 - dayNum2);
	}

	public static int caculate2Days(Calendar firstCalendar, Calendar secondCalendar) {
		if (firstCalendar.after(secondCalendar)) {
			Calendar calendar = firstCalendar;
			firstCalendar = secondCalendar;
			secondCalendar = calendar;
		}

		long calendarNum1 = firstCalendar.getTimeInMillis();
		long calendarNum2 = secondCalendar.getTimeInMillis();
		return Math.abs((int) ((calendarNum1 - calendarNum2) / 86400000L));
	}

	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds ";
	}

	/**
	 * 
	 * @param begin
	 *            时间段的开始
	 * @param end
	 *            时间段的结束
	 * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
	 * @author fy.zhang
	 */
	public static String formatDuring(Date begin, Date end) {
		return formatDuring(end.getTime() - begin.getTime());
	}

	public static String formatDate3(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTON_3);
		return format.format(date);
	}
	
	public static String formatDate4(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTON_4);
		return format.format(date);
	}

	public static Date getBeforeSevenDay() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		return calendar.getTime();
	}

	public static String getNowMonthDay() {
		DateFormat format = new SimpleDateFormat(DATE_PATTON_4);
		Date date = new Date();
		return format.format(date);
	}
}