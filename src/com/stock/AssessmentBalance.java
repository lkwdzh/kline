package com.stock;

import java.util.Date;

import com.util.DateUtil;

public class AssessmentBalance {
	private double money;

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getStockcount() {
		return stockcount;
	}

	public void setStockcount(int stockcount) {
		this.stockcount = stockcount;
	}

	public double getCurrentmonitorprice() {
		return currentmonitorprice;
	}

	public void setCurrentmonitorprice(double currentmonitorprice) {
		this.currentmonitorprice = currentmonitorprice;
	}

	private int stockcount;
	private double currentmonitorprice;
	private double currentprice;

	public double getCurrentprice() {
		return currentprice;
	}

	public void setCurrentprice(double currentprice) {
		this.currentprice = currentprice;
	}

	public AssessmentBalance(double money, int stockcount,
			double currentmonitorprice) {
		this.money = money;
		this.stockcount = stockcount;
		this.currentmonitorprice = currentmonitorprice;

	}

	public String toString() {
		double summoney = money + stockcount * currentprice;
		return "Date: " + DateUtil.formatYMD(currentDate) + "Sum: " + summoney
				+ " Money:" + money + " Stockcount:" + stockcount
				+ " Monitoringprice:" + currentmonitorprice + " Currentprice:"
				+ currentprice + " MonitorLevel:" + monitorLevel
				+ " SumBuyMoney:" + sumBuyMoney;
	}

	private int monitorLevel;

	public int getMonitorLevel() {
		return monitorLevel;
	}

	public void setMonitorLevel(int monitorLevel) {
		this.monitorLevel = monitorLevel;
	}

	private double sumBuyMoney;

	public void staticSumBuyMoney(double buyMoney) {
		sumBuyMoney += buyMoney;
	}

	private Date currentDate = new Date();

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

}
