package com.stock;

import java.util.Date;

import com.util.DateUtil;

public class KLineRec {
	public double m_close;
	public double m_high;
	public double m_low;
	public double m_money;
	public double m_open;
	public Date m_tradeDate = new Date(0L);
	public double m_volume;
	public double ma_five;
	public double ma_ten;
	public double zdf;
	

	public String toString() {
		String result = "date = " + DateUtil.formatYMD(m_tradeDate) + " 开:" + m_open + " 收:"
				+ m_close + " 高:" + m_high + " 低:" + m_low + " 量:" + m_volume
				+ " 额:" + m_money + " MA_5 = " + ma_five + " MA_10 = " + ma_ten + " 涨跌幅 : " + zdf;
		return result;
	}
}
