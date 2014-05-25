package com.stock;

public class InvestmentAccount {
	private double originalMoney;
	private double currentMoney;
	private double currentAsset;

	public double getOriginalMoney() {
		return originalMoney;
	}

	public void setOriginalMoney(double originalMoney) {
		this.originalMoney = originalMoney;
	}

	public double getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(double currentMoney) {
		this.currentMoney = currentMoney;
	}

	public double getCurrentAsset() {
		return currentAsset;
	}

	public void setCurrentAsset(double currentAsset) {
		this.currentAsset = currentAsset;
	}

	public double getCurrentSum() {
		return currentAsset + currentMoney;
	}

	public String toString() {
		return "originalMoney = " + originalMoney + " currentMoney = "
				+ currentMoney + "currentAsset = " + currentAsset
				+ "currentSum = " + this.getCurrentSum();
	}
	
	public void sellStock(double incomeMoney){
		this.currentMoney += incomeMoney;
	}
	public void buyStock(double paidMoney){
		this.currentMoney -= paidMoney;
	}
	
	public void startStatAsset(){
		this.currentAsset = 0;
	}

	public void statAsset(double assertMoney){
		this.currentAsset += assertMoney;
	}
	
}
