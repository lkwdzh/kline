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
		// message = stockHangQin.getName() + "涨"
		// + stockCostStandardBean.getZf() * 100 + "%到"
		// + stockHangQin.getCurrentPrice() + ", 卖1000";
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
				// ***Todo, 因为之前的不够， 导致当天买的可能不能全买完.
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
		// message = stockHangQin.getName() + "跌"
		// + stockCostStandardBean.getDf() * 100 + "%到"
		// + stockHangQin.getCurrentPrice() + ", 买1000";
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
