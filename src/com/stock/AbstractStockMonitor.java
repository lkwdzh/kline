package com.stock;

public abstract class AbstractStockMonitor implements StockMonitor {
	protected StockHangQinBean queryStockHangQin(String stockId, boolean isSz)
			throws Exception {
		StockHangQinBean result = StockReader.getStockHangQinFromInternet(
				stockId, isSz);
		return result;
	}

	public StockMonitrorResult monitorStock(
			StockCostStandardBean stockCostStandardBean) {
		StockMonitrorResult result = new StockMonitrorResult();
		try {
			StockHangQinBean stockHangQin = this.queryStockHangQin(
					stockCostStandardBean.getStockId(), stockCostStandardBean
							.isSz());
			if (stockHangQin.getCurrentPrice() == 0.0) {
				return null;
			}
			result.setStockID(stockCostStandardBean.getStockId());
			result.setTradePrice(stockHangQin.getCurrentPrice());

			processTrade(stockCostStandardBean, result, stockHangQin);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	protected void processTrade(StockCostStandardBean stockCostStandardBean,
			StockMonitrorResult result, StockHangQinBean stockHangQin) {
		if (checkSellCondition(stockCostStandardBean, stockHangQin)) {
			int sellNumber = sellStock(stockCostStandardBean, stockHangQin);
			result.setTradeNumber(sellNumber);
			result.setBuy(false);
		}
		
		if (checkBuyCondition(stockCostStandardBean, stockHangQin)) {
			int buyNumber = buyStock(stockCostStandardBean, stockHangQin);
			result.setTradeNumber(buyNumber);
			result.setBuy(true);
		}
	}

	protected abstract boolean checkSellCondition(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin);

	protected abstract boolean checkBuyCondition(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin);

	protected abstract int sellStock(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin);

	protected abstract int buyStock(
			StockCostStandardBean stockCostStandardBean,
			StockHangQinBean stockHangQin);

}
