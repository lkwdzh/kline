package com.klinelib.analyst;

import com.stock.KLineRec;
import com.util.NumberUtil;

public class MA5RoundMA10AnalysingStrategy implements AnalysingStrategy {
	public boolean isMeet(KLineRec[] kLineRecs) {
		boolean result = true;
		Object[] indicators = MA5RoundMA10IndicatorReader.getInstance()
				.readIndicator();
		for (int i = 0; i < indicators.length; i++) {
			MA5RoundMA10Indicator indicator = (MA5RoundMA10Indicator) indicators[i];
			// System.out.println(kLineRecs[i]);
			double ratio = (kLineRecs[i].ma_five - kLineRecs[i].ma_ten)
					/ kLineRecs[i].ma_ten;
			ratio = NumberUtil.roundDoubleWith4Numbers(ratio);
			// System.out.println("Day = " + (i + 1) + " : ratio = " + ratio
			// + " MA10 = " + kLineRecs[i].ma_ten + " MA5 = "
			// + kLineRecs[i].ma_five);

			if (indicator.isUp()) {
				// if (kLineRecs[i].ma_five < kLineRecs[i].ma_ten
				// * (1 + indicator.getUpRatio())) {
				if (ratio < indicator.getUpRatio()) {
					result = false;
					break;
				}
			} else if (indicator.isDown()) {
				// if (kLineRecs[i].ma_five > kLineRecs[i].ma_ten
				// * (1 - indicator.getDownRatio())) {
				if (ratio > indicator.getUpRatio()) {
					result = false;
					break;
				}
			} else if (indicator.isScope()) {
				// if (kLineRecs[i].ma_five > kLineRecs[i].ma_ten
				// * (1 + indicator.getUpRatio())
				// || kLineRecs[i].ma_five < kLineRecs[i].ma_ten
				// * (1 - indicator.getDownRatio())) {
				if (ratio > indicator.getUpRatio()
						|| ratio < indicator.getDownRatio()) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
}
