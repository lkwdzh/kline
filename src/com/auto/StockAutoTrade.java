package com.auto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;

public class StockAutoTrade {
	private static IntCall exec;

	static {
		try {
			NativeCall.init();
			init();
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("\n" + "Something went wrong...");
			System.out.println(t);
		}
	}

	public static synchronized void buyStock(String stockID, int count) {
		String cmdName = "BuyStock";
		Map cmdParameters = prepareParameters(stockID, count);
		executeTradeSoftwareCMD(cmdName, cmdParameters);

	}

	public static synchronized void activateTradeSoft() {
		String cmdName = "ActivateTradeSoftwareWindow";
		executeTradeSoftwareCMD(cmdName, new HashMap());

	}

	public static void sellStock(String stockID, int count) {
		String cmdName = "SellStock";
		Map cmdParameters = prepareParameters(stockID, count);
		executeTradeSoftwareCMD(cmdName, cmdParameters);
	}

	private static Map prepareParameters(String stockID, int count) {
		Map cmdParameters = new HashMap();
		cmdParameters.put("stockID", stockID);
		cmdParameters.put("count", new Integer(count));
		return cmdParameters;
	}

	private static synchronized void executeTradeSoftwareCMD(String cmdName,
			Map cmdParameters) {
		List scripts = (List) TradeSoftScriptReader.getInstance()
				.getCMDScripts(cmdName);
		for (int i = 0; i < scripts.size(); i++) {
			String cmd = (String) scripts.get(i);
			if (cmd.indexOf("%") > 0) {
				String parameterName = cmd.substring(cmd.indexOf("%") + 1);
				parameterName = parameterName.substring(0, parameterName
						.indexOf("%"));
				String parameter = cmdParameters.get(parameterName).toString();
				cmd = cmd.replace("%" + parameterName + "%", parameter);
			}

			ahk(cmd);
			sleepshort();
		}
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public final static String SLEEP_SHORT = "1000";
	public final static String SLEEP_LONG = "2000";

	private static void sleepshort() {
		ahk(" Sleep " + SLEEP_SHORT + "");
	}

	private static void init() {
		IntCall textdll = new IntCall("AutoHotkey.dll", "ahktextdll");
		textdll.executeCall(new Object[] { "", "", "" });
		exec = new IntCall("AutoHotkey.dll", "ahkExec");
	}

	private static void ahk(String code) {
		exec.executeCall(code);
	}

	public static void main(String[] args) {
		buyStock("002139", 100);
		sellStock("601118", 100);
		activateTradeSoft();
	}
}