package com.klinelib.analyst;

import java.util.ArrayList;
import java.util.List;

import com.klinelib.Constants;
import com.klinelib.KLineAnalyst;
import com.stock.KLineRec;
import com.util.NumberUtil;
import com.file.FileWriter;

public class IndicatorSimulator {
	private final static int ANALYSING_DAYS = 26;
	private final static int INCREASING_DAYS = 9;

	public static void simulateIndicator(String stockID) {
		KLineRec[] kLineRecs = KLineAnalyst.getKLineData(stockID,
				ANALYSING_DAYS + 10);
		if (kLineRecs == null) {
			return;
		}
		List linesArray = new ArrayList();
		for (int i = 0; i < kLineRecs.length - 10; i++) {
			double ratio = calculateRatioMA10AndMA5(kLineRecs[i]);
			System.out.println("Day = " + (i + 1) + " : ratio = " + ratio
					+ " MA10 = " + kLineRecs[i].ma_ten + " MA5 = "
					+ kLineRecs[i].ma_five);
			if (i >= INCREASING_DAYS) {
				MA5RoundMA10Indicator indicator = generateIndicator(ratio);
				indicator.setDayNo(i + 1 - INCREASING_DAYS);
				linesArray.add(indicator.toString());
			}
		}
		String filePath = Constants.DATA_ROOT
				+ Constants.MA5_ROUND_MA10_INDICATOR;
		FileWriter
				.write(filePath, (String[]) linesArray.toArray(new String[0]));

	}

	private static double calculateRatioMA10AndMA5(KLineRec kLineRec) {
		double ratio = (kLineRec.ma_five - kLineRec.ma_ten) / kLineRec.ma_ten;
		ratio = NumberUtil.roundDoubleWith4Numbers(ratio);
		return ratio;
	}

	private static MA5RoundMA10Indicator generateIndicator(double ratio) {
		MA5RoundMA10Indicator indicator = new MA5RoundMA10Indicator();
		if (ratio >= 0) {
			indicator.setUp(true);
			indicator.setUpRatio(ratio);
		} else {
			indicator.setDown(true);
			indicator.setDownRatio(ratio);
		}
		return indicator;
	}

	public static void main(String[] args) {
		simulateIndicator("002618");

	}
}
