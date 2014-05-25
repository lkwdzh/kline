package com.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date getTomorrowYMD() {
		long now = Calendar.getInstance().getTimeInMillis();
		long tomorrow = now + 24 * 60 * 60 * 1000;
		return getCertainYMD(new Date(tomorrow));
	}
	
	public static Date getTodayYMD() {
		long now = Calendar.getInstance().getTimeInMillis();
		return getCertainYMD(new Date(now));
	}
	
	public static boolean isToday(Date date) {
		long now = Calendar.getInstance().getTimeInMillis();
		return formatYMD(new Date(now)).equals(formatYMD(date));
	}

	public static Date getCertainYMD(int year, int month, int day){
		Calendar.getInstance().set(Calendar.YEAR, year);
		Calendar.getInstance().set(Calendar.MONTH, month);
		Calendar.getInstance().set(Calendar.DAY_OF_MONTH, day);
		Calendar.getInstance().set(Calendar.HOUR, 0);
		Calendar.getInstance().set(Calendar.MINUTE, 0);
		Calendar.getInstance().set(Calendar.SECOND, 0);
		Calendar.getInstance().set(Calendar.MILLISECOND, 0);
		return Calendar.getInstance().getTime();
	}
	
	public static Date getCertainYMD(Date certainDay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date result = null;
		try {
			result = sdf.parse(sdf.format(certainDay));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return result;
	}

	public static String formatYMD(Date date) {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍMM‘¬dd»’");
		try {
			result = sdf.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return result;
	}
}
