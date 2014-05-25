package com.klinelib.ma5ma10;

import com.stock.KLineRec;

public class BuyConditionMA5Up implements BuyCondition {
	public boolean getBuyCondition(KLineRec[] kLineRecs, int i) {
		return kLineRecs[i + 1].ma_five > kLineRecs[i + 2].ma_five
				&& kLineRecs[i + 2].ma_five > kLineRecs[i + 3].ma_five;
	}
}
