package com.klinelib;

import java.util.List;

import com.stock.KLineRec;
import com.util.DateUtil;
import com.util.NumberUtil;

public class KLineDataReader {
	public static KLineRec[] getKLineData(String stockID, int tradeDays) {
		KLineRec[] kLineRecs = KlineReader.getKLineRec(stockID, tradeDays);

		if (kLineRecs == null || kLineRecs.length < tradeDays
				|| !DateUtil.isToday(kLineRecs[0].m_tradeDate)) {
			return null;
		}
		generateZDF(kLineRecs);
		return kLineRecs;
	}

	public static void generateZDF(KLineRec[] kLineRecs) {
		for (int i = 0; i < kLineRecs.length; i++) {
			double jinSou = kLineRecs[i].m_close;
			double zuoSou = 0;
			if (i == kLineRecs.length - 1) {
				zuoSou = jinSou;
			} else {
				zuoSou = kLineRecs[i + 1].m_close;
			}
			double zdf = (jinSou - zuoSou) / zuoSou;
			zdf = NumberUtil.roundDoubleWith2Numbers(zdf * 100);
			kLineRecs[i].zdf = zdf;
		}

	}

	public static void main(String[] args) {
		List stockIDList = StockIDReader.read("SS");

		for (int j = 0; j < stockIDList.size(); j++) {
			String stockID = stockIDList.get(j).toString();
			KLineRec[] kLineRecs = getKLineData(stockID, 245);
			int zt = 0;
			if (kLineRecs != null) {
				for (int i = 0; i < kLineRecs.length; i++) {
					if (kLineRecs[i].zdf >= 4) {
						zt++;
					}
				}
				if(zt >= 20){
					System.out.println(stockID + "	" + "	"+ zt);
				}
			}

		}
	}
}
