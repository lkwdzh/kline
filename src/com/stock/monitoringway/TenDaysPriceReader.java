package com.stock.monitoringway;

import com.klinelib.KlineReader;
import com.stock.KLineRec;
import com.util.NumberUtil;

public class TenDaysPriceReader {
	private static int DAYS = 10;

	public static double read10DaysPrice(String stockID) {
		return readPrice(stockID, DAYS);
	}
	

	public static boolean checkMA5UpMA10P1(String stockID) {
		double sum = 0;
		int tradeDays = 11;
		KLineRec[] kLineRecs = KlineReader.getKLineRec(stockID, tradeDays);
		for (int i = 1; i < kLineRecs.length; i++){
			sum += kLineRecs[i].m_close;
		}
		double ma10P1 = sum / tradeDays - 1;
		sum = 0;
		for (int i = 1; i < 6; i++){
			sum += kLineRecs[i].m_close;
		}
		double ma5P1 = sum / 5;
		return ma5P1 > ma10P1;
	}

	
	private static double readPrice(String stockID, int tradeDays) {
		double result = 0;
		KLineRec[] kLineRecs = KlineReader.getKLineRec(stockID, tradeDays);
		for (int i = 0; i < kLineRecs.length; i++){
			result += kLineRecs[i].m_close;
		}
		result = result / tradeDays;
		result = NumberUtil.roundDoubleWith2Numbers(result);
		return result;
	}
	public static void main(String[] args) {
		double tenDaysPrice = read10DaysPrice("000001");
		System.out.println("tenDaysPrice = " + tenDaysPrice);
	}
}
