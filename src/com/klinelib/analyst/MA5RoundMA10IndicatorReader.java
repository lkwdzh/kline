package com.klinelib.analyst;

import com.file.BasicFileReader;
import com.file.FileReader;
import com.klinelib.Constants;

public class MA5RoundMA10IndicatorReader {
	private Object[] indicators = null;
	private static MA5RoundMA10IndicatorReader instance = new MA5RoundMA10IndicatorReader();

	public static MA5RoundMA10IndicatorReader getInstance() {
		return instance;
	}

	private MA5RoundMA10IndicatorReader() {

	}

	public Object[] readIndicator() {
		if (indicators == null) {
			MA5RoundMA10IndicatorParser indicatorParser = new MA5RoundMA10IndicatorParser();
			String filePath = Constants.DATA_ROOT
					+ Constants.MA5_ROUND_MA10_INDICATOR;
			FileReader fileReader = new BasicFileReader(filePath,
					indicatorParser);
			indicators = fileReader.readLinesToObjects();
		}
		return indicators;
	}
}
