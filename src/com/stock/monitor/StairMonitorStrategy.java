package com.stock.monitor;

import com.stock.StockCostStandardBean;
import com.stock.StockHangQinBean;

public class StairMonitorStrategy extends AbstractMonitorStrategy {
	protected boolean checkSellCondition(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		double currentCostPrice = stockCostStandardBean
				.getCurrentCostStandardPrice();
		double upMonitoringPercentPrice = currentCostPrice
				* (1 + stockCostStandardBean.getZf());
		return stockHangQin.getCurrentPrice() >= upMonitoringPercentPrice;
	}

	protected boolean checkBuyCondition(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		double currentCostPrice = stockCostStandardBean
				.getCurrentCostStandardPrice();
		double downMonitoringPercentPrice = currentCostPrice
				* (1 - stockCostStandardBean.getDf());
		return stockHangQin.getCurrentPrice() <= downMonitoringPercentPrice;
	}

	protected int sellStock(StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		// message = stockHangQin.getName() + "��"
		// + stockCostStandardBean.getZf() * 100 + "%��"
		// + stockHangQin.getCurrentPrice() + ", ��1000";
		int currentSellNumber = stockCostStandardBean.getTradeRankNumber()
				* stockCostStandardBean.getCurrentLevel();
		if (stockCostStandardBean.getCurrentCanTradeNumber() > 0) {
			if (stockCostStandardBean.getCurrentCanTradeNumber() > currentSellNumber) {
				stockCostStandardBean.setCurrentCostStandardPrice(stockHangQin
						.getCurrentPrice());
				stockCostStandardBean
						.setCurrentCanTradeNumber(stockCostStandardBean
								.getCurrentCanTradeNumber()
								- currentSellNumber);
				stockCostStandardBean.setCurrentNumber(stockCostStandardBean
						.getCurrentNumber()
						- currentSellNumber);
			} else {
				// ***Todo, ��Ϊ֮ǰ�Ĳ����� ���µ�����Ŀ��ܲ���ȫ����.
				currentSellNumber = stockCostStandardBean
						.getCurrentCanTradeNumber();
				stockCostStandardBean.setCurrentCostStandardPrice(stockHangQin
						.getCurrentPrice());
				stockCostStandardBean.setCurrentCanTradeNumber(0);
				stockCostStandardBean.setCurrentNumber(stockCostStandardBean
						.getCurrentNumber()
						- currentSellNumber);
			}
			stockCostStandardBean.setCurrentLevel(stockCostStandardBean
					.getCurrentLevel() - 1);
		}
		return currentSellNumber;
	}

	protected int buyStock(StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		// message = stockHangQin.getName() + "��"
		// + stockCostStandardBean.getDf() * 100 + "%��"
		// + stockHangQin.getCurrentPrice() + ", ��1000";
		stockCostStandardBean.setCurrentCostStandardPrice(stockHangQin
				.getCurrentPrice());
		int currentLevel = stockCostStandardBean.getCurrentLevel() + 1;
		int currentBuyNumber = stockCostStandardBean.getTradeRankNumber()
				* currentLevel;
		stockCostStandardBean.setCurrentNumber(stockCostStandardBean
				.getCurrentNumber()
				+ currentBuyNumber);
		stockCostStandardBean.setCurrentLevel(currentLevel);
		return currentBuyNumber;

	}
}
