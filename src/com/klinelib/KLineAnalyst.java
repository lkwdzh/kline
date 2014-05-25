package com.klinelib;

import java.util.List;

import com.klinelib.analyst.AnalysingContext;
import com.klinelib.analyst.MA5RoundMA10IndicatorReader;
import com.stock.KLineRec;
import com.util.DateUtil;
import com.util.NumberUtil;

public class KLineAnalyst {

	public static KLineRec[] getKLineData(String stockID, int tradeDays) {
		KLineRec[] kLineRecs = KlineReader.getKLineRec(stockID, tradeDays);

		if (kLineRecs == null || kLineRecs.length < tradeDays
				|| !DateUtil.isToday(kLineRecs[0].m_tradeDate)) {
			return null;
		}
		generateMAPrice(kLineRecs);
		return kLineRecs;
	}

	public static void generateMAPrice(KLineRec[] kLineRecs) {
		for (int i = 0; i < kLineRecs.length; i++) {
			double ma10Price = getMAPrice(kLineRecs, 10, i);
			kLineRecs[i].ma_ten = ma10Price;
			double ma5Price = getMAPrice(kLineRecs, 5, i);
			kLineRecs[i].ma_five = ma5Price;
		}
	}

	public final static int TEN_DAYS = 10;

	public static void checkMeetAnalysingStrategy(String stockID) {
		if(stockID.indexOf("3") == 0){
			return;
		}
		int trade_days = MA5RoundMA10IndicatorReader.getInstance()
				.readIndicator().length
				+ TEN_DAYS;
		KLineRec[] kLineRecs = getKLineData(stockID, trade_days);
		if (kLineRecs == null) {
			//System.out.println("Kline is null");
			return;
		}
		AnalysingContext analysingContext = new AnalysingContext("MA5RoundMA10");
		boolean selected = analysingContext.isMeet(kLineRecs);
		if (selected) {
			System.out.println("*********************stockID = " + stockID);
		}
	}

	private static double getMAPrice(KLineRec[] kLineRecs, int maDays, int dayNo) {
		double result = 0;
		if (kLineRecs.length > maDays + dayNo) {
			double maPrice = 0;
			for (int j = dayNo; j < maDays + dayNo; j++) {
				// System.out.println("J = " + j + " Close = " +
				// kLineRecs[j].m_close);
				maPrice += kLineRecs[j].m_close;
			}
			maPrice = maPrice / maDays;
			result = NumberUtil.roundDoubleWith2Numbers(maPrice);
		}
		return result;
	}

	public static void main(String[] args) {
		// String stockID = "000672";
		String stockID = "600036";
		//checkMeetAnalysingStrategy(stockID);
		 //String flag = Constants.FLAG;
		 String flag = "SZ";
		 if (args.length > 0) {
		 flag = args[0].toUpperCase();
		 }
		
		 processStock(flag);
		 flag = "SS";
		 processStock(flag);

	}

	private static void processStock(String flag) {
		String stockID;
		List stockSSID = StockIDReader.read(flag);

		for (int i = 0; i < stockSSID.size(); i++) {
			stockID = (String) stockSSID.get(i);
			//System.out.println("Process : " + stockID);
			checkMeetAnalysingStrategy(stockID);
		}
	}

}
