package com.klinelib.analyst;

import com.stock.KLineRec;

public class MA5UpMA10AnalysingStrategy implements AnalysingStrategy{
	public boolean isMeet(KLineRec[] kLineRecs){
		boolean result = true;
		for (int i = 2; i < 10; i++) {
			if (kLineRecs[i].m_close < kLineRecs[i].ma_five) {
				result = false;
				break;
			}
		}
		for (int i = 0; i < 2; i++) {
			if (kLineRecs[i].m_close >= kLineRecs[i].ma_ten) {
				result = false;
				break;
			}
		}
		return result;
	}
}
