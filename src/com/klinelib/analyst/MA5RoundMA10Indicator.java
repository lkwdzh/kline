package com.klinelib.analyst;

public class MA5RoundMA10Indicator {
	private boolean down;

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isScope() {
		return scope;
	}

	public void setScope(boolean scope) {
		this.scope = scope;
	}

	private boolean up;
	private boolean scope;
	private double downRatio;

	public double getDownRatio() {
		return downRatio;
	}

	public void setDownRatio(double downRatio) {
		this.downRatio = downRatio;
	}

	public double getUpRatio() {
		return upRatio;
	}

	public void setUpRatio(double upRatio) {
		this.upRatio = upRatio;
	}

	private double upRatio;

	private int dayNo;

	public int getDayNo() {
		return dayNo;
	}

	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}

	public String toString() {
		return dayNo + " " + up + " " + upRatio + " " + down + " " + downRatio
				+ " " + scope;
	}

}
