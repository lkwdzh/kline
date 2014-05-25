package com.klinelib.ma5ma10;

import com.stock.KLineRec;

public class BuyConditionDayP1UpDayP2 implements BuyCondition {
	public boolean getBuyCondition(KLineRec[] kLineRecs, int i) {
		return kLineRecs[i + 1].m_close > kLineRecs[i + 2].m_close * 1;
	}

}

// if (kLineRecs[i + 1].ma_ten >= kLineRecs[i + 2].ma_ten &&
// kLineRecs[i + 1].ma_five >= kLineRecs[i + 2].ma_five) {
// double increasingRatio = (kLineRecs[i + 1].m_close - kLineRecs[i + 1 +
// 3].m_close)
// / kLineRecs[i + 1].m_close * 100;
// if (increasingRatio >= 5) {
