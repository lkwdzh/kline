package com.klinelib.analyst;

import java.util.regex.Pattern;

import com.file.LineParser;

public class MA5RoundMA10IndicatorParser implements LineParser {
	public Object parseLine(String line) {
		// ##1 true, 0.05, false, 0, false\
		Pattern pattern = Pattern.compile(" ");
		String[] monitorStockInfo = pattern.split(line);
		MA5RoundMA10Indicator indicator = new MA5RoundMA10Indicator();
		indicator.setDayNo(Integer.parseInt(monitorStockInfo[0]));
		indicator.setUp(Boolean.parseBoolean(monitorStockInfo[1]));
		indicator.setUpRatio(Double.parseDouble(monitorStockInfo[2]));
		indicator.setDown(Boolean.parseBoolean(monitorStockInfo[3]));
		indicator.setDownRatio(Double.parseDouble(monitorStockInfo[4]));
		indicator.setScope(Boolean.parseBoolean(monitorStockInfo[5]));
		return indicator;
	}
}
