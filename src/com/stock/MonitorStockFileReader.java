package com.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.klinelib.Constants;

public class MonitorStockFileReader {

	public static List readMonitorStock() {
		List result = new ArrayList();
		String filePath = Constants.DATA_ROOT
				+ Constants.MONITOR_STOCK_LIST_FILE;
		String line = null;
		StringBuffer sb = new StringBuffer();
		File monitorStockFile = new File(filePath);
		if (monitorStockFile.exists()) {
			System.out.println("该文件存在");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(monitorStockFile)));
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					if (line.indexOf("#") == 0) {
						continue;
					} else if (line.indexOf("InvestAccount") == 0) {
						InvestmentAccount investmentAccount = parseInvestmentAccount(line);
						InvestmentAccountDataHolder.getInstance()
								.setInvestmentAccount(investmentAccount);
					} else {
						StockCostStandardBean scsb = parseMonitorStock(line);
						result.add(scsb);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("该文件不存在!");
		}
		return result;
	}

	private static InvestmentAccount parseInvestmentAccount(String line) {
		Pattern pattern = Pattern.compile(" ");
		String[] investInfo = pattern.split(line);
		InvestmentAccount investmentAccount = new InvestmentAccount();
		investmentAccount.setOriginalMoney(Double.parseDouble(investInfo[1]));
		investmentAccount.setCurrentMoney(Double.parseDouble(investInfo[2]));
		investmentAccount.setCurrentAsset(Double.parseDouble(investInfo[3]));
		return investmentAccount;
	}

	private static StockCostStandardBean parseMonitorStock(String line) {
		Pattern pattern = Pattern.compile(" ");
		String[] monitorStockInfo = pattern.split(line);
		StockCostStandardBean scsb = new StockCostStandardBean();
		scsb.setStockId(monitorStockInfo[0]);
		scsb.setName(monitorStockInfo[1]);
		scsb.setCurrentCostStandardPrice(Double
				.parseDouble(monitorStockInfo[2]));
		scsb.setZf(Double.parseDouble(monitorStockInfo[3]));
		scsb.setDf(Double.parseDouble(monitorStockInfo[4]));
		scsb.setSz(Boolean.parseBoolean(monitorStockInfo[5]));
		scsb.setCurrentLevel(Integer.parseInt(monitorStockInfo[6]));
		scsb.setCurrentNumber(Integer.parseInt(monitorStockInfo[7]));
		scsb.setCurrentCanTradeNumber(Integer.parseInt(monitorStockInfo[8]));
		scsb.setTradeRankNumber(Integer.parseInt(monitorStockInfo[9]));
		scsb.setMonitoringWay(monitorStockInfo[10]);
		return scsb;
	}

	public static void writeMonitorStock(boolean needStatSumAsset) {
		String filePath = Constants.DATA_ROOT
				+ Constants.MONITOR_STOCK_LIST_FILE;
		File monitorStockFile = new File(filePath);
		try {
			if (monitorStockFile.exists()) {
				System.out.println("该文件存在");
				FileWriter fileWriter = new FileWriter(monitorStockFile);
				fileWriter.write("#Latest update at:" + new Date() + "\r\n");
				fileWriter.write("#StockID Name"
						+ "  MonitorPriceing ZF DF isSZ "
						+ "CurrentLevel CurrentNumber CurrentCanTradeNumber "
						+ "TradeRankNumber MonitoringWay\r\n");
				if (needStatSumAsset) {
					InvestmentAccountDataHolder.getInstance()
							.getInvestmentAccount().startStatAsset();
				}

				for (int i = 0; i < InvestmentAccountDataHolder.getInstance()
						.getInvestStockList().size(); i++) {
					StockCostStandardBean scsb = (StockCostStandardBean) InvestmentAccountDataHolder
							.getInstance().getInvestStockList().get(i);
					if (needStatSumAsset) {
						StockHangQinBean sqb = StockReader
								.getStockHangQinFromInternet(scsb.getStockId(),
										scsb.isSz());
						double sumSZ = sqb.getCurrentPrice()
								* scsb.getCurrentNumber();
						System.out.println("Current StockID = "
								+ sqb.getStockId() + " Current Price = "
								+ sqb.getCurrentPrice() + " CurrentNumber = "
								+ scsb.getCurrentNumber() + " SZ = " + sumSZ);
						InvestmentAccountDataHolder.getInstance()
								.getInvestmentAccount().statAsset(sumSZ);

					}

					String stockLine = generateMonitorStockLine(scsb);
					fileWriter.write(stockLine + "\r\n");
				}
				fileWriter
						.write("#OriginalMoney CurrentMoney CurrentAsset CurrentSum\r\n");
				String assetLine = generateInvestmentAccountLine(InvestmentAccountDataHolder
						.getInstance().getInvestmentAccount());
				fileWriter.write("InvestAccount " + assetLine + "\r\n");
				fileWriter.close();
			} else {
				System.out.println("该文件不存在!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String generateMonitorStockLine(StockCostStandardBean scsb) {
		String line = scsb.getStockId() + " " + scsb.getName() + " "
				+ scsb.getCurrentCostStandardPrice() + " " + scsb.getZf() + " "
				+ scsb.getDf() + " " + scsb.isSz() + " "
				+ scsb.getCurrentLevel() + " " + scsb.getCurrentNumber() + " "
				+ scsb.getCurrentCanTradeNumber() + " "
				+ scsb.getTradeRankNumber() + " " + scsb.getMonitoringWay();
		return line;
	}

	private static String generateInvestmentAccountLine(
			InvestmentAccount investmentAccount) {
		String line = investmentAccount.getOriginalMoney() + " "
				+ investmentAccount.getCurrentMoney() + " "
				+ investmentAccount.getCurrentAsset() + " "
				+ investmentAccount.getCurrentSum();
		return line;
	}

	public static void main(String[] args) {
		writeMonitorStock(false);
	}
}
