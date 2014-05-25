package com.klinelib.ma5ma10;

import com.stock.KLineRec;

public class BuyConditionMA5UpMA10 implements BuyCondition {
	public boolean getBuyCondition(KLineRec[] kLineRecs, int i){
		return kLineRecs[i + 1].ma_five > kLineRecs[i + 1].ma_ten * 1;
	}
}
