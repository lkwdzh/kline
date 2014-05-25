package com.klinelib.ma5ma10;

import com.stock.KLineRec;

public interface BuyCondition {
	public boolean getBuyCondition(KLineRec[] kLineRecs, int i);
}
