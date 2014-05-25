package com.stock;

public class StockMonitrorResult {
	private int tradeNumber;

	public int getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(int tradeNumber) {
		this.tradeNumber = tradeNumber;
	}

	public String getStockID() {
		return stockID;
	}

	public void setStockID(String stockID) {
		this.stockID = stockID;
	}

	public boolean isBuy() {
		return buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
	}

	private String stockID;
	private boolean buy;

	public String toString() {
		return "StockId = " + stockID + " TradeNumber = " + tradeNumber
				+ " isBuy = " + buy + " currentPrice = " + tradePrice;
	}

	private double tradePrice;

	public double getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(double tradePrice) {
		this.tradePrice = tradePrice;
	}

	public double getTradeMoney() {
		return this.tradeNumber * this.tradePrice;
	}
}
