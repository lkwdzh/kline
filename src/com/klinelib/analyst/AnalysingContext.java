package com.klinelib.analyst;

import com.stock.KLineRec;

public class AnalysingContext {
	private AnalysingStrategy analysingStrategy;

	public AnalysingContext(String type) {
		if (type == null) {
			throw new IllegalArgumentException(
					"analysing.strategy.type.is.null");
		}
		String analysingStrategyClassName = "com.klinelib.analyst." + type
				+ "AnalysingStrategy";
		try {
			Class analysingStrategyClass = Class
					.forName(analysingStrategyClassName);
			analysingStrategy = (AnalysingStrategy) analysingStrategyClass
					.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isMeet(KLineRec[] kLineRecs) {
		return analysingStrategy.isMeet(kLineRecs);
	}
}
