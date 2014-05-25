package com.klinelib.ma5ma10;

import java.util.List;

import com.klinelib.KLineAnalyst;
import com.klinelib.KlineReader;
import com.klinelib.StockIDReader;
import com.stock.KLineRec;
import com.util.ClassUtil;

public class MA5MA10Analyst {
	public static double ZY = 0.04;
	public static double ZK = 0.016;

	public static void main1(String[] args) {

		// String buyConditionType = "MA5UpMA10";
		String[] markets = { "SS", "SZ" };
		for (int j = 0; j < markets.length; j++) {
			List stockIDs = StockIDReader.read(markets[j]);
			String[] buyConditions = { "DayP1UpDayP2", "MA5Up", "MA5UpMA10" };
			for (int k = 0; k < buyConditions.length; k++) {
				System.out.println(markets[j] + " : " + buyConditions[k]);
				for (int i = 0; i < stockIDs.size(); i++) {
					String stockID = (String) stockIDs.get(i);
					if (stockID.indexOf("3") != 0) {
						processStockEarn(stockID, buyConditions[k]);
						MA5MA10Balance.getInstance().reSetForStock();
					}
				}
				System.out.println("TotalEarn = "
						+ MA5MA10Balance.getInstance().getTotalEarn()
						+ " Ying : "
						+ MA5MA10Balance.getInstance().getYingStockCount()
						+ " Kui : "
						+ MA5MA10Balance.getInstance().getKuiStockCount());
				MA5MA10Balance.getInstance().reSetForMarket();

			}
		}

	}

	public static void main(String[] args) {
		String stockID = "002274";
		String[] buyConditions = { /* "DayP1UpDayP2", "MA5Up", */"MA5UpMA10" };
		for (int i = 0; i < buyConditions.length; i++) {
			processStockEarn(stockID, buyConditions[i]);
		}
		MA5MA10Balance.getInstance().reSetForStock();
	}

	public static final int TRADE_DAYS = 50;

	public static int getTradeDays() {
		return TRADE_DAYS;
	}

	private static void processStockEarn(String stockID, String buyConditionType) {
		MA5MA10Balance.getInstance().reSetForStock();
		BuyCondition buyCondition = (BuyCondition) ClassUtil.getInstance(
				"com.klinelib.ma5ma10.BuyConditionXXXX", buyConditionType);

		boolean logFlag = true;
		boolean isPending = false;
		KLineRec[] kLineRecs = KlineReader.getKLineRec(stockID, getTradeDays());

		if (kLineRecs == null || kLineRecs.length < getTradeDays()) {
			if (logFlag) {
				System.out.println("This stock isn't reach the trade days");
			}
			return;
		}
		KLineAnalyst.generateMAPrice(kLineRecs);
		int tradeTime = 0;
		int yingTime = 0;
		int kuiTime = 0;
		for (int i = getTradeDays() - 12; i >= 0; i--) {
			if (MA5MA10Balance.getInstance().getBuyPrice() == 0
					&& buyCondition.getBuyCondition(kLineRecs, i)) {
				if (logFlag) {
					System.out.println("Buy Day: " + kLineRecs[i]);
				}
				MA5MA10Balance.getInstance().setBuyPrice(kLineRecs[i].m_open);
			} else if (MA5MA10Balance.getInstance().getBuyPrice() != 0) {
				double upRatio = (kLineRecs[i].m_high - MA5MA10Balance
						.getInstance().getBuyPrice())
						/ MA5MA10Balance.getInstance().getBuyPrice();
				double downRatio = (MA5MA10Balance.getInstance().getBuyPrice() - kLineRecs[i].m_low)
						/ MA5MA10Balance.getInstance().getBuyPrice();

				if (upRatio >= ZY) {
					if (logFlag) {
						System.out.println("Sell Day: Ying: " + kLineRecs[i]);
					}
					double earn = MA5MA10Balance.getInstance().getBuyPrice()
							* (ZY - 0.005);
					MA5MA10Balance.getInstance().addEarn(earn);
					MA5MA10Balance.getInstance().reSet();
					// if (logFlag) {
					// System.out.println("SNAPSHOT : "
					// + MA5MA10Balance.getInstance());
					// }
					tradeTime++;
					yingTime++;
				} else if (downRatio >= ZK) {
					if (logFlag) {
						System.out.println("Sell Day: Kui: " + kLineRecs[i]);
					}
					double earn = MA5MA10Balance.getInstance().getBuyPrice()
							* (ZK + 0.005);
					MA5MA10Balance.getInstance().addEarn(earn * (-1));
					MA5MA10Balance.getInstance().reSet();
					// if (logFlag) {
					// System.out.println("SNAPSHOT : "
					// + MA5MA10Balance.getInstance());
					// }
					tradeTime++;
					kuiTime++;
				}
				if (upRatio >= ZY && downRatio >= ZK) {
					isPending = true;
					if (logFlag) {
						System.out.println("YKPending");
					}
				}
			}
		}
		double earnRatio = MA5MA10Balance.getInstance().getEarn()
				/ MA5MA10Balance.getInstance().getLastPrice() * 100;
		if (earnRatio >= 50 && !isPending) {
			System.out.println("StockID = " + stockID + " earnRatio = "
					+ earnRatio);
		}

		System.out.println("SNAPSHOT : " + MA5MA10Balance.getInstance() + " : "
				+ buyConditionType);
		if (logFlag) {
			System.out.println("TradeTime = " + tradeTime);
			System.out.println("YingTime = " + yingTime);
			System.out.println("KuiTime = " + kuiTime);

		}
	}
}
