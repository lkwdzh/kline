package com.klinelib.ma5ma10;

public class MA5MA10Balance {

	private static MA5MA10Balance instance = new MA5MA10Balance();

	public static MA5MA10Balance getInstance() {
		return instance;
	}

	private MA5MA10Balance() {

	}

	private double buyPrice = 0;

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
		this.lastPrice = buyPrice;
	}

	public void reSet() {
		this.buyPrice = 0;
	}

	private int kuiStockCount;

	public int getKuiStockCount() {
		return kuiStockCount;
	}

	public void setKuiStockCount(int kuiStockCount) {
		this.kuiStockCount = kuiStockCount;
	}

	private int yingStockCount;

	public int getYingStockCount() {
		return yingStockCount;
	}

	public void setYingStockCount(int yingStockCount) {
		this.yingStockCount = yingStockCount;
	}

	public void reSetForStock() {
		this.buyPrice = 0;
		totalEarn = totalEarn + earn;
		if (earn > 0) {
			yingStockCount++;
		} else {
			kuiStockCount++;
		}
		this.earn = 0;
	}

	public void reSetForMarket() {
		this.buyPrice = 0;
		this.yingStockCount = 0;
		kuiStockCount = 0;
		this.earn = 0;
		this.totalEarn = 0;
	}

	public void addEarn(double earn) {
		this.earn = this.earn + earn;
	}

	private double earn = 0;
	private double lastPrice = 0;

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double getEarn() {
		return earn;
	}

	private double totalEarn;

	public double getTotalEarn() {
		return totalEarn;
	}

	public void setTotalEarn(double totalEarn) {
		this.totalEarn = totalEarn;
	}

	public String toString() {
		return "buyPrice = " + buyPrice + " Earn = " + earn;
	}
}
