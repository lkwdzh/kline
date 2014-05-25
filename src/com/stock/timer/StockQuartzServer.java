package com.stock.timer;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import com.klinelib.Constants;



public class StockQuartzServer {

	public static void main(String[] args) {
		StockQuartzServer server = new StockQuartzServer();
		try {
	       	
			
			server.startScheduler();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	public void startScheduler() throws SchedulerException {
		// Use the factory to create a Scheduler instance
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		// JobDetail holds the definition for Jobs
		JobDetail closeMarketJob = new JobDetail("CloseStockMarket",
				Scheduler.DEFAULT_GROUP, CloseStockMarket.class);
		// Other neccessary Job parameters here
		// Create a Trigger that fires every 60 seconds
		Trigger closeMarketTrigger = TriggerUtils.makeDailyTrigger("SendThanksSMJob", 15, 2);
		// Setup the Job and Trigger with the Scheduler
		scheduler.scheduleJob(closeMarketJob, closeMarketTrigger);
		// Start the Scheduler running
		Trigger triggerActivateTradeSoft = TriggerUtils.makeMinutelyTrigger("ActivateTradeSoft", Constants.TRADE_SOFT_ACTIVATE_INTERVAL, 10000);
		JobDetail activeTradeSoftJob = new JobDetail("ActivateTradeSoft",
				Scheduler.DEFAULT_GROUP, ActivateTradeSoft.class);
		scheduler.scheduleJob(activeTradeSoftJob, triggerActivateTradeSoft);
		scheduler.start();
	}
}