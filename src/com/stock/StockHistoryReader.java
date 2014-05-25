package com.stock;

import java.io.DataInputStream;
import java.util.Date;

import com.util.DateUtil;

import lido.common.CommEnv;

public class StockHistoryReader {

	public static void readHistory() {
		String m_historyKLineUrlHeader = "http://222.73.229.19/lidoo/servlet/static";
		Date today = DateUtil.getTomorrowYMD();

		String l_connStr = m_historyKLineUrlHeader + "?query_type=" + "42"
				+ "&code=" + stockID + "&cycle=day" + "&end_time="
				+ Long.toString(today.getTime()) + "&rec_num="
				+ Integer.toString(tradeDays) + "&field_num="
				+ Integer.toString(7) + "&field="
				+ "trade_time+open+high+low+close+volume+money";
		System.out.println("URL = " + l_connStr);
		
		DataInputStream l_in = null;
		l_in = CommEnv.f_urlConn(l_connStr);
		Object l_arg = null;
		try {
			if (l_in != null) {
				int l_result = l_in.readInt();
				if (l_result == 4200) {
					int l_count = l_in.readInt();
					if (l_count <= 0)
						return;
					KLineRec[] l_trans = new KLineRec[l_count];
					for (int l_i = 0; l_i < l_count; l_i++) {
						l_trans[l_i] = new KLineRec();
						l_trans[l_i].m_tradeDate = new Date(l_in.readLong());
						l_trans[l_i].m_open = l_in.readDouble();
						l_trans[l_i].m_high = l_in.readDouble();
						l_trans[l_i].m_low = l_in.readDouble();
						l_trans[l_i].m_close = l_in.readDouble();
						l_trans[l_i].m_volume = l_in.readDouble();
						l_trans[l_i].m_money = l_in.readDouble();
						
						System.out.println((l_i + 1) + " " + l_trans[l_i]);
//						System.out.println("abc = " + l_in.readDouble());
//						System.out.println("abc = " + l_in.readDouble());
//						System.out.println("abc = " + l_in.readDouble());
//						System.out.println("abc = " + l_in.readDouble());
//						System.out.println("abc = " + l_in.readDouble());
						//l_in.readLine();

					}

					l_arg = l_trans;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
//		KLineRec[] klikeRecs = (KLineRec[]) l_arg;
//		int size = klikeRecs.length;
//		AssessmentBalance ab = new AssessmentBalance(initialMoney, 0, 0);
//		System.out.println("begin: " + ab);
//		int buytimes = 0;
//		int selltimes = 0;
//		for (int i = size - 1; i >= 0; i--) {
//			ab.setCurrentprice(klikeRecs[i].m_close);
//			ab.setCurrentDate(klikeRecs[i].m_tradeDate);
//			if (ab.getStockcount() == 0) {
//				double open = klikeRecs[i].m_open;
//				double buyMoney = batchNumber * open;
//				if (open <= controllPrice && ab.getMoney() > buyMoney) {
//					ab.setStockcount(batchNumber);
//					ab.setCurrentmonitorprice(open);
//					
//					ab.setMoney(ab.getMoney() - buyMoney);
//					ab.staticSumBuyMoney(buyMoney);
//					ab.setMonitorLevel(tradeLevel);
//					buytimes++;
//					System.out.println(" I买  " + batchNumber + " " + ab
//							+ " 股票信息: " + klikeRecs[i]);
//				}
//			} else {
//				double high = klikeRecs[i].m_high;
//				double ZF = ab.getCurrentmonitorprice() * monitoringZF;
//				while (high >= ZF) {
//					if (ab.getStockcount() > 0) {
//						int sellcount = batchNumber * ab.getMonitorLevel();
//						ab.setStockcount(ab.getStockcount() - sellcount);
//						ab.setCurrentmonitorprice(ZF);
//						ab.setMoney(ab.getMoney() + ZF * sellcount);
//						selltimes++;
//						ZF = ab.getCurrentmonitorprice() * monitoringZF;
//						ab.setMonitorLevel(ab.getMonitorLevel() - tradeLevel);
//						System.out.println("卖  " + sellcount + " " + ab
//								+ " 股票信息: " + klikeRecs[i]);
//
//					} else {
//						break;
//					}
//				}
//				double low = klikeRecs[i].m_low;
//				double DF = ab.getCurrentmonitorprice() * monitoringDF;
//				while (low <= DF) {
//					if (ab.getMoney() > DF * batchNumber
//							* (ab.getMonitorLevel() + 1)) {
//						int buycount = batchNumber * (ab.getMonitorLevel() + 1);
//						ab.setStockcount(ab.getStockcount() + buycount);
//						ab.setCurrentmonitorprice(DF);
//						double buyMoney = DF * buycount;
//						ab.setMoney(ab.getMoney() - buyMoney);
//						ab.staticSumBuyMoney(buyMoney);
//						buytimes++;
//						DF = ab.getCurrentmonitorprice() * monitoringDF;
//						ab.setMonitorLevel(ab.getMonitorLevel() + tradeLevel);
//						System.out.println("买  " + buycount + " " + ab
//								+ " 股票信息: " + klikeRecs[i]);
//					} else {
//						break;
//					}
//				}
//			}
//		}
//		System.out.println("end: " + ab);
//		System.out.println("buy times: " + buytimes);
//		System.out.println("sell times: " + selltimes);
	}

	public static double monitoringZF = 1.02;
	public static double monitoringDF = 0.98;
	public static int batchNumber = 1000;
	public static double initialMoney = 120000;
	public static int tradeDays = 10;
	public static String stockID = "000001.SZ";
	public static int tradeLevel = 1;
	public static double controllPrice = 12.53;

	public static void main(String[] args) {
		readHistory();
	}
}
