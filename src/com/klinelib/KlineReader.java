package com.klinelib;

import java.io.DataInputStream;
import java.util.Date;

import lido.common.CommEnv;

import com.stock.KLineRec;
import com.util.DateUtil;

public class KlineReader {
	public static KLineRec[] getKLineRec(String stockID, int tradeDays) {
		stockID = processStockIDPostfix(stockID);
		KLineRec[] result = null;
		String historyKLineURL = "http://222.73.229.19/lidoo/servlet/static";
		Date today = DateUtil.getTomorrowYMD();

		historyKLineURL = historyKLineURL + "?query_type=" + "42" + "&code="
				+ stockID + "&cycle=day" + "&end_time="
				+ Long.toString(today.getTime()) + "&rec_num="
				+ Integer.toString(tradeDays) + "&field_num="
				+ Integer.toString(7) + "&field="
				+ "trade_time+open+high+low+close+volume+money";

		System.out.println("historyKLineURL = " + historyKLineURL);
		
		
		DataInputStream dataIn = CommEnv.f_urlConn(historyKLineURL);

		try {
			if (dataIn != null) {
				int l_result = dataIn.readInt();
				if (l_result == 4200) {
					int l_count = dataIn.readInt();
					if (l_count <= 0) {
						return null;
					}
					result = new KLineRec[l_count];
					for (int i = 0; i < l_count; i++) {
						result[i] = new KLineRec();
						result[i].m_tradeDate = new Date(dataIn.readLong());
						result[i].m_open = dataIn.readDouble();
						result[i].m_high = dataIn.readDouble();
						result[i].m_low = dataIn.readDouble();
						result[i].m_close = dataIn.readDouble();
						result[i].m_volume = dataIn.readDouble();
						result[i].m_money = dataIn.readDouble();
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}

	private static String processStockIDPostfix(String stockID) {
		if (stockID.indexOf("6") == 0) {
			stockID += ".SS";
		} else {
			stockID += ".SZ";
		}
		return stockID;
	}

	public static void main(String[] args){
		String stockID = "000001";
		int tradeDays = 10;
		KLineRec[] kLineRecs = getKLineRec(stockID, tradeDays);
		for(int i = 0; i< kLineRecs.length; i++){
			System.out.println("i = " + (i+1) + " " + kLineRecs[i]);
		}
	}

}
