package com.klinelib.analyst;

import com.stock.KLineRec;

public interface AnalysingStrategy {
	public boolean isMeet(KLineRec[] kLineRecs);
}
