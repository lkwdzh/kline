package com.stock;

public class StockCostStandardBean {
	private String stockId;
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public double getCurrentCostStandardPrice() {
		return currentCostStandardPrice;
	}
	public void setCurrentCostStandardPrice(double currentCostStandardPrice) {
		this.currentCostStandardPrice = currentCostStandardPrice;
	}
	public boolean isSz() {
		return sz;
	}
	public void setSz(boolean sz) {
		this.sz = sz;
	}
	private double currentCostStandardPrice;
	private boolean sz;
	
	private double zf;
	public double getZf() {
		return zf;
	}
	public void setZf(double zf) {
		this.zf = zf;
	}
	public double getDf() {
		return df;
	}
	public void setDf(double df) {
		this.df = df;
	}
	private double df;
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private int currentLevel; 
	public int getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	public int getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}
	public int getCurrentCanTradeNumber() {
		return currentCanTradeNumber;
	}
	public void setCurrentCanTradeNumber(int currentCanTradeNumber) {
		this.currentCanTradeNumber = currentCanTradeNumber;
	}
	public int getTradeRankNumber() {
		return tradeRankNumber;
	}
	public void setTradeRankNumber(int tradeRankNumber) {
		this.tradeRankNumber = tradeRankNumber;
	}
	private int currentNumber; 
	private int currentCanTradeNumber;
	private int tradeRankNumber;
	private String monitoringWay;
	public String getMonitoringWay() {
		return monitoringWay;
	}
	public void setMonitoringWay(String monitoringWay) {
		this.monitoringWay = monitoringWay;
	}
	
}
