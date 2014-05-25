package com.stock;

import java.util.List;

public class InvestmentAccountDataHolder {
	private List investStockList;

	public List getInvestStockList() {
		return investStockList;
	}

	public void setInvestStockList(List investStockList) {
		this.investStockList = investStockList;
	}

	public InvestmentAccount getInvestmentAccount() {
		return investmentAccount;
	}

	public void setInvestmentAccount(InvestmentAccount investmentAccount) {
		this.investmentAccount = investmentAccount;
	}

	private InvestmentAccount investmentAccount;
	private static InvestmentAccountDataHolder instance = new InvestmentAccountDataHolder();

	public static InvestmentAccountDataHolder getInstance() {
		return instance;
	}

	private InvestmentAccountDataHolder() {

	}

	public void init() {
		this.investStockList = MonitorStockFileReader.readMonitorStock();
	}

	public void closeStockMarket() {
		System.out.println("closeStockMarket");
		if (investStockList == null) {
			this.init();
		}
		for (int i = 0; i < investStockList.size(); i++) {
			StockCostStandardBean stockCostStandardBean = (StockCostStandardBean) investStockList
					.get(i);
			if (stockCostStandardBean.getCurrentNumber() > 0
					&& stockCostStandardBean.getCurrentCanTradeNumber() == 0) {
				stockCostStandardBean
						.setCurrentCanTradeNumber(stockCostStandardBean
								.getCurrentNumber());
			}
			if (stockCostStandardBean.getCurrentNumber() == 0
					&& stockCostStandardBean.getCurrentCanTradeNumber() == 0
					&& stockCostStandardBean.getCurrentCostStandardPrice() > 0) {
				stockCostStandardBean.setCurrentCostStandardPrice(0);
			}
		}
		MonitorStockFileReader.writeMonitorStock(true);
	}
}
