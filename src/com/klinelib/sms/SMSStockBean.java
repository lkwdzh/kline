package com.klinelib.sms;

public class SMSStockBean {
	private String stockId;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public double getZhangFu() {
		return zhangFu;
	}

	public void setZhangFu(double zhangFu) {
		this.zhangFu = zhangFu;
	}

	public double getDieFu() {
		return dieFu;
	}

	public void setDieFu(double dieFu) {
		this.dieFu = dieFu;
	}

	private String stockName;
	private double zhangFu;
	private double dieFu;

	private boolean isZS;

	public boolean isZS() {
		return isZS;
	}

	public void setZS(boolean isZS) {
		this.isZS = isZS;
	}

	public String toString() {
		return "stockId = " + stockId + "stockName = " + stockName
				+ " zhangFu = " + zhangFu + " dieFu = " + dieFu + " isZS = "
				+ isZS;
	}
	
	private double zhangFuZeng = 1;
	public double getZhangFuZeng() {
		return zhangFuZeng;
	}

	public void setZhangFuZeng(double zhangFuZeng) {
		this.zhangFuZeng = zhangFuZeng;
	}

	public double getDieFuZeng() {
		return dieFuZeng;
	}

	public void setDieFuZeng(double dieFuZeng) {
		this.dieFuZeng = dieFuZeng;
	}

	public double getZhangTargetPrice() {
		return zhangTargetPrice;
	}

	public void setZhangTargetPrice(double zhangTargetPrice) {
		this.zhangTargetPrice = zhangTargetPrice;
	}

	public double getDieTargetPrice() {
		return dieTargetPrice;
	}

	public void setDieTargetPrice(double dieTargetPrice) {
		this.dieTargetPrice = dieTargetPrice;
	}

	public double getZhangTargetPriceZeng() {
		return zhangTargetPriceZeng;
	}

	public void setZhangTargetPriceZeng(double zhangTargetPriceZeng) {
		this.zhangTargetPriceZeng = zhangTargetPriceZeng;
	}

	public double getDieTargetPriceZeng() {
		return dieTargetPriceZeng;
	}

	public void setDieTargetPriceZeng(double dieTargetPriceZeng) {
		this.dieTargetPriceZeng = dieTargetPriceZeng;
	}

	private double dieFuZeng = 1;
	private double zhangTargetPrice;
	private double dieTargetPrice;
	private double zhangTargetPriceZeng = 1;
	private double dieTargetPriceZeng = 1;
	
	private String stockCharacteristics;

	public String getStockCharacteristics() {
		return stockCharacteristics;
	}

	public void setStockCharacteristics(String stockCharacteristics) {
		this.stockCharacteristics = stockCharacteristics;
	}
	
	

}
