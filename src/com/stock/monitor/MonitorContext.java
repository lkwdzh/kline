package com.stock.monitor;

import com.stock.StockCostStandardBean;
import com.stock.StockMonitrorResult;

public class MonitorContext {
	private MonitorStrategy monitorStrategy;

	public MonitorContext(String type) {
		if (type == null) {
			throw new IllegalArgumentException("monitor.type.is.null");
		}
		String monitorStrategyClassName = "com.stock.monitor." + type
				+ "MonitorStrategy";
		try {
			Class monitorStrategyClass = Class
					.forName(monitorStrategyClassName);
			monitorStrategy = (MonitorStrategy) monitorStrategyClass
					.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public StockMonitrorResult monitorStock(
			StockCostStandardBean stockCostStandardBean) {
		return monitorStrategy.monitorStock(stockCostStandardBean);
	}
}
