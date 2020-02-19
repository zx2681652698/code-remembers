package com.ab.dh.api.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	/**
	 * 日期格式化对象，精确到日
	 */
	public final static SimpleDateFormat SDF_DAY = new SimpleDateFormat(
			"yyyy-MM-dd");
	/**
	 * 日期格式化对象，精确到日,反斜杠
	 */
	public final static SimpleDateFormat SDF_DAY_1 = new SimpleDateFormat(
			"yyyy/MM/dd");
	/**
	 * 日期格式化对象，精确到秒
	 */
	public final static SimpleDateFormat SDF_SEND = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	/**
	 * 日期格式化对象，精确到毫秒
	 */
	public final static SimpleDateFormat SDF_MINSEND = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSSZ");
	/**
	 * 将日期格式为字符串
	 */
	public final static SimpleDateFormat SDF_TOSTRING = new SimpleDateFormat(
			"yyMMddhhssmmSSS");

	/**
	 * 将日期格式转为"yyyy年MM月dd日 HH:mm:ss
	 */
	public final static SimpleDateFormat SDF_SEND2 = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm:ss");
    /**
     * 将日期格式转为"yyyy年MM月dd日
     */
	public final static SimpleDateFormat SDF_SEND3 = new SimpleDateFormat(
            "yyyy年MM月dd日");
	public static final String selectMM = "C6FE9A0282E520ADEC6991A646467295";
	/**
	 * 获取服务器当前时间
	 * 
	 * @return
	 */
	public static Date getCurrentTime() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	/**
	 * 获取服务器当前日期
	 * 
	 * @return
	 */
	public static Date getCurrentDay() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}
	/**
	 * 获取当前年
	 * @return
	 */
	public static String getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String yearStr = Integer.toString(year);
		String retrunStr = yearStr.substring(2,yearStr.length());
		return retrunStr;
	}
	/**
	 * 计算日期的函数 author: HST 参照日期指当按照年月进行日期的计算的时候，参考的日期，如下例，结果返回2002-03-31
	 * 
	 * @param baseDate
	 *            起始日期
	 * @param interval
	 *            时间间隔
	 * @param unit
	 *            时间间隔单位
	 * @param compareDate
	 *            参照日期
	 * @return Date类型变量
	 */
	public static Date calDate(Date baseDate, int interval, String unit, Date compareDate) {
		Date returnDate = null;

		GregorianCalendar mCalendar = new GregorianCalendar();
		mCalendar.setTime(baseDate);
		if (unit.equals("Y")) {
			mCalendar.add(Calendar.YEAR, interval);
		}
		if (unit.equals("M")) {
			mCalendar.add(Calendar.MONTH, interval);
		}
		if (unit.equals("D")) {
			mCalendar.add(Calendar.DATE, interval);
		}
		mCalendar.add(Calendar.DATE, -1);

		if (compareDate != null) {
			GregorianCalendar cCalendar = new GregorianCalendar();
			cCalendar.setTime(compareDate);

			int mYears = mCalendar.get(Calendar.YEAR);
			int mMonths = mCalendar.get(Calendar.MONTH);
			int cMonths = cCalendar.get(Calendar.MONTH);
			int cDays = cCalendar.get(Calendar.DATE);
			if (unit.equals("Y")) {
				cCalendar.set(mYears, cMonths, cDays);
				if (cCalendar.before(mCalendar)) {
					mCalendar.set(mYears + 1, cMonths, cDays);
					returnDate = mCalendar.getTime();
				} else {
					returnDate = cCalendar.getTime();
				}
			}
			if (unit.equals("M")) {
				cCalendar.set(mYears, mMonths, cDays);
				if (cCalendar.before(mCalendar)) {
					mCalendar.set(mYears, mMonths + 1, cDays);
					returnDate = mCalendar.getTime();
				} else {
					returnDate = cCalendar.getTime();
				}
			}
			if (unit.equals("D")) {
				returnDate = mCalendar.getTime();
			}
		} else {
			returnDate = mCalendar.getTime();
		}

		return returnDate;
	}
	/**
	 * 计算日期的函数 author: HST 参照日期指当按照年月进行日期的计算的时候，参考的日期，如下例，结果返回2002-03-31
	 * 未测试 
	 * @param tbaseDate
	 *            起始日期
	 * @param interval
	 *            时间间隔
	 * @param unit
	 *            时间间隔单位
	 * @param
	 *
	 * @return String类型变量
	 */
	public static String calSDate(String tbaseDate, int interval, String unit) {
		Date returnDate = null;
		String ReturnDate = null;

		Date baseDate = new Date();
		try {
			baseDate = SDF_DAY.parse(tbaseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		GregorianCalendar mCalendar = new GregorianCalendar();
		mCalendar.setTime(baseDate);
		if (unit.equals("Y"))
			mCalendar.add(Calendar.YEAR, interval);
		if (unit.equals("M"))
			mCalendar.add(Calendar.MONTH, interval);
		if (unit.equals("D"))
			mCalendar.add(Calendar.DATE, interval);
		mCalendar.add(Calendar.DATE, -1);
		returnDate = mCalendar.getTime();
		ReturnDate = SDF_DAY.format(returnDate);
		return ReturnDate;
	}
	public static boolean compare_date(String startdate, String enddate,
			String birthday) {
		try {
			Date start = SDF_DAY.parse(startdate);
			Date end = SDF_DAY.parse(enddate);
			Date Dbirthday = SDF_DAY.parse(birthday);
            return Dbirthday.getTime() <= start.getTime()
                    && Dbirthday.getTime() > end.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 比较两个字符串时间大小
	 * @param DATE1
	 * @param DATE2
	 * @return
	 *
	 * 返回值是1 则 date1>date2 date1 在date2 之后
	 */
	public static int compare_date2(String DATE1, String DATE2) {
		try {
			Date dt1 = SDF_SEND.parse(DATE1);
			Date dt2 = SDF_SEND.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前时间的前后几天
	 */
	public static String getDay(String d,int interval) {
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, interval);
		date = calendar.getTime();
		return SDF_DAY.format(date);
	}


	public static int calcDays(String startDate, String endDate) {
		int days = -1;
		try {
			long start = SDF_SEND.parse(startDate).getTime();
			long end = SDF_SEND.parse(endDate).getTime();
			days = (int) ((end - start) / (24 * 60 * 60 * 1000))+1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}
	/**
	 * 判断订单时间和起保时间的天数,限制支付时间
	 *
	 * @param orderTime
	 * @param startTime
	 * @return
	 * @throws ParseException
	 */
	public static String getPayTimeLimit2(String orderTime, String startTime) {
		String resultMsg = "";
		if (StringUtils.isNotBlank(orderTime) && StringUtils.isNotBlank(startTime)) {
			int i = calcDays(orderTime, startTime);
			if (i >= 3) {
				resultMsg = getSpecifiedDayBefore2(orderTime, 2);
			} else {
				resultMsg = getSpecifiedDayBefore2(startTime, -1);
			}
		}
		return resultMsg;
	}

	/**
	 * 获得指定日期的前一天
	 *
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore2(String specifiedDay, int days) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + days);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}
}
