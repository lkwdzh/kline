package com.stock.monitor;

import com.stock.StockCostStandardBean;
import com.stock.StockMonitrorResult;

public interface MonitorStrategy {
	StockMonitrorResult monitorStock(StockCostStandardBean stockCostStandardBean);
}
