package org.ggs.util;

import java.util.*;
import java.text.*;

/**
 * Title:日期处理工具
 * 
 * @author ggs 高国生
 * 
 * 
 * Description:对日期的相关操作
 * 
 * 
 * 
 * Copyright: Copyright (c) 2003
 * 
 * 
 * 
 * 
 * @version 1.0
 */
public class DateUtil {

	/**
	 * 得到当前的日期字符串，方便存放文件夹
	 * 
	 * @param date
	 *            日期对象
	 * @return 返回日期字符串
	 */
	public static final String getDateString(Date date) {
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyy-MM-dd");
		return formattxt.format(date);
	}

	/**
	 * 得到日期/时间字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 返回日期/时间字符串
	 */
	public static final String getDateTimeString(Date date) {
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formattxt.format(date);
	}

	/**
	 * 得到当前日期/时间字符串
	 * 
	 * @return 返回日期/时间字符串
	 */
	public static final String getNowDateTimeString() {
		Date date = new Date();
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formattxt.format(date);
	}

	/**
	 * 得到当前日期字符串
	 * 
	 * @return 返回当前日期字符串
	 */
	public static final String getNowDateString() {
		Date date = new Date();
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyy-MM-dd");
		return formattxt.format(date);
	}

	/**
	 * 得到日期对象
	 * 
	 * @param dateTimeString
	 *            日期字符串
	 * @return 返回日期对象
	 */
	public static final Date getDate(String dateTimeString) {
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = formattxt.parse(dateTimeString);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			date = new Date();
		}
		return date;
	}

	/**
	 * 得到上一年的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 上一年的日期字符串
	 */
	public static final String getLastYearString(Date date) {
		Calendar lastYear = Calendar.getInstance();
		lastYear.setTime(date);
		lastYear.add(Calendar.YEAR, -1);
		return DateUtil.getDateString(lastYear.getTime());
	}

	/**
	 * 得到上一月的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 上一月的日期字符串
	 */
	public static final String getLastMonthString(Date date) {
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTime(date);
		lastMonth.add(Calendar.MONTH, -1);
		return DateUtil.getDateString(lastMonth.getTime());
	}

	/**
	 * 得到上一周的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 上一月周日期字符串
	 */
	public static final String getLastWeekString(Date date) {
		Calendar lastWeek = Calendar.getInstance();
		lastWeek.setTime(date);
		lastWeek.add(Calendar.HOUR_OF_DAY, -168);
		return DateUtil.getDateString(lastWeek.getTime());
	}

	/**
	 * 得到昨天的日期字符串
	 * 
	 * @param date
	 *            日期对象
	 * @return 昨天的日期字符串
	 */
	public static final String getYesterdayString(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(Calendar.HOUR_OF_DAY, -24);
		return DateUtil.getDateString(yesterday.getTime());
	}

	/**
	 * 得到当前日期字符串，没有连接线的
	 * 
	 * @return 返回当前日期字符串
	 */
	public static final String getNoLineNowDateString() {
		Date date = new Date();
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyyMMdd");
		return formattxt.format(date);
	}

	/**
	 * 取得当时的时间戳。年（四位）_月（两位）_日（两位）_时（两位）_分（两位）_秒（两位）_毫秒（三位）
	 * 
	 * @return 当时的时间戳
	 */
	public static final String getNowTimeStamp() {
		Date date = new Date();
		SimpleDateFormat formattxt = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss_SSS");
		return formattxt.format(date);

	}

	/**
	 * 取得当时的时间戳。
	 * 
	 * @return 当时的时间戳
	 */
	public static final String getTimeMillis() {
		Date date = new Date();
		SimpleDateFormat formattxt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return formattxt.format(date);
	}

	/**
	 * 返回星期 数字，0周日，1周1，...6周六
	 */

	public static int getWeek() {
		int newWeek = 0;
		String theWeek = "";
		Date date1 = new Date();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStringToParse = bartDateFormat.format(date1);
		// String dateStringToParse = thedate;
		try {
			Date date = bartDateFormat.parse(dateStringToParse);
			SimpleDateFormat bartDateFormat2 = new SimpleDateFormat("EEEE");
			theWeek = bartDateFormat2.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (theWeek.equals("星期日"))
			newWeek = 0;
		else if (theWeek.equals("星期一"))
			newWeek = 1;
		else if (theWeek.equals("星期二"))
			newWeek = 2;
		else if (theWeek.equals("星期三"))
			newWeek = 3;
		else if (theWeek.equals("星期四"))
			newWeek = 4;
		else if (theWeek.equals("星期五"))
			newWeek = 5;
		else if (theWeek.equals("星期六"))
			newWeek = 6;

		return newWeek;
	}
	
	/**
	 * 返回星期中文
	 */

	public static String getWeekCH() {
		int newWeek = 0;
		String theWeek = "";
		Date date1 = new Date();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStringToParse = bartDateFormat.format(date1);
		// String dateStringToParse = thedate;
		try {
			Date date = bartDateFormat.parse(dateStringToParse);
			SimpleDateFormat bartDateFormat2 = new SimpleDateFormat("EEEE");
			theWeek = bartDateFormat2.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return theWeek;
		
	}

	public static void main(String[] args) {
		System.out.println(getWeek());
	}
}
