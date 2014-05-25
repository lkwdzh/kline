package com.stock.monitoringway;

import com.stock.AbstractStockMonitor;
import com.stock.StockCostStandardBean;
import com.stock.StockHangQinBean;
import com.stock.StockMonitrorResult;

public class TenDaysPriceStockMonitor extends AbstractStockMonitor {
	protected boolean checkSellCondition(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		boolean result = false;
		if (stockCostStandardBean.getCurrentCanTradeNumber() > 0) {
			double currentCostPrice = stockCostStandardBean
					.getCurrentCostStandardPrice();
			double upMonitoringPercentPrice = currentCostPrice
					* (1 + stockCostStandardBean.getZf());
			if (stockHangQin.getCurrentPrice() >= upMonitoringPercentPrice) {
				result = true;
			}
			double downMonitoringPercentPrice = currentCostPrice
					* (1 - stockCostStandardBean.getDf());
			if (stockHangQin.getCurrentPrice() <= downMonitoringPercentPrice) {
				result = true;
			}
		}
		return result;
	}

	protected boolean checkBuyCondition(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		double current10DaysPrice = TenDaysPriceReader
				.read10DaysPrice(stockCostStandardBean.getStockId());
		return stockHangQin.getCurrentPrice() <= current10DaysPrice
				&& stockCostStandardBean.getCurrentNumber() == 0;
	}

	protected void processTrade(StockCostStandardBean stockCostStandardBean,
			StockMonitrorResult result, StockHangQinBean stockHangQin) {
		if (checkBuyCondition(stockCostStandardBean, stockHangQin)) {
			int buyNumber = buyStock(stockCostStandardBean, stockHangQin);
			result.setTradeNumber(buyNumber);
			result.setBuy(true);
		} else if (checkSellCondition(stockCostStandardBean, stockHangQin)) {
			int sellNumber = sellStock(stockCostStandardBean, stockHangQin);
			result.setTradeNumber(sellNumber);
			result.setBuy(false);
		}
	}

	protected int sellStock(StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		int currentSellNumber = stockCostStandardBean
				.getCurrentCanTradeNumber();
		stockCostStandardBean.setCurrentCanTradeNumber(0);
		stockCostStandardBean.setCurrentNumber(0);
		return currentSellNumber;
	}

	protected int buyStock(StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin) {
		int currentBuyNumber = stockCostStandardBean.getTradeRankNumber();
		stockCostStandardBean.setCurrentNumber(currentBuyNumber);
		stockCostStandardBean.setCurrentCostStandardPrice(stockHangQin
				.getCurrentPrice());
		return currentBuyNumber;
	}

}
