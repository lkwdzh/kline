package com.stock.sms;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;

import com.auto.StockAutoTrade;
import com.stock.InvestmentAccountDataHolder;
import com.stock.MonitorStockFileReader;
import com.stock.StockCostStandardBean;
import com.stock.StockMonitor;
import com.stock.StockMonitrorResult;
import com.stock.monitoringway.PointShotStockMonitor;
import com.stock.monitoringway.StairsStockMonitor;
import com.stock.monitoringway.TenDaysPriceStockMonitor;
import com.stock.timer.StockQuartzServer;
import com.util.ClassUtil;

public class StockNotifier {

	public static void main(String[] args) {
		StockQuartzServer server = new StockQuartzServer();
		try {
			server.startScheduler();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}

		InvestmentAccountDataHolder.getInstance().init();
		List monitorStockList = InvestmentAccountDataHolder.getInstance()
				.getInvestStockList();
		try {
			while (true) {
				if (isMarketOpen()) {
					for (int i = 0; i < monitorStockList.size(); i++) {
						try {
							StockCostStandardBean stockCostStandardBean = (StockCostStandardBean) monitorStockList
									.get(i);
							StockMonitor stockMonitor = getMonitor(stockCostStandardBean);
							StockMonitrorResult stockMonitrorResult = stockMonitor
									.monitorStock(stockCostStandardBean);
							if (stockMonitrorResult != null
									&& stockMonitrorResult.getTradeNumber() > 0) {
								System.out.println("Trade = "
										+ stockMonitrorResult);
								if (stockMonitrorResult.isBuy()) {
									InvestmentAccountDataHolder.getInstance()
											.getInvestmentAccount().buyStock(
													stockMonitrorResult
															.getTradeMoney());

									StockAutoTrade.buyStock(stockMonitrorResult
											.getStockID(), stockMonitrorResult
											.getTradeNumber());
								} else {
									InvestmentAccountDataHolder.getInstance()
											.getInvestmentAccount().sellStock(
													stockMonitrorResult
															.getTradeMoney());
									StockAutoTrade.sellStock(
											stockMonitrorResult.getStockID(),
											stockMonitrorResult
													.getTradeNumber());
								}
								MonitorStockFileReader.writeMonitorStock(false);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				Thread.sleep(2000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static StockMonitor getMonitor(
			StockCostStandardBean stockCostStandardBean) {
		String monitorClassPattern = "com.stock.monitoringway.XXXXStockMonitor";
		StockMonitor result = (StockMonitor)ClassUtil.getInstance(monitorClassPattern, stockCostStandardBean.getMonitoringWay());
		return result;
	}

	private static boolean isMarketOpen() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		Date morningStart = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 15);
		calendar.set(Calendar.MINUTE, 0);
		Date afterEnd = calendar.getTime();
		boolean runing = now.after(morningStart) && now.before(afterEnd)
				&& !isNoonRest() && !isWeekend();
		return runing;
	}

	private static boolean isNoonRest() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.SECOND, 0);
		Date morningEnd = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		calendar.set(Calendar.MINUTE, 0);
		Date afterStart = calendar.getTime();

		boolean runing = now.after(morningEnd) && now.before(afterStart);
		return runing;
	}

	private static boolean isWeekend() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		boolean result = (day == 1 || day == 7);
		return result;
	}
}
