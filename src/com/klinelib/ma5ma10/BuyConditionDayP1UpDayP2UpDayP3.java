package com.klinelib.ma5ma10;

import com.stock.KLineRec;

public class BuyConditionDayP1UpDayP2UpDayP3  implements BuyCondition {
	public boolean getBuyCondition(KLineRec[] kLineRecs, int i) {
		return kLineRecs[i + 1].m_close > kLineRecs[i + 2].m_close * 1
				&& kLineRecs[i + 2].m_close > kLineRecs[i + 3].m_close;
	}

}